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
import dev.bugstitch.socionect.presentation.components.NavigationBar
import dev.bugstitch.socionect.presentation.navigation.Home
import dev.bugstitch.socionect.presentation.screens.organisation.BrowseOrganisationScreen
import dev.bugstitch.socionect.presentation.screens.organisation.OrganisationListScreen
import dev.bugstitch.socionect.presentation.viewmodels.CreateOrganisationScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.organisation.BrowseOrganisationScreenViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    navigateDiscover: () -> Unit,
    navigateCreateOrg: () -> Unit,
    organisationList: List<Organisation>,
    onOrganisationItemClick: (Organisation) -> Unit,
    navigateDiscoverOrganisations: () -> Unit,
    navigateToUserRequests:()->Unit,
    onOrganisationCreated: () -> Unit,
    isLarge: Boolean
) {

    val page = rememberSaveable{mutableStateOf(0)}
    val pagerState = rememberPagerState(
        initialPage = page.value,
        pageCount = {4}
    )

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Home") })
        },
        bottomBar = {
            if(!isLarge){
                NavigationBar(
                    onChatClick = {
                        scope.launch {
                            pagerState.scrollToPage(0)
                        }
                    },
                    onCreateClick = {
                        scope.launch {
                            pagerState.scrollToPage(2)
                        }

                    },
                    onSearchClick = {
                        scope.launch {
                            pagerState.scrollToPage(1)
                        }
                    },
                    onProfileClick = {
                        scope.launch {
                            pagerState.scrollToPage(3)
                        }
                    },
                    isLarge = isLarge,
                    page = pagerState.currentPage
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Row {

                if(isLarge){
                    NavigationBar(
                        onChatClick = {
                            scope.launch {
                                pagerState.scrollToPage(0)
                            }
                        },
                        onCreateClick = {
                            scope.launch {
                                pagerState.scrollToPage(2)
                            }

                        },
                        onSearchClick = {
                            scope.launch {
                                pagerState.scrollToPage(1)
                            }
                        },
                        onProfileClick = {
                            scope.launch {
                                pagerState.scrollToPage(3)
                            }
                        },
                        isLarge = isLarge,
                        page = pagerState.currentPage
                    )
                }

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

                            val vm = koinViewModel<BrowseOrganisationScreenViewModel>()
                            val list = vm.results.collectAsState()
                            val state = vm.state.collectAsState()

                            BrowseOrganisationScreen(
                                query = vm.query.value,
                                results = list.value,
                                requestedOrg = state.value.usersRequestedOrganisations,
                                userOrg = state.value.usersCurrentOrganisation,
                                loading = vm.loading.value,
                                error = vm.error.value,
                                onQueryChange = { text -> vm.setQuery(text) },
                                onSendRequest = { org -> vm.sendRequestToOrganisation(org.id) }
                            )

                        }
                        2 ->{
                            val vm = koinViewModel<CreateOrganisationScreenViewModel>()

                            LaunchedEffect(vm.created.value){
                                if(vm.created.value){
                                    onOrganisationCreated()
                                }
                            }

                            CreateOrganisationScreen(
                                organisationName = vm.orgName.value,
                                organisationDescription = vm.orgDescription.value,
                                error = vm.error.value,
                                onOrganisationNameChange = { n->
                                    vm.setOrgName(n)
                                },
                                onOrganisationDescriptionChange = { d->
                                    vm.setOrgDescription(d)
                                },
                                onOrganisationCreate = {
                                    vm.create()
                                }
                            )
                        }

                        3 ->{
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
                                    onClick = {navigateDiscoverOrganisations()},
                                ){
                                    Text("Browse Organisation")
                                }
                                Button(
                                    onClick = {navigateToUserRequests()},
                                ){
                                    Text("Requests")
                                }
                            }
                        }
                    }

                }
            }


        }
    }
}
