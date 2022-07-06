package com.imtmobileapps.view.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.imtmobileapps.R
import com.imtmobileapps.components.LoginCard
import com.imtmobileapps.model.Credentials
import com.imtmobileapps.ui.theme.topAppBarBackgroundColor
import com.imtmobileapps.ui.theme.topAppBarContentColor
import com.imtmobileapps.util.*
import com.imtmobileapps.util.Constants.LOGIN_SCREEN_TAG
import com.imtmobileapps.view.portfoliolist.PortfolioListViewModel
import logcat.asLog
import logcat.logcat
import java.io.FileNotFoundException

@Composable
fun LoginScreen(
    viewModel: PortfolioListViewModel,
    navController: NavController,
) {

    BackHandler {
        // blocks system back button
    }

    val usernameText = rememberSaveable {
        mutableStateOf("")
    }

    val passwordText = rememberSaveable {
        mutableStateOf("")
    }

    val checked = rememberSaveable {
        mutableStateOf(false)
    }
    val scaffoldState =
        rememberScaffoldState() // This is here in case we want to display a snackbar

    val context = LocalContext.current

    val credentials = remember {
        mutableStateOf(Credentials(username = "", password = ""))
    }

    val isLoggedIn = viewModel.isLoggedIn.collectAsState()

    LaunchedEffect(key1 = isLoggedIn.value, block = {
        when (isLoggedIn.value) {
            is RequestState.Loading -> {
                logcat(LOGIN_SCREEN_TAG) { "RequestState.Loading is : ${isLoggedIn.value}" }

            }
            is RequestState.Success -> {
                logcat(LOGIN_SCREEN_TAG) { "RequestState.Success is : ${isLoggedIn.value}" }
                try {
                    writeUsernameAndPassword(context = context,
                        usernameText.value,
                        passwordText.value)
                    logcat(LOGIN_SCREEN_TAG) { "Write file success!" }

                } catch (e: Exception) {
                    // notify user that the file already exists
                    logcat(LOGIN_SCREEN_TAG) { "Problem writing file ${e.localizedMessage as String}" }
                }
                navController.navigate(Routes.PORTFOLIO_LIST)
            }

            is RequestState.LoggedOut -> {
                logcat(LOGIN_SCREEN_TAG) { "RequestState.LoggedOut TODO - cleanup : ${isLoggedIn.value}" }
                // Could delete sensitive file here, but only
            }

            is RequestState.Error -> {
                logcat(LOGIN_SCREEN_TAG) { "RequestState.Error is : ${isLoggedIn.value}" }
                // show snack bar to user
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = "Login failed with those credentials. Please retry.",
                    actionLabel = "Retry",
                    SnackbarDuration.Long
                )
                logcat(LOGIN_SCREEN_TAG) { "result is : ${result.name}" }
                if (result == SnackbarResult.ActionPerformed) {
                    logcat(LOGIN_SCREEN_TAG) { "THEY clicked retry." }
                    // clear text fields
                    usernameText.value = ""
                    passwordText.value = ""

                }
            }
            else -> {}
        }

    })

    // Check if the user has the credentials cached, if so, log them in
    LaunchedEffect(key1 = credentials.value, block = {
        try {
            // READ
            val auth = readUsernameAndPassword(context = context)
            val test = auth.split(":")
            credentials.value.username = test[0]
            credentials.value.password = test[1]
            logcat(LOGIN_SCREEN_TAG) { "LaunchedEffect SPLIT is  : ${test[0]} ${test[1]}" }

            // go ahead and log them in
            logcat(LOGIN_SCREEN_TAG) {
                "calling viewModel.LOGIN from  LaunchedEffect : ${credentials.value.username} ${credentials.value.password}"
            }
            viewModel.login(credentials.value.username, credentials.value.password)
            navController.navigate(Routes.PORTFOLIO_LIST)


        } catch (e: FileNotFoundException) {
            logcat(LOGIN_SCREEN_TAG) { "FileNotFoundException ${e.localizedMessage as String}" }
        } catch (e: Exception) {
            logcat(LOGIN_SCREEN_TAG) { "User has not requested remember me: ${e.localizedMessage as String}" }
        }
    })

    Scaffold(scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.login),
                        color = MaterialTheme.colors.topAppBarContentColor
                    )
                },
                backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
            )
        },
        content = {
            it.calculateTopPadding()
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (isLoggedIn.value) {
                    is RequestState.Loading -> {
                        CircularProgressIndicator(
                            color = MaterialTheme.colors.primary,
                        )
                    }

                    else -> {
                        LoginCard(usernameText = usernameText.value,
                            passwordText = passwordText.value,
                            onUsernameChanged = { username ->
                                usernameText.value = username
                            },
                            onPasswordChanged = { password ->
                                passwordText.value = password
                            },
                            onDone = {
                                logcat(LOGIN_SCREEN_TAG) { "onDone" }
                                viewModel.login(usernameText.value, passwordText.value)
                            },
                            onSignInClicked = {
                                // if file exists, get the uname and password from it, then login
                                if (credentials.value.username.isNotEmpty() && credentials.value.password.isNotEmpty()) {
                                    logcat(LOGIN_SCREEN_TAG) {
                                        "calling viewModel.LOGIN with FILE values : ${credentials.value.username} ${credentials.value.password}"
                                    }
                                    viewModel.login(credentials.value.username,
                                        credentials.value.password)
                                } else {
                                    // else get the login values from the text fields
                                    logcat(LOGIN_SCREEN_TAG) { "calling viewModel.LOGIN with INPUT TEXT FIELD values ${usernameText.value} ${passwordText.value}" }
                                    viewModel.login(usernameText.value, passwordText.value)
                                }

                            },
                            onForgotPasswordClicked = {
                                logcat(LOGIN_SCREEN_TAG) { "onForgotPasswordClicked" }

                            },
                            onCreateAccountClicked = {
                                logcat(LOGIN_SCREEN_TAG) { "onCreateAccountClicked" }
                                navController.navigate(Routes.SIGN_UP_SCREEN)

                            },

                            onRememberMeChecked = { remember ->
                                checked.value = remember
                                if (!checked.value) {
                                    // delete the file
                                    try {
                                        deleteSensitiveFile(context = context)
                                    } catch (e: Exception) {
                                        logcat(LOGIN_SCREEN_TAG) { e.asLog() }
                                    }
                                }
                            },
                            checked = checked.value
                        )// end card
                    }
                }
            }// end Column
        }
    )// end Scaffold

}