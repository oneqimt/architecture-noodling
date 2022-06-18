package com.imtmobileapps.view.holding

import androidx.activity.compose.BackHandler
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.imtmobileapps.components.AddHoldingDetailCard
import com.imtmobileapps.model.Coin
import com.imtmobileapps.model.CryptoValue
import com.imtmobileapps.ui.theme.topAppBarBackgroundColor
import com.imtmobileapps.ui.theme.topAppBarContentColor
import com.imtmobileapps.util.Constants.HOLDING_DETAIL_TAG
import com.imtmobileapps.util.removeWhiteSpace
import logcat.logcat

@Composable
fun HoldingDetailScreen(
    viewModel: HoldingViewModel,
    onPopBackStack: () -> Unit

){
    BackHandler {
        onPopBackStack()
    }
    val selectedCoin = viewModel.selectedCoin.collectAsState()
    val selectedCryptoValue = viewModel.selectedCryptoValue.collectAsState()

    val quantityValueText = rememberSaveable {
        mutableStateOf("")
    }

    val costValueText = rememberSaveable {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onPopBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },
                title = {
                    selectedCoin.value?.coinName?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colors.topAppBarContentColor
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor

            )
        },

        content = {
            it.calculateTopPadding()
            var coin: Coin?
            var crypto: CryptoValue?
            selectedCoin.value.let { c ->
                coin = c

            }
            selectedCryptoValue.value.let { cv->
                crypto = cv
            }

            AddHoldingDetailCard(
                quantityValueText = quantityValueText.value,
                costValueText = costValueText.value,
                selectedCoin = coin,
                selectedCryptoValue = crypto,
                onQuantityChanged = { q ->
                    quantityValueText.value = removeWhiteSpace(q)
                    logcat(HOLDING_DETAIL_TAG) { "onQuantityChanged and quantity is: $it" }
                },
                onCostChanged = { cost ->
                    costValueText.value = removeWhiteSpace(cost)
                    logcat(HOLDING_DETAIL_TAG) { "onCostChanged and cost is: $it" }
                },
                addHoldingClicked = {
                    // need CoinHoldings object
                    // do validation here

                    logcat(HOLDING_DETAIL_TAG) { "addHoldingClicked! and quantity is : ${quantityValueText.value} cost is ${costValueText.value}" }
                },

                deleteHolding = {
                    // need Holdings object
                    // do validation here
                    logcat(HOLDING_DETAIL_TAG) { "deleteHoldingClicked!" }
                },

                updateHolding = {
                    // need CoinHoldings object
                    // do validation here
                    logcat(HOLDING_DETAIL_TAG) { "updateHoldingClicked! and quantity is : ${quantityValueText.value} cost is ${costValueText.value}" }

                },
                onDone = {
                    // CryptoValue was null, so user could add the holding
                    // do validation here
                    logcat(HOLDING_DETAIL_TAG) { "onDone! and quantity is : ${quantityValueText.value} cost is ${costValueText.value}" }
                }
            )
        }
    )
}