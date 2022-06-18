package com.imtmobileapps.view.login

import androidx.activity.compose.BackHandler
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.imtmobileapps.R
import com.imtmobileapps.components.LoginCard
import com.imtmobileapps.model.Credentials
import com.imtmobileapps.ui.theme.topAppBarBackgroundColor
import com.imtmobileapps.ui.theme.topAppBarContentColor
import com.imtmobileapps.util.Constants.LOGIN_SCREEN_TAG
import com.imtmobileapps.util.Routes
import com.imtmobileapps.util.deleteSensitiveFile
import com.imtmobileapps.util.readUsernameAndPassword
import com.imtmobileapps.util.writeUsernameAndPassword
import com.imtmobileapps.view.portfoliolist.PortfolioListViewModel
import kotlinx.coroutines.launch
import logcat.asLog
import logcat.logcat
import java.io.FileNotFoundException

@Composable
fun LoginScreen(
    viewModel: PortfolioListViewModel,
    navController: NavController,
) {

    BackHandler {
        navController.popBackStack()
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

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val credentials = remember {
        mutableStateOf(Credentials(username = "", password = ""))
    }

    // Check if the user has the credentials cached, if so, log them in
    LaunchedEffect(key1 = credentials.value) {
        try {
            // READ
            val auth = readUsernameAndPassword(context = context)
            logcat(LOGIN_SCREEN_TAG) { "LaunchedEffect AUTH from SANDBOX is : ${auth}" }
            val test = auth.split(":")
            credentials.value.username = test[0]
            credentials.value.password = test[1]
            logcat(LOGIN_SCREEN_TAG) { "LaunchedEffect SPLIT is  : ${test[0]} ${test[1]}" }
            logcat(LOGIN_SCREEN_TAG) { "LaunchedEffect Credentials object is  : ${credentials.value}" }
            // go ahead and log them in
            logcat(LOGIN_SCREEN_TAG) {
                "calling viewModel.LOGIN from  LaunchedEffect : ${credentials.value.username} ${credentials.value.password}"
            }
            viewModel.login(credentials.value.username, credentials.value.password)

            navController.navigate(Routes.PORTFOLIO_LIST) {
                popUpTo(Routes.LOGIN_SCREEN) { inclusive = true }
            }
        } catch (e: FileNotFoundException) {
            logcat(LOGIN_SCREEN_TAG) { "FileNotFoundException ${e.localizedMessage as String}" }
        } catch (e: Exception) {
            logcat(LOGIN_SCREEN_TAG) { "READ PROBLEM ${e.localizedMessage as String}" }
        }
    }

    fun goToList() {
        navController.navigate(Routes.PORTFOLIO_LIST) {
            popUpTo(Routes.LOGIN_SCREEN) { inclusive = true }
        }
    }

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
            LoginCard(usernameText = usernameText.value,
                passwordText = passwordText.value,
                onUsernameChanged = { username ->
                    usernameText.value = username
                },
                onPasswordChanged = { password ->
                    passwordText.value = password
                },
                onDone = {
                    // viewModel.login(usernameText.value, passwordText.value)
                    logcat(LOGIN_SCREEN_TAG) { "onDone" }

                },
                onSignInClicked = {
                    logcat(LOGIN_SCREEN_TAG) { "onSignInClicked" }
                    // if file exists, get the uname and password from it, then login
                    logcat(LOGIN_SCREEN_TAG) { "OnSignInClicked and CREDENTIALS are : ${credentials.value}" }
                    if (credentials.value.username.isNotEmpty() && credentials.value.password.isNotEmpty()) {
                        logcat(LOGIN_SCREEN_TAG) {
                            "calling viewModel.LOGIN with FILE values : ${credentials.value.username} ${credentials.value.password}"
                        }
                        viewModel.login(credentials.value.username, credentials.value.password)

                        goToList()
                    } else {
                        // else get the login values from the text fields
                        logcat(LOGIN_SCREEN_TAG) { "calling viewModel.LOGIN with INPUT TEXT FIELD values ${usernameText.value} ${passwordText.value}" }
                        viewModel.login(usernameText.value, passwordText.value)
                        goToList()
                    }

                },
                onForgotPasswordClicked = {
                    logcat(LOGIN_SCREEN_TAG) { "onForgotPasswordClicked" }

                },
                onCreateAccountClicked = {
                    logcat(LOGIN_SCREEN_TAG) { "onCreateAccountClicked" }

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

                    } else {
                        // write it
                        try {
                            writeUsernameAndPassword(context = context,
                                usernameText.value,
                                passwordText.value)
                            logcat(LOGIN_SCREEN_TAG) { "Write file success!" }

                        } catch (e: Exception) {
                            // notify user that the file already exists
                            logcat(LOGIN_SCREEN_TAG) { "Problem writing file ${e.localizedMessage as String}" }
                        }
                    }

                },
                checked = checked.value
            )// end card

        }
    )// end Scaffold

}