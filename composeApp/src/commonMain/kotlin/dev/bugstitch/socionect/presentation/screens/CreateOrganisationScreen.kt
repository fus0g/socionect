package dev.bugstitch.socionect.presentation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateOrganisationScreen(
    organisationName: String,
    organisationDescription: String,
    error: String?,
    onOrganisationNameChange: (String) -> Unit,
    onOrganisationDescriptionChange: (String) -> Unit,
    onOrganisationCreate: () -> Unit,
    isLarge: Boolean
) {

    val containerModifier = if (isLarge) {
        Modifier
            .widthIn(max = 420.dp)
            .fillMaxHeight()
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(24.dp)
    } else {
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = containerModifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Create Organisation",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = organisationName,
                onValueChange = onOrganisationNameChange,
                label = { Text("Organisation name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = organisationDescription,
                onValueChange = onOrganisationDescriptionChange,
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            if (!error.isNullOrBlank()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Button(
                onClick = onOrganisationCreate,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create organisation")
            }
        }
    }
}
