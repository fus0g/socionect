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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.presentation.components.NavigationBar
import dev.bugstitch.socionect.presentation.components.icons.SocionectLogo
import dev.bugstitch.socionect.presentation.navigation.CoalitionChatRoom
import dev.bugstitch.socionect.presentation.navigation.CoalitionRequestScreen
import dev.bugstitch.socionect.presentation.navigation.CreateCoalition
import dev.bugstitch.socionect.presentation.navigation.CreateOrganisationSubtopic
import dev.bugstitch.socionect.presentation.navigation.EmptyObj
import dev.bugstitch.socionect.presentation.navigation.FindAndSendRequestToUser
import dev.bugstitch.socionect.presentation.navigation.OrganisationListScreen
import dev.bugstitch.socionect.presentation.navigation.OrganisationMainScreen
import dev.bugstitch.socionect.presentation.navigation.OrganisationReceivedRequests
import dev.bugstitch.socionect.presentation.navigation.SubtopicChatRoom
import dev.bugstitch.socionect.presentation.navigation.UserRequestsScreen
import dev.bugstitch.socionect.presentation.screens.organisation.BrowseOrganisationScreen
import dev.bugstitch.socionect.presentation.screens.organisation.CoalitionRequestsScreen
import dev.bugstitch.socionect.presentation.screens.organisation.CreateCoalitionScreen
import dev.bugstitch.socionect.presentation.screens.organisation.CreateSubTopicScreen
import dev.bugstitch.socionect.presentation.screens.organisation.FindAndSendRequestToUserScreen
import dev.bugstitch.socionect.presentation.screens.organisation.OrganisationListScreen
import dev.bugstitch.socionect.presentation.screens.organisation.OrganisationMainScreen
import dev.bugstitch.socionect.presentation.screens.organisation.OrganisationReceivedRequestScreen
import dev.bugstitch.socionect.presentation.screens.organisation.chat.OrganisationCoalitionChatScreen
import dev.bugstitch.socionect.presentation.screens.organisation.chat.OrganisationSubtopicChatScreen
import dev.bugstitch.socionect.presentation.screens.user.UserRequestsScreen
import dev.bugstitch.socionect.presentation.screens.user.UserScreen
import dev.bugstitch.socionect.presentation.viewmodels.CreateOrganisationScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.organisation.BrowseOrganisationScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.organisation.CoalitionRequestsScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.organisation.CreateCoalitionScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.organisation.CreateOrganisationSubtopicScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.organisation.FindAndSendRequestToUserScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.organisation.OrganisationCoalitionChatViewModel
import dev.bugstitch.socionect.presentation.viewmodels.organisation.OrganisationListScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.organisation.OrganisationMainScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.organisation.OrganisationReceivedRequestScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.organisation.OrganisationSubtopicChatViewModel
import dev.bugstitch.socionect.presentation.viewmodels.user.UserRequestsScreenViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onOrganisationItemClick: (Organisation) -> Unit,
    onOrganisationCreated: () -> Unit,
    isLarge: Boolean,
    navController: NavController
) {

    val page = rememberSaveable{mutableStateOf(0)}
    val pagerState = rememberPagerState(
        initialPage = page.value,
        pageCount = {4}
    )

    val scope = rememberCoroutineScope()

    val lNavController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row(verticalAlignment = Alignment.CenterVertically){
                    SocionectLogo(modifier = Modifier.size(64.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Socionect")
                }
            })
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
            Row(modifier = Modifier.fillMaxSize()
                .padding(bottom = 10.dp)){

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

                val pagerModifier = if(isLarge) Modifier.width(360.dp) else Modifier

                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = !isLarge,
                    modifier = pagerModifier
                ){page->
                    when(page){
                        0 -> {
                            val localNavController = rememberNavController()

                            NavHost(
                                navController = localNavController,
                                startDestination = OrganisationListScreen
                            ){
                                composable<OrganisationListScreen>{backStackEntry->

                                    val organisationListScreenViewModel = koinViewModel<OrganisationListScreenViewModel>()

                                    LaunchedEffect(backStackEntry){
                                        organisationListScreenViewModel.fetchAllOrganisations()
                                    }
                                    val orgListData = organisationListScreenViewModel.organisations.collectAsState()


                                    OrganisationListScreen(
                                        list = orgListData.value.organisations,
                                        onItemClick = {item->
                                            localNavController.navigate(OrganisationMainScreen(
                                                orgId = item.id,
                                                orgName = item.name,
                                                orgDescription = item.description,
                                                orgCreatedAt = item.createdAt
                                            ))
                                        },
                                        isLarge = isLarge
                                    )
                                }

                                composable<OrganisationMainScreen>{ backStackEntry->
                                    val args: OrganisationMainScreen = backStackEntry.toRoute()
                                    val organisation = Organisation(
                                        id = args.orgId,
                                        name = args.orgName,
                                        description = args.orgDescription,
                                        createdAt = args.orgCreatedAt
                                    )
                                    val vm = koinViewModel<OrganisationMainScreenViewModel>(viewModelStoreOwner = backStackEntry)
                                    vm.fetchSubtopics(organisation.id)
                                    vm.fetchCoalitions(organisation.id)
                                    val state by vm.state.collectAsState()



                                    OrganisationMainScreen(
                                        organisation = organisation,
                                        subtopics = state.subtopics,
                                        onSubtopicPressed = {
                                            if(isLarge)
                                            {
                                                lNavController.navigate(
                                                    SubtopicChatRoom(
                                                        subtopicId = it.id,
                                                        subtopicName = it.name
                                                    )
                                                )
                                            }else{
                                                navController.navigate(
                                                    SubtopicChatRoom(
                                                        subtopicId = it.id,
                                                        subtopicName = it.name
                                                    )
                                                )
                                            }
                                        },
                                        onSubtopicCreatePressed = {
                                            if(isLarge)
                                            {
                                                lNavController.navigate(
                                                    CreateOrganisationSubtopic(
                                                        orgId = organisation.id,
                                                        orgName = organisation.name,
                                                        orgDescription = organisation.description,
                                                        orgCreatedAt = organisation.createdAt
                                                    )
                                                )

                                            }else{
                                                navController.navigate(
                                                    CreateOrganisationSubtopic(
                                                        orgId = organisation.id,
                                                        orgName = organisation.name,
                                                        orgDescription = organisation.description,
                                                        orgCreatedAt = organisation.createdAt
                                                    )
                                                )
                                            }
                                        },
                                        onReceivedRequestsClick = {
                                            if(isLarge)
                                            {
                                                lNavController.navigate(
                                                    OrganisationReceivedRequests(
                                                        organisation.id
                                                    )
                                                )
                                            }else{
                                                navController.navigate(
                                                    OrganisationReceivedRequests(
                                                        organisation.id
                                                    )
                                                )
                                            }
                                        },
                                        onInviteUsersClick = {
                                            if(isLarge)
                                            {
                                                lNavController.navigate(
                                                    FindAndSendRequestToUser(
                                                        organisation.id
                                                    )
                                                )
                                            }
                                            else{
                                                navController.navigate(
                                                    FindAndSendRequestToUser(
                                                        organisation.id
                                                    )
                                                )
                                            }
                                        },
                                        coalitions = state.coalitions,
                                        onCreateCoalitionClick = {
                                            if(isLarge)
                                            {
                                                lNavController.navigate(
                                                    CreateCoalition(
                                                        orgId = organisation.id,
                                                        orgName = organisation.name,
                                                        orgDescription = organisation.description,
                                                        orgCreatedAt = organisation.createdAt
                                                    )
                                                )
                                            }else{
                                                navController.navigate(
                                                    CreateCoalition(
                                                        orgId = organisation.id,
                                                        orgName = organisation.name,
                                                        orgDescription = organisation.description,
                                                        orgCreatedAt = organisation.createdAt
                                                    )
                                                )
                                            }
                                        },
                                        onCoalitionRequestsClick = {
                                            if(isLarge)
                                            {
                                                lNavController.navigate(
                                                    CoalitionRequestScreen(
                                                        orgId = organisation.id,
                                                        orgName = organisation.name,
                                                        orgDescription = organisation.description,
                                                        orgCreatedAt = organisation.createdAt
                                                    )
                                                )
                                            }
                                            else {

                                                navController.navigate(
                                                    CoalitionRequestScreen(
                                                        orgId = organisation.id,
                                                        orgName = organisation.name,
                                                        orgDescription = organisation.description,
                                                        orgCreatedAt = organisation.createdAt
                                                    )
                                                )
                                            }
                                        },
                                        onCoalitionPressed = {
                                            if(isLarge)
                                            {
                                                lNavController.navigate(
                                                    CoalitionChatRoom(
                                                        coalitionId = it.id,
                                                        coalitionName = it.name
                                                    )
                                                )
                                            }else{
                                                navController.navigate(
                                                    CoalitionChatRoom(
                                                        coalitionId = it.id,
                                                        coalitionName = it.name)
                                                )
                                            }
                                        },
                                        isLarge = isLarge,
                                        onBackClick = {
                                            localNavController.navigate(OrganisationListScreen)
                                        }
                                    )
                                }
                            }
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
                                onSendRequest = { org -> vm.sendRequestToOrganisation(org.id) },
                                isLarge = isLarge
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
                                },
                                isLarge = isLarge
                            )
                        }

                        3 ->{
                            UserScreen(
                                onLogout = onLogout,
                                navigateToUserRequests = {
                                    if(isLarge)
                                    {
                                        lNavController.navigate(UserRequestsScreen)
                                    }
                                    else{
                                        navController.navigate(UserRequestsScreen)
                                    }
                                },
                                isLarge = isLarge
                            )
                        }
                    }

                }

                Column {

                    NavHost(
                        navController = lNavController,
                        startDestination = EmptyObj
                    ){
                        composable<EmptyObj>{

                        }

                        composable<SubtopicChatRoom> { backStackEntry ->
                            val args: SubtopicChatRoom = backStackEntry.toRoute()

                            val subtopicId = args.subtopicId
                            val subtopicName = args.subtopicName

                            val vm = koinViewModel<OrganisationSubtopicChatViewModel>(viewModelStoreOwner = backStackEntry)

                            val messages by vm.messages.collectAsState()
                            val loading by vm.loading.collectAsState()

                            LaunchedEffect(subtopicName) {
                                vm.loadChat(subtopicId)
                            }

                            OrganisationSubtopicChatScreen(
                                chatTitle = subtopicName,
                                messages = messages,
                                loading = loading,
                                onSend = { text -> vm.sendMessage(text, subtopicId) },
                                onBack = {
                                    vm.disconnect()
                                    lNavController.navigate(EmptyObj)
                                         },
                                isLarge = isLarge
                            )
                        }

                        composable<CoalitionChatRoom> { backStackEntry ->
                            val args: CoalitionChatRoom = backStackEntry.toRoute()

                            val coalitionId = args.coalitionId
                            val coalitionName = args.coalitionName

                            val vm = koinViewModel<OrganisationCoalitionChatViewModel>(viewModelStoreOwner = backStackEntry)

                            val messages by vm.messages.collectAsState()
                            val loading by vm.loading.collectAsState()

                            LaunchedEffect(coalitionName) {
                                vm.loadChat(coalitionId)
                            }

                            OrganisationCoalitionChatScreen(
                                chatTitle = coalitionName,
                                messages = messages,
                                loading = loading,
                                onSend = { text -> vm.sendMessage(text, coalitionId) },
                                onBack = {
                                    vm.disconnect()
                                    lNavController.navigate(EmptyObj)
                                         },
                                isLarge = isLarge
                            )
                        }

                        composable<CreateOrganisationSubtopic> { backStackEntry ->

                            val args: OrganisationMainScreen = backStackEntry.toRoute()
                            val organisation = Organisation(
                                id = args.orgId,
                                name = args.orgName,
                                description = args.orgDescription,
                                createdAt = args.orgCreatedAt
                            )

                            val vm = koinViewModel<CreateOrganisationSubtopicScreenViewModel>(viewModelStoreOwner = backStackEntry)

                            LaunchedEffect(vm.finished.value){
                                if(vm.finished.value){
                                    lNavController.navigate(EmptyObj){
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            }

                            CreateSubTopicScreen(
                                subtopicName = vm.subtopicName.value,
                                subtopicDescription = vm.subtopicDescription.value,
                                error = vm.error.value,
                                onSubtopicNameChange = {
                                    vm.setSubtopicName(it)
                                },
                                onSubtopicDescriptionChange = {
                                    vm.setSubtopicDescription(it)
                                },
                                onSubtopicCreate = {
                                    vm.createSubtopic(organisation.id)
                                },
                                isLarge = isLarge
                            )

                        }

                        composable<CreateCoalition> {
                            val args: CreateCoalition = it.toRoute()
                            val vm = koinViewModel<CreateCoalitionScreenViewModel>(viewModelStoreOwner = it)
                            val orgs = vm.results.collectAsState()

                            LaunchedEffect(vm.created.value){
                                if(vm.created.value){
                                    lNavController.navigate(EmptyObj) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            }

                            CreateCoalitionScreen(
                                name = vm.name.value,
                                description = vm.description.value,
                                onNameChange = {n-> vm.setName(n) },
                                onDescriptionChange = {d-> vm.setDescription(d) },
                                searchValue = vm.query.value,
                                onSearchValueChange = {q-> vm.setQuery(q) },
                                organisations = orgs.value,
                                onOrganisationSelected = {org-> vm.addOrganisation(org) },
                                selectedOrganisations = vm.addedOrganisations,
                                onCreateClick = { vm.createCoalition(args.orgId) },
                                isLarge = isLarge
                            )
                        }

                        composable<FindAndSendRequestToUser> {
                            val args: FindAndSendRequestToUser = it.toRoute()
                            val vm = koinViewModel<FindAndSendRequestToUserScreenViewModel>(viewModelStoreOwner = it)
                            val state = vm.state.collectAsState()

                            LaunchedEffect(Unit){
                                vm.getMemberUsers(args.organisationId)
                                vm.getRequestedUsers(args.organisationId)
                            }

                            FindAndSendRequestToUserScreen(
                                query = vm.query.value,
                                results = state.value.searchedUsers,
                                requestedUsers = state.value.requestedUsers,
                                memberUsers = state.value.memberUsers,
                                loading = vm.loading.value,
                                error = vm.error.value,
                                onQueryChange = {usr-> vm.setQuery(usr) },
                                onRequestClick = {  usr-> vm.sendRequestToUser(usr.id,args.organisationId) },
                                isLarge = isLarge
                            )
                        }

                        composable<OrganisationReceivedRequests>{
                            val args: OrganisationReceivedRequests = it.toRoute()
                            val vm = koinViewModel<OrganisationReceivedRequestScreenViewModel>(viewModelStoreOwner = it)
                            val scope = rememberCoroutineScope()
                            scope.launch {
                                vm.getRequests(args.orgId)
                            }
                            val state = vm.state.collectAsState()

                            OrganisationReceivedRequestScreen(
                                list = state.value.users,
                                onAccept = {usr->
                                    vm.acceptRequest(usr.id, args.orgId)
                                },
                                onDecline = {usr->
                                    vm.declineRequest(usr.id, args.orgId)
                                },
                                isLarge = isLarge
                            )
                        }

                        composable<CoalitionRequestScreen> {
                            val args: CreateCoalition = it.toRoute()
                            val vm = koinViewModel<CoalitionRequestsScreenViewModel>(viewModelStoreOwner = it)
                            val org = Organisation(
                                id = args.orgId,
                                name = args.orgName,
                                description = args.orgDescription,
                                createdAt = args.orgCreatedAt
                            )
                            LaunchedEffect(org.id) {
                                vm.loadRequests(org)
                            }
                            val list = vm.list.collectAsState()
                            CoalitionRequestsScreen(
                                list = list.value,
                                onAccept = { r->
                                    vm.acceptRequest(r,org)
                                },
                                onDecline = { r->
                                    vm.declineRequest(r,org)
                                },
                                isLarge = isLarge
                            )
                        }

                        composable<UserRequestsScreen> {
                            val vm = koinViewModel<UserRequestsScreenViewModel>()
                            LaunchedEffect(Unit) {
                                vm.getSentRequests()
                                vm.getReceivedRequests()
                            }

                            val state = vm.state.collectAsState()

                            UserRequestsScreen(
                                sentRequests = state.value.sentRequests,
                                receivedRequests = state.value.receivedRequests,
                                onAccept = {
                                    vm.confirmRequest(it)
                                },
                                onDecline = {
                                    vm.declineRequest(it)
                                },
                                isLarge = isLarge
                            )
                        }
                    }

                }
            }


        }
    }
}
