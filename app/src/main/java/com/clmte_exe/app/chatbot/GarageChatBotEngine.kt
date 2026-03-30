package com.clmte_exe.app.chatbot

import com.clmte_exe.app.api_calls.FirestoreManager
import kotlin.random.Random

data class ChatMessage(
    val id: String,
    val text: String,
    val fromUser: Boolean,
    val createdAt: Long = System.currentTimeMillis()
)

data class ChatHistoryDocument(
    val messages: List<ChatMessageDto> = emptyList()
)

data class ChatMessageDto(
    val id: String = "",
    val text: String = "",
    val fromUser: Boolean = false,
    val createdAt: Long = 0L
)

object GarageChatBotEngine {
    private const val CHAT_COLLECTION = "garage_chat"
    private const val CHAT_DOCUMENT_ID = "single_user_history"
    private const val MAX_STORED_MESSAGES = 200

    private val firestoreManager = FirestoreManager()

    val demoQuestions = listOf(
        "When should I change engine oil?",
        "How often should tires rotate?",
        "Why is my car battery draining?",
        "My brakes are squeaking, what should I do?",
        "What service is needed at 50,000 km?"
    )

    val systemPrompt = """
        You are Garage Assistant for an internal workshop app.
        Answer any question the user asks.
        Keep responses clear and concise (2-5 lines).
        Prefer safety-first suggestions for critical issues (brakes, overheating, battery faults).
    """.trimIndent()

    fun makeId(): String = Random.nextInt(100000, 999999).toString()

    suspend fun loadChatHistory(): List<ChatMessage> {
        return runCatching {
            val document = firestoreManager.getDocument<ChatHistoryDocument>(
                collection = CHAT_COLLECTION,
                documentId = CHAT_DOCUMENT_ID
            )

            document?.messages
                .orEmpty()
                .sortedBy { it.createdAt }
                .map { dto ->
                    ChatMessage(
                        id = dto.id.ifBlank { makeId() },
                        text = dto.text,
                        fromUser = dto.fromUser,
                        createdAt = dto.createdAt
                    )
                }
        }.getOrElse { emptyList() }
    }

    suspend fun saveChatHistory(messages: List<ChatMessage>) {
        val trimmedMessages = messages.takeLast(MAX_STORED_MESSAGES)

        val payload = ChatHistoryDocument(
            messages = trimmedMessages.map { message ->
                ChatMessageDto(
                    id = message.id,
                    text = message.text,
                    fromUser = message.fromUser,
                    createdAt = message.createdAt
                )
            }
        )

        runCatching {
            firestoreManager.saveDocument(
                collection = CHAT_COLLECTION,
                documentId = CHAT_DOCUMENT_ID,
                data = payload
            )
        }
    }

    suspend fun clearChatHistory() {
        runCatching {
            firestoreManager.deleteDocument(
                collection = CHAT_COLLECTION,
                documentId = CHAT_DOCUMENT_ID
            )
        }
    }
}