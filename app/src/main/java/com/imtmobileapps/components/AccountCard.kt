package com.imtmobileapps.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.imtmobileapps.R
import com.imtmobileapps.model.Person
import com.imtmobileapps.model.State
import com.imtmobileapps.ui.theme.cardBackgroundColor
import com.imtmobileapps.ui.theme.cardBorderColor
import com.imtmobileapps.ui.theme.spinnerBackgroundColor
import com.imtmobileapps.util.Constants.ACCOUNT_CARD
import com.imtmobileapps.util.getDummyPerson
import com.imtmobileapps.util.validateEmail
import com.imtmobileapps.util.validatePhone
import logcat.logcat

@ExperimentalMaterialApi
@Composable
fun AccountCard(
    firstNameText: String,
    lastNameText: String,
    emailNameText: String,
    addressNameText: String,
    phoneNameText: String,
    cityNameText: String,
    selectedState: State,
    zipNameText: String,
    person: Person,
    states: List<State>,
    onFirstNameChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onAddressChanged: (String) -> Unit,
    onPhoneChanged: (String) -> Unit,
    onCityChanged: (String) -> Unit,
    onStateSelectionChanged: (State) -> Unit,
    onZipChanged: (String) -> Unit,
    onSaveClicked: () -> Unit,
    onDone: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp),
        elevation = 4.dp,
        border = BorderStroke(0.3.dp, MaterialTheme.colors.cardBorderColor),
        backgroundColor = MaterialTheme.colors.cardBackgroundColor,
        shape = RoundedCornerShape(corner = CornerSize(6.dp))
    ) {
        val scaffoldState = rememberScaffoldState()
        val focusManager = LocalFocusManager.current
        // Fields we have to validate
        val isEmailError = rememberSaveable { mutableStateOf(false) }
        val isPhoneError = rememberSaveable { mutableStateOf(false) }

        Scaffold(scaffoldState = scaffoldState) {
            it.calculateTopPadding()
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(10.dp, 20.dp)

            ) {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // FIRST NAME
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = firstNameText,
                        label = { Text(text = stringResource(id = R.string.first_name)) },
                        onValueChange = { first ->
                            onFirstNameChanged(first)
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }

                        )
                    )
                }// end first name

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // LAST NAME
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = lastNameText,
                        label = { Text(text = stringResource(id = R.string.last_name)) },
                        onValueChange = { last ->
                            onLastNameChanged(last)
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }

                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // EMAIL
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = emailNameText,
                        label = { Text(text = stringResource(id = R.string.email)) },
                        onValueChange = { email ->
                            onEmailChange(email)
                        },
                        isError = isEmailError.value,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Email),
                        keyboardActions = KeyboardActions(onNext = {
                            val isValidEmail: Boolean = if (emailNameText.isEmpty()) {
                                // use email on person object
                                validateEmail(person.email!!)
                            } else {
                                validateEmail(emailNameText)
                            }
                            if (isValidEmail) {
                                isEmailError.value = false
                                focusManager.moveFocus(FocusDirection.Down)

                            } else {
                                isEmailError.value = true
                            }
                        }

                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // ADDRESS
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        value = addressNameText,
                        label = { Text(text = stringResource(id = R.string.address)) },
                        maxLines = 2,
                        onValueChange = { address ->
                            onAddressChanged(address)
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }

                        )
                    )
                }// end address

                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // PHONE
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = phoneNameText,
                        label = { Text(text = stringResource(id = R.string.phone)) },
                        onValueChange = { phone ->
                            onPhoneChanged(phone)
                        },
                        isError = isPhoneError.value,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Phone),
                        keyboardActions = KeyboardActions(onNext = {
                            // If they entered a phone, let's validate it
                            if (phoneNameText.isNotEmpty()) {
                                val isValidPhone = validatePhone(phoneNameText)
                                if (isValidPhone) {
                                    isPhoneError.value = false
                                    focusManager.moveFocus(FocusDirection.Down)

                                } else {
                                    isPhoneError.value = true
                                }
                            } else {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        }
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // CITY
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = cityNameText,
                        label = { Text(text = stringResource(id = R.string.city)) },
                        onValueChange = { city ->
                            onCityChanged(city)
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }

                        )
                    )
                }// end city
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.spinnerBackgroundColor.copy(0.2f)),
                    horizontalArrangement = Arrangement.Center
                ) {
                    SpinnerCompose(
                        states = states,
                        preselectedState = selectedState,
                        onSelectionChanged = { selState ->
                            onStateSelectionChanged(selState)
                            logcat(ACCOUNT_CARD) { "state changed and it is $selState" }
                        }
                    )
                }// end state

                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // ZIP
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = zipNameText,
                        label = { Text(text = stringResource(id = R.string.zip)) },
                        onValueChange = { zip ->
                            onZipChanged(zip)
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text),
                        keyboardActions = KeyboardActions(onDone = {
                            // Maybe validate email/phone
                            onDone()
                        }
                        ))
                }// end zip

                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // SAVE BUTTON
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = {
                            onSaveClicked()
                            logcat(ACCOUNT_CARD) { "SAVE clicked!" }
                        }
                    ) {
                        Text(text = stringResource(id = R.string.save))
                    }
                }//end save button

            }
        }
    }

}

@ExperimentalMaterialApi
@Preview(name = "Preview1", showBackground = true)
@Composable
fun AccountCardPreview(

) {
    AccountCard(
        firstNameText = "",
        lastNameText = "",
        emailNameText = "",
        addressNameText = "",
        phoneNameText = "",
        cityNameText = "",
        selectedState = State(id = 0, name = "", abbreviation = ""),
        zipNameText = "",
        person = getDummyPerson(),
        states = emptyList(),
        onFirstNameChanged = { },
        onLastNameChanged = { },
        onEmailChange = {},
        onAddressChanged = { },
        onPhoneChanged = {},
        onCityChanged = {},
        onStateSelectionChanged = {},
        onZipChanged = {},
        onSaveClicked = {},
        onDone = {}
    )
}