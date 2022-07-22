package com.imtmobileapps.view.login

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
import com.imtmobileapps.util.Constants.MINIMUM_CHARS
import com.imtmobileapps.util.validatePassword
import com.imtmobileapps.util.validateUsername
import kotlinx.coroutines.launch

@Composable
fun LoginCard(
    usernameText: String,
    passwordText: String,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onDone: () -> Unit,
    onSignInClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onCreateAccountClicked: () -> Unit,
    onRememberMeChecked: (Boolean) -> Unit,
    checked: Boolean,
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp),
        elevation = 4.dp,
        border = BorderStroke(0.3.dp, MaterialTheme.colors.cardBorderColor),
        backgroundColor = MaterialTheme.colors.cardBackgroundColor,
        shape = RoundedCornerShape(corner = CornerSize(6.dp))
    ){
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        val focusManager = LocalFocusManager.current

        val passwordVisibility = remember { mutableStateOf(false) }
        val isUsernameError = rememberSaveable { mutableStateOf(false) }
        val isPasswordError = rememberSaveable { mutableStateOf(false) }

        // string resources
        val errorMessage = stringResource(id = R.string.check_fields)
        val enterValidValues = stringResource(id = R.string.enter_valid_values)
        val fieldsNotValid = stringResource(id = R.string.fields_are_not_valid)

        Scaffold(scaffoldState = scaffoldState) {
            it.calculateBottomPadding()
            Column(
                modifier =
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(10.dp, 20.dp))
            {

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    /* *****************-USERNAME-****************** */
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
                            isUsernameError.value = value.length <= MINIMUM_CHARS

                        },

                        isError = isUsernameError.value
                    )
                }// end username

                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center)
                {
                    /* *****************-PASSWORD-****************** */
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = passwordText,
                        onValueChange = { value ->
                            onPasswordChanged(value)
                            isPasswordError.value = value.length <= MINIMUM_CHARS
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
                }// end Password

                Spacer(modifier = Modifier.height(10.dp))
                /* *****************-REMEMBER ME-****************** */
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End)
                {
                    // checkbox
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { value ->
                            if (validateUsername(usernameText) && validatePassword(passwordText)) {
                                onRememberMeChecked(value)
                            } else {
                                focusManager.clearFocus()
                                // show snackbar
                                scope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        message = enterValidValues,
                                        actionLabel = fieldsNotValid
                                    )
                                }
                            }
                        }
                    )

                    Text(
                        text = stringResource(id = R.string.remember_me),
                        modifier = Modifier.padding(5.dp, 12.dp, 10.dp, 0.dp)
                    )
                }// end Remember
                Spacer(modifier = Modifier.height(10.dp))
                /* *****************-FORGOT PASSWORD-****************** */
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center)
                {
                    TextButton(onClick = {
                        onForgotPasswordClicked()
                    }, enabled = true) {
                        Text(text = stringResource(id = R.string.forgot_password))
                    }
                }// end Forgot Password

                /* *****************-SIGN IN BUTTON-****************** */
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center)
                {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (validateUsername(usernameText) && validatePassword(passwordText)) {
                                onSignInClicked()
                                focusManager.clearFocus()
                            } else {

                                // Have to check both fields, so that we may show which one has error
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

                        })
                    {
                        Text(text = stringResource(id = R.string.sign_in))
                    }

                }// end sign in button

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center)
                {
                    Text(text = stringResource(id = R.string.do_not_have_account))

                }// end static text

                /* *****************-CREATE ACCOUNT BUTTON-****************** */
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center)
                {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            focusManager.clearFocus()
                            onCreateAccountClicked()
                        })
                    {
                        Text(text = stringResource(id = R.string.create_account))
                    }

                }// end Create Account
            } // end column
        }
    }

}
@Preview
@Composable
fun LoginCardPreview() {
    val username = "test1"
    val password = "pass"
    LoginCard(usernameText = username,
        passwordText = password,
        onUsernameChanged = {},
        onPasswordChanged = {},
        onDone = {},
        onSignInClicked = {},
        onForgotPasswordClicked = {},
        onCreateAccountClicked = {},
        onRememberMeChecked = {},
        checked = false
    )
}
