package com.imtmobileapps.data.remote

import com.imtmobileapps.model.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val cryptoApi: CryptoApi,
    private val geckoApi: GeckoApi
){
    suspend fun getPersonCoins(personId : Int): List<CryptoValue> {
        return cryptoApi.getPersonCoins(personId)
    }

    suspend fun getTotalValues(personId: Int) : TotalValues {
        return cryptoApi.getTotals(personId)
    }

    suspend fun getAllCoins(): MutableList<Coin>{
        return cryptoApi.getAllCoins()
    }

    suspend fun login(uname: String, pass: String): SignUp {
        return cryptoApi.login(uname = uname, pass = pass)
    }

    suspend fun logout():Boolean{
        return cryptoApi.logout()
    }

    suspend fun signUp(signUp: SignUp): SignUp{
        return cryptoApi.signUp(signUp)
    }

    fun resetPassword(email: String) : ReturnDTO{
        return cryptoApi.resetPassword(email)
    }

    fun addHolding(coinHolding: CoinHolding): Holdings{
        return cryptoApi.addHolding(coinHolding)
    }

    fun deleteHolding(holdings: Holdings) : Holdings{
        return cryptoApi.deleteHolding(holdings)
    }

    fun updateHolding(coinHolding: CoinHolding): Holdings{
        return cryptoApi.updateHolding(coinHolding)
    }

    suspend fun updatePerson(person: Person): Person {
        return cryptoApi.updatePerson(person)
    }

    suspend fun getChartData(ids: String): List<GeckoCoin>{
        return geckoApi.getChartData(ids)

    }
}