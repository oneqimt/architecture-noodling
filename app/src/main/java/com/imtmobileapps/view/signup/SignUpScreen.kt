package com.imtmobileapps.view.signup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.imtmobileapps.R
import com.imtmobileapps.components.SignUpCard
import com.imtmobileapps.ui.theme.topAppBarBackgroundColor
import com.imtmobileapps.ui.theme.topAppBarContentColor
import com.imtmobileapps.util.*
import com.imtmobileapps.util.Constants.SIGN_UP_SCREEN_TAG
import com.imtmobileapps.view.portfoliolist.PortfolioListViewModel
import kotlinx.coroutines.launch
import logcat.logcat

@Composable
fun SignUpScreen(
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

    val emailText = rememberSaveable {
        mutableStateOf("")
    }
    val scaffoldState =
        rememberScaffoldState()

    val signUp = viewModel.signUP.collectAsState()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = signUp.value, block = {
        when (signUp.value) {
            is RequestState.Error -> {

            }
            is RequestState.Loading -> {

            }
            is RequestState.Success<*> ->{
                logcat(SIGN_UP_SCREEN_TAG){"SignUp Success go back to LoginScreen"}
                navController.navigate(Routes.LOGIN_SCREEN)
            }
            else -> {}
        }
    })

    Scaffold(scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.register),
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
                when (signUp.value) {
                    is RequestState.Loading -> {
                        CircularProgressIndicator(
                            color = MaterialTheme.colors.primary,
                        )
                    }

                    else -> {
                        SignUpCard(
                            emailText = emailText.value,
                            usernameText = usernameText.value,
                            passwordText = passwordText.value,
                            onEmailChange = { email ->
                                emailText.value = email
                            },
                            onUsernameChanged = { username ->
                                usernameText.value = username
                            },
                            onPasswordChanged = { password ->
                                passwordText.value = password
                            },
                            onDone = {
                                logcat(SIGN_UP_SCREEN_TAG) { "onDone clicked" }
                                if (validateSignUp(emailText.value,
                                        usernameText.value,
                                        passwordText.value)
                                ) {
                                    viewModel.registerUser(emailText.value,
                                        usernameText.value,
                                        passwordText.value)
                                } else {
                                    // REFACTOR into 1 method
                                    scope.launch {
                                        // show snack bar to user
                                        val result = scaffoldState.snackbarHostState.showSnackbar(
                                            message = "Those values and not valid. Please retry.",
                                            actionLabel = "Retry",
                                            SnackbarDuration.Long
                                        )
                                        logcat(Constants.SIGN_UP_SCREEN_TAG) { "result is : ${result.name}" }
                                        if (result == SnackbarResult.ActionPerformed) {
                                            logcat(Constants.SIGN_UP_SCREEN_TAG) { "THEY clicked retry." }
                                            // clear text fields
                                            emailText.value = ""
                                            usernameText.value = ""
                                            passwordText.value = ""

                                        }
                                    }
                                }

                            },
                            onRegisterClicked = {
                                logcat(SIGN_UP_SCREEN_TAG) { "onRegister clicked" }
                                if (validateSignUp(
                                        emailText.value,
                                        usernameText.value,
                                        passwordText.value)
                                ) {
                                    viewModel.registerUser(emailText.value,
                                        usernameText.value,
                                        passwordText.value)
                                } else {
                                   val result =  showSnackbar(scaffoldState, scope)
                                    if (result == SnackbarResult.ActionPerformed){
                                        logcat(Constants.SIGN_UP_SCREEN_TAG) { "THEY clicked retry." }
                                        // clear text fields
                                        emailText.value = ""
                                        usernameText.value = ""
                                        passwordText.value = ""
                                    }
                                }
                            }
                        ) // end SignUpCard
                    } // end else
                }// end when
            }
        }
    )
}


