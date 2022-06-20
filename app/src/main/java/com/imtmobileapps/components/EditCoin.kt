package com.imtmobileapps.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imtmobileapps.R
import com.imtmobileapps.model.Coin
import com.imtmobileapps.model.CryptoValue
import com.imtmobileapps.ui.theme.cardBackgroundColor
import com.imtmobileapps.ui.theme.cardBorderColor
import com.imtmobileapps.ui.theme.coinNameTextColor
import com.imtmobileapps.util.Constants.EDIT_COIN_TAG
import com.imtmobileapps.util.SheetType
import com.imtmobileapps.util.getDummyCoin
import com.imtmobileapps.util.getDummyCryptoValue
import logcat.logcat

@ExperimentalMaterialApi
@Composable
fun EditCoin(
    coin: Coin?,
    cryptoValue: CryptoValue?,
    sheetType: SheetType,
    quantityValueText: String,
    costValueText: String,
    onQuantityChanged: (String) -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
            .padding(10.dp),
        elevation = 4.dp,
        border = BorderStroke(0.1.dp, MaterialTheme.colors.cardBorderColor),
        backgroundColor = MaterialTheme.colors.background,
        shape = RoundedCornerShape(corner = CornerSize(6.dp))
    ){
        val focusManager = LocalFocusManager.current

        Column(modifier =
        Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState()))
        {

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                when(sheetType){
                    SheetType.COIN ->{

                        coin?.coinName?.let {
                            Text(
                                modifier = Modifier.padding(2.dp),
                                text = it,
                                color = MaterialTheme.colors.coinNameTextColor,
                                style = MaterialTheme.typography.h6,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                    }
                    SheetType.CRYPTO_VALUE ->{
                        cryptoValue?.coin?.let { coin ->
                            coin.coinName?.let {
                                Text(
                                    modifier = Modifier.padding(2.dp),
                                    text = coin.coinName,
                                    color = MaterialTheme.colors.coinNameTextColor,
                                    style = MaterialTheme.typography.h6,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                ) }
                        }
                    }
                }
            }// 1st row
            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                TextField(

                    value = quantityValueText,
                    label = { Text(text = stringResource(id = R.string.quantity)) },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),

                    onValueChange = {
                        logcat(EDIT_COIN_TAG){" value changed is $it"}
                        onQuantityChanged(it)
                    },
                )

            }

        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun EditCoinPreview(){
    EditCoin(
        coin = getDummyCoin(1),
        cryptoValue = getDummyCryptoValue(),
        sheetType = SheetType.COIN,
        quantityValueText = "3.0",
        costValueText = "234.56",
        onQuantityChanged = {}

    )
}