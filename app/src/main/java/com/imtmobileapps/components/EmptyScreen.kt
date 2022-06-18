package com.imtmobileapps.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.imtmobileapps.ui.theme.cardBackgroundColor
import com.imtmobileapps.ui.theme.cardBorderColor
import com.imtmobileapps.util.Constants.EMPTY_SCREEN_TAG
import com.imtmobileapps.util.Routes
import com.imtmobileapps.view.portfoliolist.PortfolioListViewModel
import logcat.logcat

@Composable
fun EmptyScreen(

) {
    Card(
        Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.cardBackgroundColor
    ) {

    }

}