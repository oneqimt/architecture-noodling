package com.imtmobileapps.view.account

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.imtmobileapps.R
import com.imtmobileapps.components.CircularProgressBar
import com.imtmobileapps.model.Person
import com.imtmobileapps.model.State
import com.imtmobileapps.ui.theme.topAppBarBackgroundColor
import com.imtmobileapps.ui.theme.topAppBarContentColor
import com.imtmobileapps.util.Constants.ACCOUNT_SCREEN_TAG
import com.imtmobileapps.util.RequestState
import com.imtmobileapps.util.showSnackbar
import logcat.logcat

@ExperimentalMaterialApi
@Composable
fun AccountScreen(
    viewModel: AccountViewModel,
    navController: NavController,
) {
    BackHandler {
        navController.popBackStack()
    }
    val scaffoldState =
        rememberScaffoldState()

    val scope = rememberCoroutineScope()

    val cachedPerson: androidx.compose.runtime.State<RequestState<Person>> =
        viewModel.personCached.collectAsState()

    val personId = viewModel.personId.collectAsState()
    val states = viewModel.states.collectAsState()

    val firstNameText = rememberSaveable {
        mutableStateOf("")
    }

    val lastNameText = rememberSaveable {
        mutableStateOf("")
    }

    val emailText = rememberSaveable {
        mutableStateOf("")
    }

    val addressText = rememberSaveable {
        mutableStateOf("")
    }

    val phoneText = rememberSaveable {
        mutableStateOf("")
    }

    val cityText = rememberSaveable {
        mutableStateOf("")
    }

    val zipText = rememberSaveable {
        mutableStateOf("")
    }
    // State is Parcelable, so it can be bundled to save
    val selectedState = rememberSaveable {
        mutableStateOf(State(0, "", "", abbreviation = ""))
    }

    LaunchedEffect(key1 = cachedPerson.value, block = {
        when (cachedPerson.value) {
            is RequestState.Success -> {
                val person = (cachedPerson.value as RequestState.Success<Person>).data
                firstNameText.value = person.firstName.toString()
                lastNameText.value = person.lastName.toString()
                emailText.value = person.email.toString()
                addressText.value = person.address.toString()
                phoneText.value = person.phone.toString()
                cityText.value = person.city.toString()
                zipText.value = person.zip.toString()
                selectedState.value = person.state!!

                viewModel.updatePersonLocal(person)
            }
            is RequestState.Error -> {
                val message = "There was a problem updating you account. Retry."
                val label = "Retry"
                val result = showSnackbar(scaffoldState, scope, message, label)
                if (result == SnackbarResult.ActionPerformed) {
                    logcat(ACCOUNT_SCREEN_TAG){"Do whatever to UI..."}
                }

            }
            else -> {}
        }

    })

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.account),
                        color = MaterialTheme.colors.topAppBarContentColor
                    )
                },
                backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
            )
        }
    ) {
        it.calculateTopPadding()

        when (cachedPerson.value) {
            RequestState.Loading -> {
                Column(
                    modifier = Modifier.padding(30.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    CircularProgressBar()
                }
            }
            is RequestState.Success -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (personId.value != -1) {
                        val person = (cachedPerson.value as RequestState.Success<Person>).data
                        person.state?.let { pstate ->
                            AccountCard(
                                firstNameText = firstNameText.value,
                                lastNameText = lastNameText.value,
                                emailNameText = emailText.value,
                                addressNameText = addressText.value,
                                cityNameText = cityText.value,
                                phoneNameText = phoneText.value,
                                selectedState = pstate,
                                zipNameText = zipText.value,
                                person = person,
                                states = states.value,
                                onFirstNameChanged = { firstName ->
                                    firstNameText.value = firstName
                                },
                                onLastNameChanged = { lastName ->
                                    lastNameText.value = lastName
                                },
                                onEmailChange = { email ->
                                    emailText.value = email
                                },
                                onAddressChanged = { address ->
                                    addressText.value = address
                                },
                                onCityChanged = { city ->
                                    cityText.value = city
                                },
                                onStateSelectionChanged = { state2 ->
                                    selectedState.value = state2
                                },
                                onPhoneChanged = { phone ->
                                    phoneText.value = phone
                                },
                                onSaveClicked = {
                                    val updatedPerson = Person(
                                        personId = personId.value,
                                        firstName = firstNameText.value,
                                        lastName = lastNameText.value,
                                        email = emailText.value,
                                        address = addressText.value,
                                        city = cityText.value,
                                        state = selectedState.value,
                                        zip = zipText.value,
                                        phone = phoneText.value
                                    )
                                    viewModel.updatePersonRemote(updatedPerson)
                                    logcat(ACCOUNT_SCREEN_TAG) { " onSaveClicked and Person to update is $updatedPerson" }
                                },
                                onZipChanged = { zip ->
                                    zipText.value = zip
                                },
                                onDone = {
                                    val updatedPerson = Person(
                                        personId = personId.value,
                                        firstName = firstNameText.value,
                                        lastName = lastNameText.value,
                                        email = emailText.value,
                                        address = addressText.value,
                                        city = cityText.value,
                                        state = selectedState.value,
                                        zip = zipText.value,
                                        phone = phoneText.value
                                    )
                                    viewModel.updatePersonRemote(updatedPerson)
                                    logcat(ACCOUNT_SCREEN_TAG) { " onDoneClicked and Person to update is $updatedPerson" }
                                }
                            )
                        }// end column
                    }
                }
            }
            else -> {}
        }

    }
}