package dev.bugstitch.socionect.presentation.screens.organisation.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bugstitch.socionect.domain.models.CoalitionMessage
import dev.bugstitch.socionect.presentation.components.ChatInputBar
import dev.bugstitch.socionect.presentation.components.ChatMessageBubble
import dev.bugstitch.socionect.presentation.components.ChatTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganisationCoalitionChatScreen(
    chatTitle: String,
    messages: List<CoalitionMessage>,
    loading: Boolean,
    onSend: (String) -> Unit,
    onBack: () -> Unit,
    isLarge: Boolean
) {
    val listState = rememberLazyListState()
    var text by remember { mutableStateOf("") }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            ChatTopBar(
                title = chatTitle,
                showBack = !isLarge,
                onBack = onBack
            )
        },
        bottomBar = {
            ChatInputBar(
                text = text,
                onTextChange = { text = it },
                onSend = {
                    if (text.isNotBlank()) {
                        onSend(text.trim())
                        text = ""
                    }
                },
                enabled = !loading,
                isLarge = isLarge
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            if (loading && messages.isEmpty()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(messages, key = { "msg-${it.id}" }) { msg ->
                    ChatMessageBubble(
                        senderId = msg.senderId,
                        senderName = msg.senderName,
                        message = msg.message,
                        isLarge = isLarge
                    )
                }
            }
        }
    }
}
