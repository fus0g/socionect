package dev.bugstitch.socionect.presentation.screens.organisation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bugstitch.socionect.domain.models.Organisation

@Composable
fun CreateCoalitionScreen(
    name: String,
    description: String,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    organisations: List<Organisation>,
    onOrganisationSelected: (Organisation) -> Unit,
    selectedOrganisations: List<Organisation>,
    onCreateClick: () -> Unit,
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
            modifier = Modifier
                .widthIn(max = if (isLarge) 520.dp else 360.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Text(
                    text = "Create Coalition",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // ───── Coalition details ─────
            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Coalition name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = MaterialTheme.shapes.large
                )
            }

            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = MaterialTheme.shapes.large
                )
            }

            item {
                Button(
                    onClick = onCreateClick,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    enabled = name.isNotBlank()
                ) {
                    Text("Create Coalition")
                }
            }

            // ───── Selected organisations ─────
            if (selectedOrganisations.isNotEmpty()) {
                item {
                    Text(
                        text = "Selected organisations",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                items(selectedOrganisations, key = { it.id }) { org ->
                    SimpleOrganisationRow(
                        organisation = org,
                        actionText = "Added",
                        enabled = false,
                        onAction = {}
                    )
                }
            }

            // ───── Search & add organisations ─────
            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Add organisations",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            item {
                OutlinedTextField(
                    value = searchValue,
                    onValueChange = onSearchValueChange,
                    label = { Text("Search organisation") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = MaterialTheme.shapes.large
                )
            }

            items(organisations, key = { it.id }) { org ->
                SimpleOrganisationRow(
                    organisation = org,
                    actionText = if (selectedOrganisations.contains(org)) "Added" else "Add",
                    enabled = !selectedOrganisations.contains(org),
                    onAction = { onOrganisationSelected(org) }
                )
            }
        }
    }
}


@Composable
private fun SimpleOrganisationRow(
    organisation: Organisation,
    actionText: String,
    enabled: Boolean,
    onAction: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = organisation.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = organisation.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.width(8.dp))

        if (enabled) {
            Button(onClick = onAction) {
                Text(actionText)
            }
        } else {
            Text(
                text = actionText,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}
