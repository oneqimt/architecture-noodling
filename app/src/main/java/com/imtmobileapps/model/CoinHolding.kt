package com.imtmobileapps.model

data class CoinHolding(
    val coin : Coin? = null,
    val holdings: Holdings? = null

){
    override fun toString(): String {
        return "CoinHolding(coin=$coin, holdings=$holdings)"
    }
}
