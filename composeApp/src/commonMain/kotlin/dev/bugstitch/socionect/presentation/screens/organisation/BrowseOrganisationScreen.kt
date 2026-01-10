package dev.bugstitch.socionect.presentation.screens.organisation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.presentation.components.BrowseOrganisationItem
import dev.bugstitch.socionect.presentation.components.CustomSearchBar

@Composable
fun BrowseOrganisationScreen(
    query: String,
    results: List<Organisation>,
    requestedOrg: List<Organisation>,
    userOrg: List<Organisation>,
    loading: Boolean,
    error: String?,
    onQueryChange: (String) -> Unit,
    onSendRequest: (Organisation) -> Unit,
    isLarge: Boolean
) {
    val baseModifier = if (isLarge) {
        Modifier
            .widthIn(max = 360.dp)
            .fillMaxHeight()
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(top = 2.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
    } else {
        Modifier
            .fillMaxSize()
            .padding(top = 2.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
    }

    Column(
        modifier = baseModifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CustomSearchBar(
            query = query,
            onQueryChange = onQueryChange
        )

        Spacer(Modifier.height(12.dp))

        when {
            loading -> {
                CircularProgressIndicator()
                return
            }

            error != null -> {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
                return
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(results, key = { it.id }) { organisation ->
                BrowseOrganisationItem(
                    organisation = organisation,
                    isJoined = userOrg.contains(organisation),
                    isRequested = requestedOrg.contains(organisation),
                    onSendRequest = { onSendRequest(organisation) }
                )
            }
        }
    }
}

