package dev.bugstitch.socionect.presentation.screens.organisation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bugstitch.socionect.domain.models.Organisation

@Composable
fun CreateSubTopicScreen(
    subtopicName: String,
    subtopicDescription: String,
    error: String?,
    onSubtopicNameChange: (String) -> Unit,
    onSubtopicDescriptionChange: (String) -> Unit,
    onSubtopicCreate: () -> Unit
){

    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = subtopicName,
            onValueChange = {onSubtopicNameChange(it)},
            label = { Text("Sub-Topic Name") }
        )
        OutlinedTextField(
            value = subtopicDescription,
            onValueChange = {onSubtopicDescriptionChange(it)},
            label = { Text("Description") }
        )
        if(!error.isNullOrBlank()){
            Text("Error: $error")
        }
        Button(onClick = onSubtopicCreate) {
            Text("Create")
        }
    }

}