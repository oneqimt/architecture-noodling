package com.imtmobileapps.view.portfoliodetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.imtmobileapps.components.EditCoin
import com.imtmobileapps.model.CryptoValue
import com.imtmobileapps.model.TotalValues
import com.imtmobileapps.ui.theme.*
import com.imtmobileapps.util.RequestState
import com.imtmobileapps.util.RowType
import com.imtmobileapps.util.SheetType
import com.imtmobileapps.util.removeWhiteSpace
import com.imtmobileapps.view.portfoliolist.PortfolioListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PortfolioDetail(
    onPopBackStack: () -> Unit,
    onEditClicked: () -> Unit,
    viewModel: PortfolioListViewModel,

    ) {
    BackHandler {
        onPopBackStack()
    }
    // remember calculates the value passed to it only during the first composition. It then
    // returns the same value for every subsequent composition.
    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scrollState = rememberScrollState()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()
    // val scaffoldState = rememberScaffoldState()

    val selectedCryptoValue: State<CryptoValue?> = viewModel.selectedCryptoValue.collectAsState()
    // pull the TotalValues object out of the RequestState wrapper
    val totalValuesFromModel: State<RequestState<TotalValues?>> =
        viewModel.totalValues.collectAsState()

    // we need to get the person's TotalValues from database as well
    val success = totalValuesFromModel.value as RequestState.Success<*>
    val totalValues = success.data as TotalValues?

    val quantityValueText = rememberSaveable {
        mutableStateOf("")
    }

    val costValueText = rememberSaveable {
        mutableStateOf("")
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onPopBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },
                title = {
                    selectedCryptoValue.value?.coin?.coinName?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colors.topAppBarContentColor
                        )
                    }
                },

                actions = {
                    DetailActions(
                        onEditClicked = {
                            scope.launch {
                                if (sheetState.isCollapsed) {
                                    sheetState.expand()
                                } else {
                                    sheetState.collapse()
                                }
                            }
                        }
                    )
                },

                backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
            )
        },

        content = {
            it.calculateTopPadding()
            LazyColumn(
                modifier = Modifier
                    .scrollable(
                        state = scrollState,
                        orientation = Orientation.Vertical
                    )
                    .padding(10.dp, 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)

            ) {

                item {
                    Card(
                        elevation = 6.dp,
                        border = BorderStroke(0.3.dp, MaterialTheme.colors.cardBorderColor),
                        backgroundColor = MaterialTheme.colors.cardBackgroundColor,
                        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)

                    ) {

                        Column(
                            modifier = Modifier.padding(10.dp, 10.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            DetailRow(
                                cryptoValue = selectedCryptoValue.value,
                                null,
                                RowType.COIN_NAME)

                            DetailRow(
                                cryptoValue = selectedCryptoValue.value,
                                null,
                                rowType = RowType.COIN_PRICE)

                            DetailRow(
                                cryptoValue = selectedCryptoValue.value,
                                null,
                                rowType = RowType.QUANTITY_HELD)

                            DetailRow(
                                cryptoValue = selectedCryptoValue.value,
                                null,
                                rowType = RowType.HOLDING_VALUE)

                            DetailRow(
                                cryptoValue = selectedCryptoValue.value,
                                null,
                                rowType = RowType.YOUR_COST)

                            DetailRow(
                                cryptoValue = selectedCryptoValue.value,
                                null,
                                rowType = RowType.INCREASE)

                            Spacer(modifier = Modifier.height(6.dp))

                        }

                    }// card end
                }

                item {
                    Card(
                        elevation = 6.dp,
                        border = BorderStroke(0.3.dp, MaterialTheme.colors.cardBorderColor),
                        backgroundColor = MaterialTheme.colors.cardBackgroundColor,
                        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)

                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp, 10.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp, 5.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    modifier = Modifier.padding(2.dp),
                                    text = stringResource(id = com.imtmobileapps.R.string.total_portfolio_holdings),
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colors.coinNameTextColor,
                                    style = MaterialTheme.typography.subtitle1,
                                )
                            }

                            DetailRow(
                                cryptoValue = null,
                                totalValues,
                                rowType = RowType.TOTAL_VALUE)

                            DetailRow(
                                cryptoValue = null,
                                totalValues,
                                rowType = RowType.TOTAL_COST)

                            DetailRow(
                                cryptoValue = null,
                                totalValues,
                                rowType = RowType.TOTAL_INCREASE)

                            Spacer(modifier = Modifier.height(6.dp))

                        }
                    }// 2nd card end
                }// item 2 end

                item {
                    Card(
                        elevation = 6.dp,
                        border = BorderStroke(0.3.dp, MaterialTheme.colors.cardBorderColor),
                        backgroundColor = MaterialTheme.colors.cardBackgroundColor,
                        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)

                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp, 10.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp, 5.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {

                                val newsTitle =
                                    selectedCryptoValue.value?.coin?.coinName.toString() + " " + "NEWS"
                                Text(
                                    modifier = Modifier.padding(2.dp),
                                    text = newsTitle,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colors.coinNameTextColor,
                                    style = MaterialTheme.typography.subtitle1,
                                )
                            }
                        }
                    }// third card end
                } // item 3 end

            }// main column end
        },// content end
        sheetBackgroundColor = MaterialTheme.colors.background,
        sheetContent = {
            EditCoin(
                coin = selectedCryptoValue.value?.coin,
                cryptoValue = selectedCryptoValue.value,
                sheetType = SheetType.CRYPTO_VALUE,
                onQuantityChanged = { q ->
                    quantityValueText.value = removeWhiteSpace(q)
                },
                costValueText = costValueText.value,
                quantityValueText = quantityValueText.value
            )
        },
        sheetPeekHeight = 0.dp,
        sheetElevation = 6.dp,
        sheetShape = RoundedCornerShape(8.dp)
    ) // scaffold end

}

@Composable
fun DetailActions(
    onEditClicked: () -> Unit,
) {
    EditAction {
        onEditClicked()
    }

}

@Composable
fun EditAction(
    onEditClicked: () -> Unit,
) {

    IconButton(onClick = {
        onEditClicked()
    }) {
        Icon(
            Icons.Filled.Edit,
            contentDescription = "Edit",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }

}
