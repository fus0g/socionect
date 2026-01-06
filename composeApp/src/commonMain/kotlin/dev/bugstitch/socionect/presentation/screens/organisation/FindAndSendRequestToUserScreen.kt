package dev.bugstitch.socionect.presentation.screens.organisation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bugstitch.socionect.domain.models.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindAndSendRequestToUserScreen(
    query: String,
    results: List<User>,
    requestedUsers: List<User>,
    memberUsers: List<User>,
    loading: Boolean,
    error: String?,
    onQueryChange: (String) -> Unit,
    onRequestClick: (User) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Discover Users") })
        }
    ) { inner ->

        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                label = { Text("Search users") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            if (loading) {
                CircularProgressIndicator()
                return@Scaffold
            }

            if (error != null) {
                Text(error, color = MaterialTheme.colorScheme.error)
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(results) { user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(user.name, style = MaterialTheme.typography.titleMedium)
                                Text("@${user.username}", style = MaterialTheme.typography.bodySmall)
                            }
                            Column {
                                when{
                                    memberUsers.any { it.id == user.id } -> {
                                        Text("joined")
                                    }
                                    requestedUsers.any { it.id == user.id } -> {
                                        Text("requested")
                                    }
                                    else -> {
                                        Button(onClick = {onRequestClick(user)}){
                                            Text("request")
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}