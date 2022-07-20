package com.imtmobileapps.view.account

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.imtmobileapps.R
import com.imtmobileapps.components.AccountCard
import com.imtmobileapps.components.CircularProgressBar
import com.imtmobileapps.model.Person
import com.imtmobileapps.model.State
import com.imtmobileapps.ui.theme.topAppBarBackgroundColor
import com.imtmobileapps.ui.theme.topAppBarContentColor
import com.imtmobileapps.util.Constants.ACCOUNT_SCREEN_TAG
import com.imtmobileapps.util.RequestState
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
        },
        content = {
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
                        val person = (cachedPerson.value as RequestState.Success<Person>).data
                        var s  = State(id = 0, name = "", abbreviation = "")
                        if (person.state != null) {
                            s = person.state
                        }

                        AccountCard(
                            firstNameText = firstNameText.value,
                            lastNameText = lastNameText.value,
                            emailNameText = emailText.value,
                            addressNameText = addressText.value,
                            cityNameText = cityText.value,
                            phoneNameText = phoneText.value,
                            selectedState = s,
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
                                viewModel.updatePerson(updatedPerson)
                                logcat(ACCOUNT_SCREEN_TAG) { " onSaveClicked and Person to update is $updatedPerson" }
                            },
                            onZipChanged = { zip ->
                                zipText.value = zip
                            },
                            onDone = {
                                // call viewModel
                                logcat(ACCOUNT_SCREEN_TAG) { " onDoneClicked" }
                            }
                        )

                    }// end column
                }

                is RequestState.Error -> {

                }
                else -> {}
            }

        }
    )
}