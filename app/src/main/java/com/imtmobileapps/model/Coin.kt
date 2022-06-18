package com.imtmobileapps.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Entity
@Parcelize
data class Coin(

    @SerializedName("coin_id")
    @ColumnInfo(name = "coin_id")
    val coinId: Int? = null,

    @SerializedName("coin_name")
    @ColumnInfo(name = "coin_name")
    val coinName: String? = null,

    @SerializedName("coin_symbol")
    @ColumnInfo(name = "coin_symbol")
    val coinSymbol: String? = null,

    @SerializedName("cmc_id")
    @ColumnInfo(name = "cmc_id")
    val cmcId : Int? = null,

    @SerializedName("slug")
    @ColumnInfo(name = "slug")
    val slug : String? = null,

    // These 2 attributes were added based upon a different API used at the server level
    // I decided to leave them in anyway, but we have to set these manually
    @SerializedName("small_coin_image_url")
    @ColumnInfo(name = "small_coin_image_url")
    var smallCoinImageUrl: String? = null,

    @SerializedName("large_coin_image_url")
    @ColumnInfo(name = "large_coin_image_url")
    var largeCoinImageUrl: String? = null,
    @SerializedName("market_cap")
    @ColumnInfo(name = "market_cap")
    val marketCap: BigDecimal? = null,

    @SerializedName("cmc_rank")
    @ColumnInfo(name = "cmc_rank")
    val cmcRank : Int = 0,

    @SerializedName("currentPrice")
    @ColumnInfo(name = "current_price")
    val currentPrice : BigDecimal? = null

):Parcelable{
    // no need for primary key here
    // Room adds columns for this object to CryptoValue
    override fun toString(): String {
        return "Coin(coinId=$coinId, coinName=$coinName, coinSymbol=$coinSymbol, cmcId=$cmcId, slug=$slug, smallCoinImageUrl=$smallCoinImageUrl, largeCoinImageUrl=$largeCoinImageUrl, marketCap=$marketCap, cmcRank=$cmcRank, currentPrice=$currentPrice)"
    }
}
