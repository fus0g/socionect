package dev.bugstitch.socionect.presentation.screens.organisation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.User

@Composable
fun OrganisationReceivedRequestScreen(
    list: List<User>,
    onAccept: (User) -> Unit,
    onDecline: (User) -> Unit
){

    Column(modifier = Modifier
        .fillMaxSize()
        .navigationBarsPadding()
        .statusBarsPadding()){

        LazyColumn {
            item {
                Text("Received Requests")
            }
            list.forEach {
                item {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(it.name, style = MaterialTheme.typography.titleMedium)
                            Text("@${it.username}", style = MaterialTheme.typography.bodySmall)
                        }
                        Row {

                            Button(onClick = { onAccept(it) }) {
                                Text("Accept")

                            }

                            Button(onClick = { onDecline(it) }) {
                                Text("Decline")
                            }
                        }
                    }
                }
            }
        }

    }

}