package dev.bugstitch.socionect.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bugstitch.socionect.domain.models.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSearchScreen(
    query: String,
    results: List<User>,
    loading: Boolean,
    error: String?,
    onQueryChange: (String) -> Unit,
    onUserClick: (User) -> Unit
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
                            .clickable { onUserClick(user) }
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
                        }
                    }
                }
            }
        }
    }
}
