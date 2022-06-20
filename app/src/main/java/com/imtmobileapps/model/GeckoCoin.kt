package com.imtmobileapps.model

import com.google.gson.annotations.SerializedName

data class GeckoCoin(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    @SerializedName("current_price")
    val currentPrice: Double,
    @SerializedName("market_cap")
    val marketCap: Long,
    @SerializedName("market_cap_rank")
    val marketCapRank: Long,
    @SerializedName("fully_diluted_valuation")
    val fullyDilutedValuation: Long,
    @SerializedName("total_volume")
    val totalVolume: Long,
    @SerializedName("high_24h")
    val high24H: Double,
    @SerializedName("low_24h")
    val low24H: Double,
    @SerializedName("price_change_24h")
    val priceChange24H: Double,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24H: Double,
    @SerializedName("market_cap_change_24h")
    val marketCapChange24H: Double,
    @SerializedName("market_cap_change_percentage_24h")
    val marketCapChangePercentage24H: Double,
    @SerializedName("circulating_supply")
    val circulatingSupply: Double,
    @SerializedName("total_supply")
    val totalSupply: Long,
    @SerializedName("max_supply")
    val maxSupply: Long,
    val ath: Double,
    @SerializedName("ath_change_percentage")
    val athChangePercentage: Double,
    @SerializedName("ath_date")
    val athDate: String,
    val atl: Double,
    @SerializedName("atl_change_percentage")
    val atlChangePercentage: Double,
    @SerializedName("atl_date")
    val atlDate: String,
    val roi: Roi,
    @SerializedName("last_updated")
    val lastUpdated: String,
    @SerializedName("sparkline_in_7d")
    val sparklineIn7D: SparklineIn7D

)

data class Roi (
    val times: Double,
    val currency: String,
    val percentage: Double
)

data class SparklineIn7D (
    val price: List<Double>
)
