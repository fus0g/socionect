package dev.bugstitch.socionect.presentation.screens.organisation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateSubTopicScreen(
    subtopicName: String,
    subtopicDescription: String,
    error: String?,
    onSubtopicNameChange: (String) -> Unit,
    onSubtopicDescriptionChange: (String) -> Unit,
    onSubtopicCreate: () -> Unit,
    isLarge: Boolean
) {

    val baseModifier = if (isLarge) {
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    } else {
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    }

    Column(
        modifier = baseModifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Form container
        Column(
            modifier = Modifier
                .widthIn(max = if (isLarge) 420.dp else 360.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Create Subtopic",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = subtopicName,
                onValueChange = onSubtopicNameChange,
                label = { Text("Subtopic name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            )

            OutlinedTextField(
                value = subtopicDescription,
                onValueChange = onSubtopicDescriptionChange,
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                minLines = 3
            )

            if (!error.isNullOrBlank()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = onSubtopicCreate,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Text("Create Subtopic")
            }
        }
    }
}
