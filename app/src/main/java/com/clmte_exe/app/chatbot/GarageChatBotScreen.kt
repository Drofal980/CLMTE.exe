package com.clmte_exe.app.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clmte_exe.sub_apps.settings.ThemeManager
import com.clmte_exe.app.ui.*
import kotlinx.coroutines.launch

@Composable
fun GarageChatBotScreen() {
    GarageChatBotContent()
}

@Composable
fun GarageChatBotContent() {
    val appBg = if (ThemeManager.isDarkMode) Color(0xFF2D2D2D) else Win98Gray
    val panelBg = if (ThemeManager.isDarkMode) Color(0xFF3A3A3A) else Color(0xFFD4D0C8)
    val inputBg = if (ThemeManager.isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (ThemeManager.isDarkMode) Color.White else Win98Black

    val messages = remember { mutableStateListOf<ChatMessage>() }
    var input by remember { mutableStateOf("") }
    var isReplying by remember { mutableStateOf(false) }
    var isLoadingHistory by remember { mutableStateOf(true) }
    var showClearConfirm by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val history = GarageChatBotEngine.loadChatHistory()
        messages.clear()
        if (history.isEmpty()) {
            messages.add(
                ChatMessage(
                    id = GarageChatBotEngine.makeId(),
                    text = "Garage Assistant ready. Ask anything.",
                    fromUser = false
                )
            )
        } else {
            messages.addAll(history)
        }
        isLoadingHistory = false
    }

    val listState = rememberLazyListState()
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    fun sendMessage(text: String) {
        val userText = text.trim()
        if (userText.isEmpty() || isReplying || isLoadingHistory) return

        val userMessage = ChatMessage(
            id = GarageChatBotEngine.makeId(),
            text = userText,
            fromUser = true
        )
        messages.add(userMessage)
        input = ""

        scope.launch {
            GarageChatBotEngine.saveChatHistory(messages.toList())
            isReplying = true
            val reply = OpenAiGarageAssistant.getReply(userText)
            val assistantMessage = ChatMessage(
                id = GarageChatBotEngine.makeId(),
                text = reply,
                fromUser = false
            )
            messages.add(assistantMessage)
            GarageChatBotEngine.saveChatHistory(messages.toList())
            isReplying = false
        }
    }

    fun clearAllMessages() {
        if (isReplying || isLoadingHistory) return
        scope.launch {
            GarageChatBotEngine.clearChatHistory()
            messages.clear()
            messages.add(
                ChatMessage(
                    id = GarageChatBotEngine.makeId(),
                    text = "Garage Assistant ready. Ask anything.",
                    fromUser = false
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appBg)
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "AI Assistant (Garage)",
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Win98Gray)
                    .win98Border(pressed = false)
                    .clickable(enabled = !isReplying && !isLoadingHistory) { showClearConfirm = true }
                    .padding(horizontal = 6.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Clear chat",
                    tint = Win98Black,
                    modifier = Modifier
                        .width(14.dp)
                        .height(14.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(panelBg)
                .win98Border(pressed = true)
                .padding(8.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isLoadingHistory) {
                    item(key = "history-loader") {
                        AssistantTypingBubble()
                    }
                }
                items(messages, key = { it.id }) { msg ->
                    ChatBubble(message = msg)
                }
                if (isReplying) {
                    item(key = "typing-loader") {
                        AssistantTypingBubble()
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Demo questions:",
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(GarageChatBotEngine.demoQuestions) { question ->
                    Box(
                        modifier = Modifier
                            .background(Win98Gray)
                            .win98Border(pressed = false)
                            .clickable(enabled = !isReplying && !isLoadingHistory) { sendMessage(question) }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(text = question, fontSize = 11.sp, color = Win98Black)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(inputBg)
                    .win98Border(pressed = true)
            ) {
                TextField(
                    value = input,
                    onValueChange = { input = it },
                    placeholder = { Text("Ask anything...", fontSize = 12.sp) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        cursorColor = textColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Win98Gray)
                    .win98Border(pressed = false)
                    .clickable(enabled = !isReplying && !isLoadingHistory) { sendMessage(input) }
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Text(text = "Send", color = Win98Blue, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }

    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = {
                Text(text = "Clear chat history?")
            },
            text = {
                Text(text = "This will remove all chat messages.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showClearConfirm = false
                        clearAllMessages()
                    }
                ) {
                    Text(text = "Clear")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}

@Composable
private fun AssistantTypingBubble() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.48f)
                .background(Color(0xFFEDEDED))
                .win98Border(pressed = false)
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(12.dp)
                        .height(12.dp),
                    color = Win98Blue,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Typing...",
                    color = Win98Black,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    val userBubble = Color(0xFFB8D8FF)
    val botBubble = Color(0xFFEDEDED)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.fromUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.82f)
                .background(if (message.fromUser) userBubble else botBubble)
                .win98Border(pressed = false)
                .padding(8.dp)
        ) {
            Text(
                text = message.text,
                color = Win98Black,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}
