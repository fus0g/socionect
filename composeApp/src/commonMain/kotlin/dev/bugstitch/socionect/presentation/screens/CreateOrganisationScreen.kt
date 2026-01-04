package dev.bugstitch.socionect.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CreateOrganisationScreen(
    organisationName: String,
    organisationDescription: String,
    error: String?,
    onOrganisationNameChange: (String) -> Unit,
    onOrganisationDescriptionChange: (String) -> Unit,
    onOrganisationCreate: () -> Unit
){

    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = organisationName,
            onValueChange = {onOrganisationNameChange(it)},
            label = { Text("Organisation") }
        )
        OutlinedTextField(
            value = organisationDescription,
            onValueChange = {onOrganisationDescriptionChange(it)},
            label = { Text("Description") }
        )
        if(!error.isNullOrBlank()){
            Text("Error: $error")
        }
        Button(onClick = onOrganisationCreate) {
            Text("Create")
        }
    }

}