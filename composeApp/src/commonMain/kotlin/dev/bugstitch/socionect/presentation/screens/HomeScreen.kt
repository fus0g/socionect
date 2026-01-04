package dev.bugstitch.socionect.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.presentation.screens.organisation.OrganisationListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    navigateDiscover: () -> Unit,
    navigateCreateOrg: () -> Unit,
    organisationList: List<Organisation>,
    onOrganisationItemClick: (Organisation) -> Unit
) {

    val page = rememberSaveable{mutableStateOf(0)}
    val pagerState = rememberPagerState(
        initialPage = page.value,
        pageCount = {2}
    )

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
            Column {

                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = true
                ){page->
                    when(page){
                        0 -> {
                            OrganisationListScreen(
                                list = organisationList,
                                onItemClick = onOrganisationItemClick
                            )
                        }

                        1 ->{
                            Column(modifier = Modifier.fillMaxSize()) {
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
                                Button(
                                    onClick = {navigateCreateOrg()},
                                ){
                                    Text("Create Organisation")
                                }
                            }
                        }
                    }

                }
            }


        }
    }
}
