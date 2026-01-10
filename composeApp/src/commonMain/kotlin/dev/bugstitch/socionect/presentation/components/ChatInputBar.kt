package dev.bugstitch.socionect.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    enabled: Boolean,
    isLarge: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = if (isLarge) 16.dp else 8.dp,
                vertical = 8.dp
            )
            .imePadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Message") },
            maxLines = 4,
            shape = MaterialTheme.shapes.large
        )

        Spacer(Modifier.width(8.dp))

        Button(
            onClick = onSend,
            enabled = enabled && text.isNotBlank(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text("Send")
        }
    }
}
