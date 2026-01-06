package dev.bugstitch.socionect.presentation.screens.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bugstitch.socionect.domain.models.Organisation

@Composable
fun UserReceivedRequestScreen(
    list: List<Organisation>,
    onAccept: (Organisation) -> Unit,
    onDecline: (Organisation) -> Unit
){
    Column(modifier = Modifier.fillMaxSize()) {

        LazyColumn(modifier = Modifier.fillMaxSize()){
            items(list, key = { it.id }) { org ->
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(org.name, style = MaterialTheme.typography.titleMedium)
                        Text(org.description, style = MaterialTheme.typography.bodySmall)
                    }
                    Row {
                        Button(onClick = { onAccept(org) }) {
                            Text("Accept")
                        }
                        Button(onClick = { onDecline(org) }) {
                            Text("Decline")
                        }
                    }
                }
            }
        }

    }
}