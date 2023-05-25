package com.imtmobileapps.view.portfoliolist

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.imtmobileapps.R
import com.imtmobileapps.components.CircularProgressBar
import com.imtmobileapps.components.PortfolioListAppBar
import com.imtmobileapps.model.CryptoValue
import com.imtmobileapps.model.Person
import com.imtmobileapps.ui.theme.staticTextColor
import com.imtmobileapps.util.CoinSort
import com.imtmobileapps.util.Constants.PORTFOLIO_LIST_TAG
import com.imtmobileapps.util.RequestState
import com.imtmobileapps.util.Routes
import com.imtmobileapps.util.deleteSensitiveFile
import com.imtmobileapps.util.resetApp
import com.imtmobileapps.util.showSnackbar
import kotlinx.coroutines.launch
import logcat.logcat
import retrofit2.HttpException


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun PortfolioList(
    viewModel: PortfolioListViewModel,
    navController: NavController,
) {

    BackHandler {
        // prevents going back to login
    }

    val portfolioCoins: State<RequestState<List<CryptoValue>>> =
        viewModel.portfolioCoins.collectAsStateWithLifecycle()

    val sortState: State<RequestState<CoinSort>> = viewModel.sortState.collectAsStateWithLifecycle()

    val cachedPerson: State<RequestState<Person>> =
        viewModel.personCached.collectAsStateWithLifecycle()
    val scaffoldState = rememberScaffoldState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var doScrollList = false

    LaunchedEffect(key1 = portfolioCoins.value, block = {

        when (portfolioCoins.value) {
            is RequestState.Error -> {
                val error = (portfolioCoins.value as RequestState.Error)
                if (error.exception is HttpException) {
                    // do nothing, it is handled below
                } else {
                    val result = showSnackbar(scaffoldState,
                        scope,
                        error.toString(),
                        "Login Again")
                    if (result == SnackbarResult.ActionPerformed) {
                        logcat(PORTFOLIO_LIST_TAG) { "THEY clicked retry." }
                        // Repeated code in this Composable,
                        // I think we could move the file delete to LoginScreen
                        try {
                            deleteSensitiveFile(context = context)
                        } catch (e: Exception) {
                            logcat(PORTFOLIO_LIST_TAG) {
                                "Problem DELETING FILE ${e.localizedMessage as String}"
                            }
                        }
                        viewModel.logout()
                        navController.navigate(Routes.LOGIN_SCREEN)
                        resetApp(context)
                    }
                }
            }
            else -> {}
        }

    })

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            PortfolioListAppBar(
                onLogout = {
                    scope.launch {
                        try {
                            deleteSensitiveFile(context = context)
                        } catch (e: Exception) {
                            logcat(PORTFOLIO_LIST_TAG) {
                                "Problem DELETING FILE ${e.localizedMessage as String}"
                            }
                        }
                    }
                    viewModel.logout()
                    navController.navigate(Routes.LOGIN_SCREEN)
                    resetApp(context)

                },
                onSaveSortState = { coinSort ->
                    doScrollList = true
                    viewModel.saveSortState(coinSort)
                },
                onGetSortState = {
                    // a call to this will set the value
                    viewModel.getSortState()
                    logcat(PORTFOLIO_LIST_TAG) { "Sort state on viewModel is : ${viewModel.sortState.value}" }
                },
                onAddClicked = {
                    logcat(PORTFOLIO_LIST_TAG) { "Add clicked navigate to HoldingDetailScreen" }
                    navController.navigate(Routes.HOLDING_LIST)
                },
                onSettingsClicked = {
                    logcat(PORTFOLIO_LIST_TAG) { "Settings clicked" }
                },
                onAccountClicked = {
                    logcat(PORTFOLIO_LIST_TAG) { "Account clicked" }
                    navController.navigate(Routes.ACCOUNT_SCREEN)
                },
                person = (cachedPerson.value as RequestState.Success<Person>).data

            )


        }
    ) {
        it.calculateTopPadding()
        when (portfolioCoins.value) {
            RequestState.Loading -> {
                Column(
                    modifier = Modifier.padding(30.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    CircularProgressBar()
                }
            }

            is RequestState.Error -> {
                // if person has no coins yet - 400 error
                val error = (portfolioCoins.value as RequestState.Error)
                val errorText: String
                if (error.exception is HttpException) {
                    errorText = stringResource(id = R.string.no_coins_yet)
                    logcat(PORTFOLIO_LIST_TAG) { "MAYBE NO COINS YET CODE is : ${error.exception.code()}" }

                } else {
                    // handle other exceptions
                    errorText = stringResource(id = R.string.error_retrieving_coins)
                }
                Column(
                    modifier = Modifier.padding(30.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Text(
                        modifier = Modifier.padding(2.dp),
                        text = errorText,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.staticTextColor,
                        style = MaterialTheme.typography.h6,
                        maxLines = 9,
                        overflow = TextOverflow.Ellipsis

                    )
                }
            }

            is RequestState.Success -> {
                val list = (portfolioCoins.value as RequestState.Success<List<CryptoValue>>).data
                LaunchedEffect(key1 = sortState.value) {
                    if (doScrollList) {
                        scope.launch {
                            listState.animateScrollToItem(0, 0)
                        }
                    }
                }
                LazyColumn(
                    // don't use lazy list state here
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 4.dp),
                    state = listState
                ) {
                    items(items = list, key = { cryptoValue ->
                        cryptoValue.id
                    }) { cryptoValue ->
                        PortfolioListItem(
                            cryptoValue = cryptoValue,
                            onCardClicked = {
                                // Do not scroll
                                doScrollList = false
                                viewModel.setSelectedCryptoValue(cryptoValue)
                                navController.navigate(Routes.PORTFOLIO_DETAIL)
                            }
                        )
                    }
                } // end lazy column
            }
            else -> Unit
        }// end when
    }
}