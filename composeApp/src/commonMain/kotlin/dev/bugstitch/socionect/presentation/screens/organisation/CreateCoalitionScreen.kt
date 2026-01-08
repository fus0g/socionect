package dev.bugstitch.socionect.presentation.screens.organisation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.bugstitch.socionect.domain.models.Organisation

@Composable
fun CreateCoalitionScreen(
    name: String,
    description: String,
    onNameChange:(String)->Unit,
    onDescriptionChange:(String)->Unit,
    searchValue: String,
    onSearchValueChange:(String)->Unit,
    organisations: List<Organisation>,
    onOrganisationSelected:(Organisation)->Unit,
    selectedOrganisations:List<Organisation>,
    onCreateClick:()->Unit
){

    Column(modifier = Modifier
        .fillMaxSize()
        .navigationBarsPadding()
        .statusBarsPadding()
    ) {

        LazyColumn {
            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { onNameChange(it) },
                    label = { Text("Name") }
                )
            }

            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { onDescriptionChange(it) },
                    label = { Text("Description") }
                )
            }

            item {
                Button(
                    onClick = onCreateClick
                ){
                    Text("Create Coalition")
                }
            }
            item {
                Text("Selected Organisation")
            }

            selectedOrganisations.forEach {
                item {
                    Column {
                        Text(it.name)
                        Text(it.description)
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = searchValue,
                    onValueChange = { onSearchValueChange(it)},
                    label = { Text("Search organisation to add") }
                )
            }

            organisations.forEach {
                item {
                    Row {
                        Column {
                            Text(it.name)
                            Text(it.description)
                        }
                        if(selectedOrganisations.contains(it))
                        {
                            Text("Added")
                        }else{
                            Button(onClick = { onOrganisationSelected(it) }) {
                                Text("Add")
                            }
                        }
                    }
                }
            }
        }
    }

}