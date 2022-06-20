package com.imtmobileapps.data.remote

import com.imtmobileapps.model.GeckoCoin
import retrofit2.http.GET
import retrofit2.http.Query

interface GeckoApi {

    // COIN GECKO
    // JUST 1 COIN
    // https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=cardano&order=market_cap_desc&per_page=100&page=1&sparkline=true
    // https://www.coingecko.com/en/api/documentation

    @GET("markets?vs_currency=usd&ids=&order=market_cap_desc&per_page=100&page=1&sparkline=true")
    suspend fun getChartData(@Query("ids") ids: String) : List<GeckoCoin>
}