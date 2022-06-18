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
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.imtmobileapps.components.CircularProgressBar
import com.imtmobileapps.components.PortfolioListAppBar
import com.imtmobileapps.model.CryptoValue
import com.imtmobileapps.util.*
import com.imtmobileapps.util.Constants.PORTFOLIO_LIST_TAG
import kotlinx.coroutines.launch
import logcat.logcat


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

    val personCoins: State<RequestState<List<CryptoValue>>> =
        viewModel.personCoins.collectAsState()

    val sortState: State<RequestState<CoinSort>> = viewModel.sortState.collectAsState()

    val person = viewModel.person.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var doScrollList = false


    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background,
        topBar = {

            person.value?.let {
                PortfolioListAppBar(
                    onLogout = {
                        scope.launch {
                            try {
                                deleteSensitiveFile(context = context)
                            } catch (e: Exception) {
                                logcat(Constants.PORTFOLIO_LIST_APP_BAR_TAG) {
                                    "Problem DELETING FILE ${e.localizedMessage as String}"
                                }
                            }

                        }
                        viewModel.logout()
                        navController.navigate(Routes.LOGIN_SCREEN)

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
                    person = it
                )
            }

        },
        content = {
            it.calculateTopPadding()
            when (personCoins.value) {
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

                    val list = (personCoins.value as RequestState.Success<List<CryptoValue>>).data

                    LaunchedEffect(key1 = sortState.value){
                        if (doScrollList){
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
                            PersonCoinsListItem(
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

        })

}