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
import dev.bugstitch.socionect.domain.models.Coalition
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.OrganisationSubtopic

@Composable
fun OrganisationMainScreen(
    organisation: Organisation,
    subtopics: List<OrganisationSubtopic>,
    onSubtopicPressed: (OrganisationSubtopic) -> Unit,
    onSubtopicCreatePressed: () -> Unit,
    onReceivedRequestsClick: (organisation: Organisation) -> Unit,
    onInviteUsersClick: (organisation: Organisation) -> Unit,
    coalitions: List<Coalition>,
    onCreateCoalitionClick: (organisation: Organisation) -> Unit,
    onCoalitionRequestsClick:(organisation: Organisation)->Unit,
    onCoalitionPressed: (Coalition) -> Unit
){
    Scaffold(modifier = Modifier.statusBarsPadding(),topBar = {
        Text(organisation.name)
    }) {

        LazyColumn(modifier = Modifier.padding(top = 32.dp)) {
            item {
                Text("Subtopics")
            }
            subtopics.forEach {
                item {
                    Column(modifier = Modifier.clickable { onSubtopicPressed(it) }) {
                        Text(it.name)
                        Text(it.description)
                    }
                }
            }

            item {
                Text("Coalitions")
            }

            coalitions.forEach {
                item {
                    Column(modifier = Modifier.clickable {onCoalitionPressed(it) }) {
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
            item {
                Button(
                    onClick = { onInviteUsersClick(organisation) }
                ){
                    Text("Invite Users")
                }
            }
            item{
                Button(
                    onClick = { onCreateCoalitionClick(organisation) }
                ){
                    Text("Create Coalition")
                }
            }

            item {
                Button(
                    onClick = {
                        onCoalitionRequestsClick(organisation)
                    }
                ){
                    Text("Coalition Requests")
                }
            }
        }

    }
}