package com.imtmobileapps.view.holding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imtmobileapps.components.CircularProgressBar
import com.imtmobileapps.components.SearchViewActionBar
import com.imtmobileapps.model.Coin
import com.imtmobileapps.util.Constants.FILTERED_COINS_VIEW_TAG
import com.imtmobileapps.util.Constants.HOLDING_LIST_TAG
import com.imtmobileapps.util.DataType
import com.imtmobileapps.util.RequestState
import com.imtmobileapps.util.SearchAppBarState
import kotlinx.coroutines.flow.StateFlow
import logcat.logcat

@ExperimentalMaterialApi
@Composable
fun HoldingListScreen(
    onNavigate: () -> Unit,
    onPopBackStack: () -> Unit,
    viewModel: HoldingViewModel,

    ) {
    // System back button
    BackHandler {
        onPopBackStack()
    }

    val allCoins: State<RequestState<MutableList<Coin>>> =
        viewModel.allCoins.collectAsState()

    val dataType by viewModel.dataType
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SearchViewActionBar(
                onPopBackStack = { onPopBackStack() },
                searchState = viewModel.searchState.value,
                searchTextState = viewModel.searchTextState.value,
                onTextChange = {
                    viewModel.updateSearchTextState(it)
                    viewModel.filterListForSearch()
                },
                onSearchTriggered = {
                    viewModel.updateSearchState(SearchAppBarState.OPENED)
                    viewModel.updateSearchTextState("")

                },
                onCloseClicked = {
                    viewModel.updateSearchState(SearchAppBarState.CLOSED)
                    viewModel.fetchAllCoinsFromRemote()

                }
            )
        }

    ) { it ->
        it.calculateTopPadding()
        when (dataType) {
            DataType.ALL_COINS -> {
                when (allCoins.value) {
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

                        val allCoinsList =
                            (allCoins.value as RequestState.Success<MutableList<Coin>>).data

                        LazyColumn(
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                        ) {

                            items(allCoinsList, key = { coin ->
                                coin.cmcRank
                            }) {
                                HoldingListItem(
                                    coin = it,
                                    onCardCLicked = { coin ->
                                        logcat(HOLDING_LIST_TAG) { "Card Clicked and selectedCoin is $coin" }
                                        viewModel.setSelectedCoin(coin)
                                        coin.coinName.let { coinName ->
                                            if (coinName != null) {
                                                viewModel.getSelectedCryptoValue(coinName = coinName)
                                            }
                                        }

                                        onNavigate()
                                    }
                                )
                            }

                        }
                    }
                    else -> {}
                }// end 1st when
            }

            DataType.FILTERED_COINS -> {
                FilteredCoinsView(
                    viewModel = viewModel,
                    onCoinClicked = { coin ->
                        logcat(FILTERED_COINS_VIEW_TAG) { "Card Clicked and selectedCoin is $coin" }
                        viewModel.setSelectedCoin(coin)
                        coin.coinName.let { coinName ->
                            if (coinName != null) {
                                viewModel.getSelectedCryptoValue(coinName = coinName)
                            }
                        }

                        onNavigate()
                    }
                )
            }
        }


    }
}