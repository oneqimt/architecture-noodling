package com.imtmobileapps.view.signup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imtmobileapps.R
import com.imtmobileapps.ui.theme.cardBackgroundColor
import com.imtmobileapps.ui.theme.cardBorderColor
import com.imtmobileapps.util.Constants
import com.imtmobileapps.util.Constants.SIGN_UP_CARD
import com.imtmobileapps.util.validateEmail
import com.imtmobileapps.util.validatePassword
import com.imtmobileapps.util.validateUsername
import kotlinx.coroutines.launch
import logcat.logcat

@Composable
fun SignUpCard(
    emailText: String,
    usernameText: String,
    passwordText: String,
    onEmailChange: (String) -> Unit,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onDone: () -> Unit,
    onRegisterClicked: () -> Unit,

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
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()
        val focusManager = LocalFocusManager.current
        val isUsernameError = rememberSaveable { mutableStateOf(false) }
        val isPasswordError = rememberSaveable { mutableStateOf(false) }
        val isEmailError = rememberSaveable { mutableStateOf(false) }
        val passwordVisibility = remember { mutableStateOf(false) }
        // string resources
        val errorMessage = stringResource(id = R.string.check_fields)

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
                    /* *****************-EMAIL-****************** */
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = emailText,
                        label = { Text(text = stringResource(id = R.string.email)) },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Email),
                        keyboardActions = KeyboardActions(onNext = {
                            val isValidEmail = validateEmail(emailText)
                            if (isValidEmail) {
                                isEmailError.value = false
                                focusManager.moveFocus(FocusDirection.Down)

                            } else {
                                isEmailError.value = true
                            }
                        }),
                        onValueChange = { value ->
                            logcat(SIGN_UP_CARD){"emailValue is $value"}
                            onEmailChange(value)
                            isEmailError.value = !validateEmail(value)

                        },
                        isError = isEmailError.value
                    )

                }// end email

                Spacer(modifier = Modifier.height(20.dp))
                /* *****************-USERNAME-****************** */
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = usernameText,
                        label = { Text(text = stringResource(id = R.string.user_name)) },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text),
                        keyboardActions = KeyboardActions(onNext = {
                            if (!validateUsername(usernameText)) {
                                isUsernameError.value = true

                            } else {
                                focusManager.moveFocus(FocusDirection.Down)
                                isUsernameError.value = false
                            }
                        }),
                        onValueChange = { value ->
                            onUsernameChanged(value)
                            isUsernameError.value = value.length <= Constants.MINIMUM_CHARS

                        },
                        isError = isUsernameError.value
                    )
                }// end username

                Spacer(modifier = Modifier.height(20.dp))
                /* *****************-PASSWORD-****************** */
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center)
                {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = passwordText,
                        onValueChange = { value ->
                            onPasswordChanged(value)
                            isPasswordError.value = value.length <= Constants.MINIMUM_CHARS
                        },
                        label = { (Text(text = stringResource(id = R.string.password))) },
                        placeholder = { Text(text = stringResource(id = R.string.password)) },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Password),

                        keyboardActions = KeyboardActions(onDone = {
                            if (!validatePassword(passwordText)) {
                                isPasswordError.value = true

                            } else {
                                focusManager.clearFocus()
                                isPasswordError.value = false
                                onDone()
                            }
                        }),
                        visualTransformation =
                        if (passwordVisibility.value) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisibility.value) {
                                Icons.Filled.Visibility
                            } else {
                                Icons.Filled.VisibilityOff
                            }
                            IconButton(onClick = {
                                passwordVisibility.value = !passwordVisibility.value
                            }) {
                                Icon(imageVector = image, null)
                            }
                        },

                        isError = isPasswordError.value

                    )
                }// end password
                Spacer(modifier = Modifier.height(20.dp))
                /* *****************-REGISTER BUTTON-****************** */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center)
                {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            // check if all fields are valid
                            if (validateEmail(emailText) && validateUsername(usernameText) && validatePassword(passwordText)){
                                onRegisterClicked()
                                focusManager.clearFocus()
                            }else{
                                // Have to check all fields, so that we may show which one has error
                                if (!validateEmail(emailText)){
                                    isEmailError.value = true
                                }
                                if (!validateUsername(usernameText)) {
                                    isUsernameError.value = true
                                }
                                if (!validateUsername(passwordText)) {
                                    isPasswordError.value = true
                                }

                                focusManager.clearFocus()

                                scope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(errorMessage)
                                }
                            }

                        }
                    ) {
                        Text(text = stringResource(id = R.string.register))
                    }
                }// end button
            }
        }
    }
}

@Preview
@Composable
fun SignUpCardPreview(

) {

    SignUpCard(
        emailText = "oneqimt@gmail.com",
        usernameText = "denny1",
        passwordText = "1111",
        onEmailChange = {},
        onUsernameChanged = {},
        onPasswordChanged = {},
        onDone = {},
        onRegisterClicked = {}
    )

}