package com.imtmobileapps.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.imtmobileapps.R
import com.imtmobileapps.ui.theme.topAppBarBackgroundColor
import com.imtmobileapps.ui.theme.topAppBarContentColor
import com.imtmobileapps.util.Constants.SEARCH_APP_BAR_TAG
import com.imtmobileapps.util.SearchAppBarState
import logcat.logcat

@Composable
fun SearchViewActionBar(
    onPopBackStack: () -> Unit,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchTriggered: () -> Unit,
    searchState: SearchAppBarState,
    searchTextState: String,

    ) {

    AppBar(
        searchState = searchState,
        searchTextState = searchTextState,
        onTextChange = {
            onTextChange(it)

        },
        onCloseClicked = {
            onCloseClicked()
        },
        onSearchTriggered = {
            onSearchTriggered()

        },
        onPopBackStack = {
            onPopBackStack()
        }
    )

}

@Composable
fun DefaultAppBar(
    onSearchClicked: () -> Unit,
    onPopBackStack: () -> Unit,
) {
    TopAppBar(
        elevation = 4.dp,
        navigationIcon = {

            IconButton(onClick = {
                logcat("DefaultAppBar") { "DefaultAppBar onNavigate called" }
                onPopBackStack()
            }
            ) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.add_holding),
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },

        actions = {
            IconButton(onClick = {
                onSearchClicked()
                logcat("DefaultAppBar") { "Search Clicked" }
            }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.search_coins),
                    tint = MaterialTheme.colors.topAppBarContentColor
                )
            }
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
    )
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.topAppBarBackgroundColor
    ) {
        TextField(
            value = text,
            onValueChange = {
                onTextChange(it)
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.search_coins),
                    color = Color.White,
                    modifier = Modifier.alpha(ContentAlpha.medium)
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = { },
                    modifier = Modifier.alpha(ContentAlpha.medium)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "SearchImage",
                        tint = Color.White
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "CloseImage",
                        tint = Color.White
                    )
                }
            },

            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onCloseClicked()
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Color.White.copy(alpha = ContentAlpha.medium)
            )
        )
    }

}

@Composable
fun AppBar(
    searchState: SearchAppBarState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchTriggered: () -> Unit,
    onPopBackStack: () -> Unit,
) {
    when (searchState) {
        SearchAppBarState.CLOSED -> {
            DefaultAppBar(
                onSearchClicked = onSearchTriggered,
                onPopBackStack = onPopBackStack
            )
        }
        SearchAppBarState.OPENED -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = onTextChange,
                onCloseClicked = onCloseClicked
            )
        }
    }
}
