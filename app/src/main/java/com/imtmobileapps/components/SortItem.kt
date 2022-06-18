package com.imtmobileapps.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.imtmobileapps.util.CoinSort
import androidx.compose.material.Text
import com.imtmobileapps.ui.theme.LARGE_PADDING
import com.imtmobileapps.ui.theme.Typography

@Composable
fun SortItem(coinSort: CoinSort){

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(LARGE_PADDING),
            text = coinSort.name,
            style = Typography.subtitle2,
            color = MaterialTheme.colors.onSurface

        )
    }
}