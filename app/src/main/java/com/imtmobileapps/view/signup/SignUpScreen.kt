package com.imtmobileapps.view.signup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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
import com.imtmobileapps.util.Constants.SIGN_UP_SCREEN_TAG
import com.imtmobileapps.view.portfoliolist.PortfolioListViewModel
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
        rememberScaffoldState() // This is here in case we want to display a snackbar

    val context = LocalContext.current

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
                SignUpCard(
                    emailText = emailText.value,
                    usernameText = usernameText.value,
                    passwordText = passwordText.value,
                    onEmailChange = {email ->
                        emailText.value = email
                    },
                    onUsernameChanged = {username ->
                        usernameText.value = username
                    },
                    onPasswordChanged = {password ->
                        passwordText.value = password
                    },
                    onDone = {
                        logcat(SIGN_UP_SCREEN_TAG){"onDone clicked"}
                    },
                    onRegisterClicked = {
                        logcat(SIGN_UP_SCREEN_TAG){"onRegister clicked"}
                    }
                )
            }// end SignUpCard
        }


    )


}
