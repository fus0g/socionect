package dev.bugstitch.socionect.presentation.screens.organisation

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.bugstitch.socionect.domain.models.Coalition
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.OrganisationSubtopic

@Composable
fun OrganisationMainScreen(
    organisation: Organisation,
    subtopics: List<OrganisationSubtopic>,
    coalitions: List<Coalition>,
    onBackClick: () -> Unit,
    onSubtopicPressed: (OrganisationSubtopic) -> Unit,
    onCoalitionPressed: (Coalition) -> Unit,
    onSubtopicCreatePressed: () -> Unit,
    onCreateCoalitionClick: (Organisation) -> Unit,
    onReceivedRequestsClick: (Organisation) -> Unit,
    onInviteUsersClick: (Organisation) -> Unit,
    onCoalitionRequestsClick: (Organisation) -> Unit,
    isLarge: Boolean
) {
    var createMenuExpanded by remember { mutableStateOf(false) }

    val baseModifier = if (isLarge) {
        Modifier
            .widthIn(max = 360.dp)
            .fillMaxHeight()
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    } else {
        Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    }

    Scaffold(
        modifier = baseModifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            OrganisationHeader(
                name = organisation.name,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            BottomActionSection(
                expanded = createMenuExpanded,
                onExpandChange = { createMenuExpanded = it },
                onCreateSubtopic = onSubtopicCreatePressed,
                onCreateCoalition = { onCreateCoalitionClick(organisation) },
                onReceivedRequests = { onReceivedRequestsClick(organisation) },
                onInviteUsers = { onInviteUsersClick(organisation) },
                onCoalitionRequests = { onCoalitionRequestsClick(organisation) }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
        ) {

            item { SectionHeader("Subtopics") }

            items(subtopics, key = { it.id }) { subtopic ->
                SimpleSidebarItem(
                    prefix = "#",
                    title = subtopic.name,
                    subtitle = subtopic.description,
                    onClick = { onSubtopicPressed(subtopic) }
                )
            }

            item {
                Spacer(Modifier.height(16.dp))
                SectionHeader("Coalitions")
            }

            items(coalitions, key = { it.id }) { coalition ->
                SimpleSidebarItem(
                    prefix = "@",
                    title = coalition.name,
                    subtitle = coalition.description,
                    onClick = { onCoalitionPressed(coalition) }
                )
            }

            item { Spacer(Modifier.height(120.dp)) }
        }
    }
}

@Composable
private fun OrganisationHeader(
    name: String,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }

        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text.uppercase(),
        modifier = Modifier.padding(vertical = 8.dp),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun SimpleSidebarItem(
    prefix: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = prefix,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 12.dp)
        )

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
@Composable
private fun BottomActionSection(
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    onCreateSubtopic: () -> Unit,
    onCreateCoalition: () -> Unit,
    onReceivedRequests: () -> Unit,
    onInviteUsers: () -> Unit,
    onCoalitionRequests: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {

        // Create dropdown
        Box {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onExpandChange(true) }
            ) {
                Text("Create")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandChange(false) }
            ) {
                DropdownMenuItem(
                    text = { Text("Create Subtopic") },
                    onClick = {
                        onExpandChange(false)
                        onCreateSubtopic()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Create Coalition") },
                    onClick = {
                        onExpandChange(false)
                        onCreateCoalition()
                    }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onReceivedRequests
        ) {
            Text("Received Requests")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onInviteUsers
        ) {
            Text("Invite Users")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onCoalitionRequests
        ) {
            Text("Coalition Requests")
        }
    }
}
