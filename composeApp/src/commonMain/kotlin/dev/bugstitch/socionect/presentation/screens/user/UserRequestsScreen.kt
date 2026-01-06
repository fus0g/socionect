package dev.bugstitch.socionect.presentation.screens.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import dev.bugstitch.socionect.domain.models.Organisation
import kotlinx.coroutines.launch

@Composable
fun UserRequestsScreen(
    receivedRequests: List<Organisation>,
    sentRequests: List<Organisation>,
    onAccept: (Organisation) -> Unit,
    onDecline: (Organisation) -> Unit
){

    val pagerState = rememberPagerState { 2 }

    val scope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()) {

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {

            when(pagerState.currentPage){
                0 -> {
                    Button(onClick = { }) {
                        Text("Received")
                    }
                }
                1 -> {
                    TextButton(onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    }) {
                        Text("Received")
                    }
                }
            }

            when(pagerState.currentPage){
                1 -> {
                    Button(onClick = { }) {
                        Text("Sent")
                    }
                }
                0 -> {
                    TextButton(onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }) {
                        Text("Sent")
                    }
                }
            }

        }

        HorizontalPager(state = pagerState,
            userScrollEnabled = true){
            when(it){
                0 -> UserReceivedRequestScreen(
                    list = receivedRequests,
                    onAccept = onAccept,
                    onDecline = onDecline
                )
                1 -> UserSentRequestScreen(
                    list = sentRequests
                )
            }
        }
    }

}