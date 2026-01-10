package dev.bugstitch.socionect

import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.window.core.layout.WindowSizeClass
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.presentation.navigation.*
import dev.bugstitch.socionect.presentation.screens.*
import dev.bugstitch.socionect.presentation.screens.common.BaseSignupLoginScreen
import dev.bugstitch.socionect.presentation.screens.organisation.CoalitionRequestsScreen
import dev.bugstitch.socionect.presentation.screens.organisation.CreateCoalitionScreen
import dev.bugstitch.socionect.presentation.screens.organisation.CreateSubTopicScreen
import dev.bugstitch.socionect.presentation.screens.organisation.FindAndSendRequestToUserScreen
import dev.bugstitch.socionect.presentation.screens.organisation.OrganisationMainScreen
import dev.bugstitch.socionect.presentation.screens.organisation.OrganisationReceivedRequestScreen
import dev.bugstitch.socionect.presentation.screens.organisation.chat.OrganisationCoalitionChatScreen
import dev.bugstitch.socionect.presentation.screens.organisation.chat.OrganisationSubtopicChatScreen
import dev.bugstitch.socionect.presentation.screens.user.UserRequestsScreen
import dev.bugstitch.socionect.presentation.theme.CustomColors
import dev.bugstitch.socionect.presentation.viewmodels.*
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
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme(colorScheme = CustomColors.LightColorScheme) {
        val navController = rememberNavController()

        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

        val isLarge = windowSizeClass.isAtLeastBreakpoint(
            widthDpBreakpoint = WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND,
            heightDpBreakpoint = WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
        )

        NavHost(navController = navController, startDestination = Landing) {


            composable<Landing> {
                val viewModel: LandingScreenViewModel = koinViewModel(viewModelStoreOwner = it)

                val checking by viewModel.isChecking.collectAsState()
                val autoLoginSuccess by viewModel.autoLoginSuccess.collectAsState()

                LandingScreen(
                    navigateHome = {
                        navController.navigate(Home) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    navigateLogin = {
                        navController.navigate(BaseSignupLogin) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    checking = checking,
                    autoLoginSuccess = autoLoginSuccess
                )
            }

            composable<BaseSignupLogin> {

                BaseSignupLoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Home) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onSignupSuccess = {
                        navController.navigate(Home) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    isLarge = isLarge
                )

            }

            composable<Home> {
                val homeScreenViewModel = koinViewModel<HomeScreenViewModel>(viewModelStoreOwner = it)
                val organisationListScreenViewModel = koinViewModel<OrganisationListScreenViewModel>(viewModelStoreOwner = it)

                val orgListData = organisationListScreenViewModel.organisations.collectAsState()

                HomeScreen(
                    onLogout = {
                        homeScreenViewModel.logout()
                        navController.navigate(BaseSignupLogin) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    organisationList = orgListData.value.organisations,
                    onOrganisationItemClick = { org ->
                        navController.navigate(OrganisationMainScreen(
                            orgId = org.id,
                            orgName = org.name,
                            orgDescription = org.description,
                            orgCreatedAt = org.createdAt
                        ))
                    },
                    onOrganisationCreated = {
                        navController.navigate(Home) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    isLarge = isLarge,
                    navController = navController
                )
            }

            composable<ChatRoom> { backStackEntry ->
                val args: ChatRoom = backStackEntry.toRoute()

                val otherUserId = args.otherUserId
                val otherUserName = args.otherUserName

                val vm = koinViewModel<ChatRoomViewModel>(viewModelStoreOwner = backStackEntry)

                val messages by vm.messages.collectAsState()
                val loading by vm.loading.collectAsState()

                LaunchedEffect(otherUserId) {
                    vm.loadChat(otherUserId)
                }

                ChatRoomScreen(
                    chatTitle = otherUserName,
                    messages = messages,
                    loading = loading,
                    onSend = { text -> vm.sendMessage(text, otherUserId) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable<OrganisationMainScreen> { backStackEntry ->

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
                        navController.navigate(SubtopicChatRoom(
                            subtopicId = it.id,
                            subtopicName = it.name
                        ))
                    },
                    onSubtopicCreatePressed = {
                        navController.navigate(CreateOrganisationSubtopic(
                            orgId = organisation.id,
                            orgName = organisation.name,
                            orgDescription = organisation.description,
                            orgCreatedAt = organisation.createdAt
                        ))
                    },
                    onReceivedRequestsClick = {
                        navController.navigate(OrganisationReceivedRequests(organisation.id))
                    },
                    onInviteUsersClick = {
                        navController.navigate(FindAndSendRequestToUser(organisation.id))
                    },
                    coalitions = state.coalitions,
                    onCreateCoalitionClick = {
                        navController.navigate(CreateCoalition(
                            orgId = organisation.id,
                            orgName = organisation.name,
                            orgDescription = organisation.description,
                            orgCreatedAt = organisation.createdAt
                        ))
                    },
                    onCoalitionRequestsClick = {
                        navController.navigate(CoalitionRequestScreen(
                            orgId = organisation.id,
                            orgName = organisation.name,
                            orgDescription = organisation.description,
                            orgCreatedAt = organisation.createdAt
                        ))
                    },
                    onCoalitionPressed = {
                        navController.navigate(CoalitionChatRoom(
                            coalitionId = it.id,
                            coalitionName = it.name
                        ))
                    },
                    isLarge = isLarge,
                    onBackClick = {

                    }
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
                        navController.navigate(OrganisationMainScreen(
                            orgId = organisation.id,
                            orgName = organisation.name,
                            orgDescription = organisation.description,
                            orgCreatedAt = organisation.createdAt
                        )){
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

            composable<CreateCoalition> {
                val args: CreateCoalition = it.toRoute()
                val vm = koinViewModel<CreateCoalitionScreenViewModel>(viewModelStoreOwner = it)
                val orgs = vm.results.collectAsState()

                LaunchedEffect(vm.created.value){
                    if(vm.created.value){
                        navController.navigate(OrganisationMainScreen(
                            orgId = args.orgId,
                            orgName = args.orgName,
                            orgDescription = args.orgDescription,
                            orgCreatedAt = args.orgCreatedAt
                        )) {
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

            composable<CoalitionRequestScreen> {
                val args: CreateCoalition = it.toRoute()
                val vm = koinViewModel<CoalitionRequestsScreenViewModel>(viewModelStoreOwner = it)
                val org = Organisation(
                    id = args.orgId,
                    name = args.orgName,
                    description = args.orgDescription,
                    createdAt = args.orgCreatedAt
                )
                LaunchedEffect(Unit){
                    vm.getRequests(org)
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
                        navController.popBackStack()
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
                        navController.popBackStack()
                             },
                    isLarge = isLarge
                )
            }

        }
    }
}
