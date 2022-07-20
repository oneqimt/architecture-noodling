package com.imtmobileapps.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.imtmobileapps.ui.theme.cardBackgroundColor

@Composable
fun EmptyScreen(

) {
    Card(
        Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.cardBackgroundColor
    ) {

    }

}