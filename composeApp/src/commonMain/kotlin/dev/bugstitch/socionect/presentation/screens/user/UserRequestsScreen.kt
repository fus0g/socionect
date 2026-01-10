package dev.bugstitch.socionect.presentation.screens.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.presentation.components.RequestsTabBar
import kotlinx.coroutines.launch

@Composable
fun UserRequestsScreen(
    receivedRequests: List<Organisation>,
    sentRequests: List<Organisation>,
    onAccept: (Organisation) -> Unit,
    onDecline: (Organisation) -> Unit,
    isLarge: Boolean
) {
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {

        RequestsTabBar(
            selectedIndex = pagerState.currentPage,
            onSelect = { index ->
                scope.launch { pagerState.animateScrollToPage(index) }
            }
        )

        Spacer(Modifier.height(12.dp))

        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> UserReceivedRequestScreen(
                    list = receivedRequests,
                    onAccept = onAccept,
                    onDecline = onDecline,
                    isLarge = isLarge
                )

                1 -> UserSentRequestScreen(
                    list = sentRequests,
                    isLarge = isLarge
                )
            }
        }
    }
}
