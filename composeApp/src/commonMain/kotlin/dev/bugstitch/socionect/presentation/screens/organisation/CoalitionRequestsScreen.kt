package dev.bugstitch.socionect.presentation.screens.organisation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.bugstitch.socionect.domain.models.CoalitionRequest

@Composable
fun CoalitionRequestsScreen(
    list: List<CoalitionRequest>,
    onAccept:(CoalitionRequest)->Unit,
    onDecline:(CoalitionRequest)->Unit
){
    LazyColumn {
        item {
            Text("Coalition Requests")
        }
        list.forEach {
            item {
                Row {
                    Column {
                        Text(it.coalition.name)
                        Text(it.coalition.description)
                    }
                    Column {
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