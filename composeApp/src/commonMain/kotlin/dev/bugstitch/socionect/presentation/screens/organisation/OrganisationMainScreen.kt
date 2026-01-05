package dev.bugstitch.socionect.presentation.screens.organisation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.OrganisationSubtopic

@Composable
fun OrganisationMainScreen(
    organisation: Organisation,
    subtopics: List<OrganisationSubtopic>,
    onSubtopicPressed: (OrganisationSubtopic) -> Unit,
    onSubtopicCreatePressed: () -> Unit,
    onReceivedRequestsClick: (organisation: Organisation) -> Unit
){
    Scaffold(modifier = Modifier.statusBarsPadding(),topBar = {
        Text(organisation.name)
    }) {

        LazyColumn(modifier = Modifier.padding(top = 32.dp)) {

            subtopics.forEach {
                item {
                    Column(modifier = Modifier.clickable { onSubtopicPressed(it) }) {
                        Text(it.name)
                        Text(it.description)
                    }
                }
            }

            item {
                Button(onClick = onSubtopicCreatePressed){
                    Text("Create Subtopic")
                }
            }

            item {
                Button(onClick = { onReceivedRequestsClick(organisation) }){
                    Text("Received Requests")
                }
            }
        }

    }
}