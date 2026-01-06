package dev.bugstitch.socionect

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.bugstitch.socionect.data.models.toOrganisation
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.toOrganisationDTO
import dev.bugstitch.socionect.presentation.navigation.*
import dev.bugstitch.socionect.presentation.screens.*
import dev.bugstitch.socionect.presentation.screens.organisation.BrowseOrganisationScreen
import dev.bugstitch.socionect.presentation.screens.organisation.CreateSubTopicScreen
import dev.bugstitch.socionect.presentation.screens.organisation.FindAndSendRequestToUserScreen
import dev.bugstitch.socionect.presentation.screens.organisation.OrganisationMainScreen
import dev.bugstitch.socionect.presentation.screens.organisation.OrganisationReceivedRequestScreen
import dev.bugstitch.socionect.presentation.screens.user.UserRequestsScreen
import dev.bugstitch.socionect.presentation.viewmodels.*
import dev.bugstitch.socionect.presentation.viewmodels.organisation.BrowseOrganisationScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.organisation.CreateOrganisationSubtopicScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.organisation.FindAndSendRequestToUserScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.organisation.OrganisationListScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.organisation.OrganisationMainScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.organisation.OrganisationReceivedRequestScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.user.UserRequestsScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()

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
                        navController.navigate(Login) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    checking = checking,
                    autoLoginSuccess = autoLoginSuccess
                )
            }

            composable<Login> {
                val loginViewModel = koinViewModel<LoginScreenViewModel>(viewModelStoreOwner = it)


                LaunchedEffect(loginViewModel.loginSuccess.value) {
                    if (loginViewModel.loginSuccess.value) {
                        navController.navigate(Home) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }

                LoginScreen(
                    email = loginViewModel.email.value,
                    password = loginViewModel.password.value,
                    loading = loginViewModel.loading.value,
                    errorMessage = loginViewModel.errorMessage.value,
                    onEmailChange = { loginViewModel.setEmail(it) },
                    onPasswordChange = { loginViewModel.setPassword(it) },
                    onSignInClick = { loginViewModel.login() },
                    onSignUpClick = {
                        navController.navigate(Signup)
                    }
                )
            }


            composable<Signup> {
                val viewModel = koinViewModel<SignUpScreenViewModel>(viewModelStoreOwner = it)

                LaunchedEffect(viewModel.signupSuccess.value) {
                    if (viewModel.signupSuccess.value) {
                        navController.navigate(Home) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }

                SignUpScreen(
                    name = viewModel.name.value,
                    username = viewModel.username.value,
                    email = viewModel.email.value,
                    password = viewModel.password.value,
                    usernameAvailable = viewModel.usernameAvailable.value,
                    emailAvailable = viewModel.emailAvailable.value,
                    loading = viewModel.loading.value,
                    errorMessage = viewModel.errorMessage.value,
                    onNameChange = { viewModel.setName(it) },
                    onUsernameChange = { viewModel.setUsername(it) },
                    onEmailChange = { viewModel.setEmail(it) },
                    onPasswordChange = { viewModel.setPassword(it) },
                    onSignUpClick = { viewModel.signUp() },
                    onBackToLoginClick = {
                        navController.navigate(Login) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable<Home> {
                val homeScreenViewModel = koinViewModel<HomeScreenViewModel>(viewModelStoreOwner = it)
                val organisationListScreenViewModel = koinViewModel<OrganisationListScreenViewModel>(viewModelStoreOwner = it)

                val orgListData = organisationListScreenViewModel.organisations.collectAsState()

                HomeScreen(
                    onLogout = {
                        homeScreenViewModel.logout()
                        navController.navigate(Login) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    navigateDiscover = {
                        navController.navigate(Discover)
                    },
                    navigateCreateOrg = {
                        navController.navigate(CreateOrganisation)
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
                    navigateDiscoverOrganisations = {
                        navController.navigate(DiscoverOrganisations)
                    },
                    navigateToUserRequests = {
                        navController.navigate(UserRequestsScreen)
                    }
                )
            }

            composable<Discover> {
                val vm = koinViewModel<UserSearchViewModel>()

                val query by vm.query.collectAsState()
                val results by vm.results.collectAsState()
                val loading by vm.loading.collectAsState()
                val error by vm.error.collectAsState()

                UserSearchScreen(
                    query = query,
                    results = results,
                    loading = loading,
                    error = error,
                    onQueryChange = { vm.setQuery(it) },
                    onUserClick = { user ->
                        navController.navigate(
                            ChatRoom(
                                otherUserId = user.id,
                                otherUserName = user.username
                            )
                        )
                    }
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

            composable<CreateOrganisation> {
                val vm = koinViewModel<CreateOrganisationScreenViewModel>(viewModelStoreOwner = it)

                LaunchedEffect(vm.created.value){
                    if(vm.created.value){
                        navController.navigate(Home) {
                            popUpTo(0) { inclusive = true }
                        }
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
                val state by vm.state.collectAsState()



                OrganisationMainScreen(
                    organisation = organisation,
                    subtopics = state.subtopics,
                    onSubtopicPressed = {},
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
                    }
                )

            }

            composable<DiscoverOrganisations> {
                val vm = koinViewModel<BrowseOrganisationScreenViewModel>(viewModelStoreOwner = it)
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
                    }
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
                    onRequestClick = {  usr-> vm.sendRequestToUser(usr.id,args.organisationId) }
                )
            }

        }
    }
}
