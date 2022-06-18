package com.imtmobileapps.view.portfoliodetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.imtmobileapps.R
import com.imtmobileapps.model.CryptoValue
import com.imtmobileapps.model.TotalValues
import com.imtmobileapps.ui.theme.coinNameTextColor
import com.imtmobileapps.util.RowType

@Composable
fun DetailRow(
    cryptoValue: CryptoValue?,
    totalValues: TotalValues?,
    rowType: RowType,
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 0.dp)

    ) {

        when (rowType) {

            RowType.COIN_NAME -> {
                Column() {
                    Image(
                        painter = rememberAsyncImagePainter(cryptoValue?.coin?.smallCoinImageUrl),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Column() {
                    if (cryptoValue?.coin?.coinName != null) {
                        TextRowBold(textValue = cryptoValue.coin.coinName)
                    }
                }

                Column() {
                    if (cryptoValue?.coin?.coinSymbol != null) {
                        TextRowBold(textValue = cryptoValue.coin.coinSymbol)
                    }
                }
            }
            RowType.COIN_PRICE -> {
                Column() {
                    TextRow(textValue = stringResource(id = R.string.current_price))
                }

                Column() {
                    if (cryptoValue != null) {
                        TextRow(textValue = cryptoValue.coin.currentPrice.toString())
                    }
                }
            }

            RowType.QUANTITY_HELD -> {
                Column() {
                    TextRow(textValue = "Quantity Held:")
                }
                Column() {

                    if (cryptoValue != null) {
                        TextRow(textValue = cryptoValue.quantity.toString())
                    }
                }
            }

            RowType.HOLDING_VALUE -> {
                Column() {
                    TextRow(textValue = stringResource(id = R.string.holding_value))
                }
                Column() {
                    if (cryptoValue != null) {
                        TextRow(textValue = cryptoValue.holdingValue)
                    }
                }
            }

            RowType.YOUR_COST -> {
                Column() {
                    TextRow(textValue = stringResource(id = R.string.your_cost))
                }
                Column() {
                    if (cryptoValue != null) {
                        TextRow(textValue = cryptoValue.cost)
                    }
                }
            }

            RowType.INCREASE -> {
                Column() {
                    if (cryptoValue != null) {
                        TextRow(textValue = cryptoValue.increaseDecrease)
                    }
                }
                Column() {

                    if (cryptoValue != null) {
                        TextRow(textValue = cryptoValue.percentage)
                    }
                }
            }

            RowType.TOTAL_VALUE -> {
                Column() {
                    TextRow(textValue = stringResource(id = R.string.total_value))
                }
                Column() {
                    if (totalValues != null) {
                        TextRow(textValue = totalValues.totalValue.toString())
                    }
                }
            }

            RowType.TOTAL_COST -> {
                Column() {
                    TextRow(textValue = stringResource(id = R.string.total_cost))
                }
                Column() {
                    if (totalValues != null) {
                        TextRow(textValue = totalValues.totalCost.toString())
                    }
                }
            }

            RowType.TOTAL_INCREASE -> {
                Column() {
                    if (totalValues != null) {
                        TextRow(textValue = totalValues.increaseDecrease.toString())
                    }
                }
                Column() {
                    if (totalValues != null) {
                        TextRow(textValue = totalValues.totalChange.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun TextRow(textValue: String) {

    return Text(modifier = Modifier.padding(2.dp),
        text = textValue,
        color = MaterialTheme.colors.coinNameTextColor,
        style = MaterialTheme.typography.subtitle1,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )

}

@Composable
fun TextRowBold(textValue: String) {
    return Text(modifier = Modifier.padding(2.dp),
        text = textValue,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.coinNameTextColor,
        style = MaterialTheme.typography.subtitle1,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}
