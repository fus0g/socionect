package dev.bugstitch.socionect.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    navigateDiscover: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Home") })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    onLogout()
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Logout")
            }

            Button(
                onClick = { navigateDiscover() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Discover Users")
            }
        }
    }
}
