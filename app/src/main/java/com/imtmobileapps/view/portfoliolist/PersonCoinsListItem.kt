package com.imtmobileapps.view.portfoliolist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.imtmobileapps.model.CryptoValue
import com.imtmobileapps.ui.theme.*
import com.imtmobileapps.util.getDummyCryptoValue
import com.imtmobileapps.R

@ExperimentalMaterialApi
@Composable
fun PersonCoinsListItem(
    cryptoValue: CryptoValue,
    onCardClicked: () -> Unit,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),

        elevation = 2.dp,
        border = BorderStroke(0.3.dp, MaterialTheme.colors.cardBorderColor),
        backgroundColor = MaterialTheme.colors.cardBackgroundColor,
        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
        onClick = {
            onCardClicked()
        },
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            cryptoValue.coin.apply {

                Column(
                    modifier = Modifier.padding(10.dp)

                ) {
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                    ) {
                        Text(
                            text = cmcRank.toString(),
                            color = MaterialTheme.colors.coinNameTextColor,
                            style = MaterialTheme.typography.caption,
                            maxLines = 1,

                            )

                        Image(
                            painter = rememberAsyncImagePainter(smallCoinImageUrl),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )

                    }

                }
                Column(
                    modifier = Modifier
                        .padding(6.dp)

                ) {
                    if (coinName != null) {
                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = coinName,
                            color = MaterialTheme.colors.coinNameTextColor,
                            style = MaterialTheme.typography.subtitle1,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (coinSymbol != null) {
                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = coinSymbol,
                            color = MaterialTheme.colors.coinSymbolTextColor,
                            style = MaterialTheme.typography.subtitle2,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                }

            }

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.End

            ) {
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = stringResource(id = R.string.current_price),
                    color = MaterialTheme.colors.staticTextColor,
                    style = MaterialTheme.typography.caption
                )

                cryptoValue.coin.apply {
                    Text(
                        modifier = Modifier.padding(2.dp),
                        text = currentPrice.toString(),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.staticTextColor,
                        style = MaterialTheme.typography.caption,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        modifier = Modifier.padding(2.dp),
                        text = stringResource(id = R.string.holding_value),
                        color = MaterialTheme.colors.staticTextColor,
                        style = MaterialTheme.typography.caption
                    )

                    cryptoValue.apply {
                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = holdingValue,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.staticTextColor,
                            style = MaterialTheme.typography.caption,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }//end

}

@ExperimentalMaterialApi
@Composable
@Preview
private fun PersonCoinsListItemPreview() {
    PersonCoinsListItem(cryptoValue = getDummyCryptoValue(), onCardClicked = {})

}