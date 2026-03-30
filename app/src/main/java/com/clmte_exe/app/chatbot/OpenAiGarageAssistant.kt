package com.clmte_exe.app.chatbot

import com.clmte_exe.app.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object OpenAiGarageAssistant {
    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    private val candidateModels = listOf("gpt-5.4")

    suspend fun getReply(userText: String): String {
        val key = BuildConfig.OPENAI_API_KEY.trim()
        if (key.isBlank()) {
            return "OpenAI error: missing API key. Add OPENAI_API_KEY and try again."
        }

        return withContext(Dispatchers.IO) {
            var lastError: String? = null

            for (model in candidateModels) {
                val attempt = runCatching {
                    callResponsesApi(
                        apiKey = key,
                        model = model,
                        userText = userText
                    )
                }

                if (attempt.isSuccess) {
                    val text = attempt.getOrNull().orEmpty().trim()
                    if (text.isNotBlank()) {
                        return@withContext text
                    }
                } else {
                    lastError = attempt.exceptionOrNull()?.message
                }
            }

            if (!lastError.isNullOrBlank()) {
                "OpenAI error: $lastError"
            } else {
                "OpenAI error: empty response."
            }
        }
    }

    private fun callResponsesApi(apiKey: String, model: String, userText: String): String {
        val payload = JSONObject().apply {
            put("model", model)
            put("temperature", 0.4)
            put("max_output_tokens", 220)
            put(
                "input",
                JSONArray().apply {
                    put(
                        JSONObject().apply {
                            put("role", "system")
                            put("content", GarageChatBotEngine.systemPrompt)
                        }
                    )
                    put(
                        JSONObject().apply {
                            put("role", "user")
                            put("content", userText)
                        }
                    )
                }
            )
        }

        val request = Request.Builder()
            .url("https://api.openai.com/v1/responses")
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(payload.toString().toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            val bodyString = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                val apiMessage = runCatching {
                    JSONObject(bodyString)
                        .optJSONObject("error")
                        ?.optString("message")
                }.getOrNull()

                throw IllegalStateException(apiMessage ?: "OpenAI HTTP ${response.code}")
            }

            val json = JSONObject(bodyString)

            val outputText = json.optString("output_text")
            if (outputText.isNotBlank()) return outputText

            val outputArray = json.optJSONArray("output") ?: return ""
            for (i in 0 until outputArray.length()) {
                val item = outputArray.optJSONObject(i) ?: continue
                val contentArray = item.optJSONArray("content") ?: continue
                for (j in 0 until contentArray.length()) {
                    val content = contentArray.optJSONObject(j) ?: continue
                    val text = content.optString("text")
                    if (text.isNotBlank()) return text
                }
            }

            return ""
        }
    }
}