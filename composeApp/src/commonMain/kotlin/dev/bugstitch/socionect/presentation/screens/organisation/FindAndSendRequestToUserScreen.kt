package dev.bugstitch.socionect.presentation.screens.organisation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bugstitch.socionect.domain.models.User
import dev.bugstitch.socionect.presentation.components.BrowseUserItem

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
    onRequestClick: (User) -> Unit,
    isLarge: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyColumn(
            modifier = Modifier.widthIn(max = if (isLarge) 520.dp else 360.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Text(
                    text = "Discover Users",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    label = { Text("Search users") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large
                )
            }

            if (loading) {
                item {
                    CircularProgressIndicator()
                }
            }

            if (!error.isNullOrBlank()) {
                item {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            items(results, key = { "user-${it.id}" }) { user ->
                BrowseUserItem(
                    user = user,
                    isJoined = memberUsers.any { it.id == user.id },
                    isRequested = requestedUsers.any { it.id == user.id },
                    onSendRequest = { onRequestClick(user) }
                )
            }

        }
    }
}
