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
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.RequestSendByUser
import dev.bugstitch.socionect.domain.models.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseOrganisationScreen(
    query: String,
    results: List<Organisation>,
    requestedOrg:List<Organisation>,
    userOrg: List<Organisation>,
    loading: Boolean,
    error: String?,
    onQueryChange: (String) -> Unit,
    onSendRequest: (Organisation) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Discover Organisations") })
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
                label = { Text("Search organisations") },
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
                items(results) { organisation ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(organisation.name, style = MaterialTheme.typography.titleMedium)
                                Text("@${organisation.description}", style = MaterialTheme.typography.bodySmall)
                            }
                            Column {
                                when{
                                    userOrg.contains(organisation) -> {
                                        Text("joined")
                                    }
                                        requestedOrg.contains(organisation) -> {
                                        Text("requested")
                                    }
                                    else -> {
                                        Button(onClick = {onSendRequest(organisation)}){
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