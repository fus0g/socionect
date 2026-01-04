package dev.bugstitch.socionect.presentation.screens.organisation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.bugstitch.socionect.domain.models.Organisation

@Composable
fun OrganisationListScreen(
    list: List<Organisation>,
    onItemClick: (Organisation) -> Unit
){
    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()
    ) {

        LazyColumn {
            list.forEach {
                item {
                    Column(modifier = Modifier.clickable(
                        onClick = {onItemClick(it)}
                    )) {
                        Text(it.name)
                        Text(it.description)
                    }
                }
            }
        }

    }
}
