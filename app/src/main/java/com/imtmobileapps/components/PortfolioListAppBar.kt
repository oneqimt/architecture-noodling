package com.imtmobileapps.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.imtmobileapps.R
import com.imtmobileapps.model.Person
import com.imtmobileapps.ui.theme.LARGE_PADDING
import com.imtmobileapps.ui.theme.topAppBarBackgroundColor
import com.imtmobileapps.ui.theme.topAppBarContentColor
import com.imtmobileapps.util.CoinSort
import com.imtmobileapps.util.Constants.PORTFOLIO_LIST_APP_BAR_TAG
import com.imtmobileapps.util.getDummyPerson
import logcat.logcat

@Composable
fun PortfolioListAppBar(
    onLogout: () -> Unit,
    onSaveSortState: (CoinSort) -> Unit,
    onGetSortState: () -> Unit,
    onAddClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    person: Person,
) {


    DefaultListAppBar(
        onSortClicked = {
            onSaveSortState(it)
            onGetSortState()
        },
        onLogoutClicked = {
            logcat(PORTFOLIO_LIST_APP_BAR_TAG) { "onLogout clicked" }
            onLogout()
        },
        onSettingsClicked = {
            logcat(PORTFOLIO_LIST_APP_BAR_TAG) { "onSettings clicked" }
            onSettingsClicked()
        },

        onAddClicked = {
            logcat(PORTFOLIO_LIST_APP_BAR_TAG) { "onAdd clicked" }
            onAddClicked()
        },

        person = person

    )

}

@Composable
fun DefaultListAppBar(
    onSortClicked: (CoinSort) -> Unit,
    onLogoutClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onAddClicked: () -> Unit,
    person: Person,
) {
    val name = "${person.firstName} ${person.lastName}"

    TopAppBar(
        title = {
            Text(
                text = name,
                color = MaterialTheme.colors.topAppBarContentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            ListAppBarActions(
                onSortClicked = onSortClicked,
                onLogoutClicked = onLogoutClicked,
                onSettingsClicked = onSettingsClicked,
                onAddClicked = onAddClicked
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
    )
}

@Composable
fun ListAppBarActions(
    onSortClicked: (CoinSort) -> Unit,
    onLogoutClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onAddClicked: () -> Unit,
) {
    AddAction(onAddClicked = onAddClicked)
    SortAction(onSortClicked = onSortClicked)
    VerticalMenuAction(
        onSettingsClicked = {
            onSettingsClicked()
        },
        onLogoutClicked = {
            onLogoutClicked()
        }
    )

}

@Composable
fun AddAction(
    onAddClicked: () -> Unit,
) {

    IconButton(onClick = {
        onAddClicked()
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_add_24),
            contentDescription = stringResource(id = R.string.sort_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }

}

@Composable
fun SortAction(
    onSortClicked: (CoinSort) -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = {
        expanded = true
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_filter_list_24),
            contentDescription = stringResource(id = R.string.sort_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onSortClicked(CoinSort.NAME)
                })
            {
                SortItem(coinSort = CoinSort.NAME)
            }
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onSortClicked(CoinSort.SYMBOL)
                })
            {
                SortItem(coinSort = CoinSort.SYMBOL)
            }
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onSortClicked(CoinSort.RANK)
                })
            {
                SortItem(coinSort = CoinSort.RANK)
            }
        }
    }
}

@Composable
fun VerticalMenuAction(
    onLogoutClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = {
        expanded = true
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_vertical_menu),
            contentDescription = "vertical menu",
            tint = MaterialTheme.colors.topAppBarContentColor
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {

            DropdownMenuItem(onClick = {
                expanded = false
                onSettingsClicked()
            }) {
                Text(
                    modifier = Modifier.padding(start = LARGE_PADDING),
                    text = stringResource(R.string.settings),
                    style = MaterialTheme.typography.subtitle2
                )
            }

            DropdownMenuItem(onClick = {
                expanded = false
                onLogoutClicked()

            }) {
                Text(
                    modifier = Modifier.padding(start = LARGE_PADDING),
                    text = stringResource(R.string.logout),
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}

@Composable
@Preview
private fun DefaultListAppBarPreview() {
    DefaultListAppBar(
        onSortClicked = {},
        onLogoutClicked = {},
        onSettingsClicked = {},
        onAddClicked = {},
        person = getDummyPerson()

    )

}

