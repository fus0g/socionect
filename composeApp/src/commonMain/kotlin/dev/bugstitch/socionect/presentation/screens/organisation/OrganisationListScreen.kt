package dev.bugstitch.socionect.presentation.screens.organisation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.presentation.components.OrganisationItem
import dev.bugstitch.socionect.presentation.components.CustomSearchBar

@Composable
fun OrganisationListScreen(
    list: List<Organisation>,
    onItemClick: (Organisation) -> Unit,
    isLarge: Boolean
) {
    var query by remember { mutableStateOf("") }

    val filteredList by remember(list, query) {
        mutableStateOf(
            if (query.isBlank()) list
            else list.filter {
                it.name.contains(query, ignoreCase = true)
            }
        )
    }

    val baseModifier = if (isLarge) {
        Modifier
            .widthIn(max = 360.dp)
            .fillMaxHeight()
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(top = 2.dp, start=16.dp,end=16.dp,bottom = 8.dp)
    } else {
        Modifier
            .fillMaxSize()
            .padding(top = 2.dp, start=8.dp,end=8.dp,bottom = 8.dp)
    }

    Column(
        modifier = baseModifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomSearchBar(
            query = query,
            onQueryChange = { query = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(filteredList, key = { it.id }) { organisation ->
                OrganisationItem(
                    organisation,
                    onItemClick = { onItemClick(organisation) }
                )
            }
        }
    }
}
