package dev.bugstitch.socionect

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

import socionect.composeapp.generated.resources.Res
import socionect.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        val scope = rememberCoroutineScope()
        val client: HttpClient = koinInject()
        var showContent by remember { mutableStateOf(false) }
        var responseText by remember { mutableStateOf("Click the button to fetch data") }

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    showContent = !showContent
                    if (showContent) {
                        scope.launch {
                            try {
                                val response: HttpResponse =
                                    client.get("https://api.restful-api.dev/objects")
                                responseText = response.bodyAsText()
                            } catch (e: Exception) {
                                responseText = "Error: ${e.message}"
                            }
                        }
                    }
                }
            ) {
                Text("Fetch API Data")
            }

            Spacer(Modifier.height(16.dp))

            AnimatedVisibility(showContent) {
                Text(
                    text = responseText,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}