package com.imtmobileapps.data

import com.imtmobileapps.model.*
import com.imtmobileapps.util.CoinSort
import com.imtmobileapps.util.DataSource
import com.imtmobileapps.util.RequestState
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {
    fun login(uname: String, pass: String): Flow<SignUp>
    suspend fun logout() : Boolean

    fun signUp(signUp: SignUp): Flow<RequestState<SignUp>>

    suspend fun savePerson(person: Person): Long
    suspend fun getPerson(personId: Int): Person
    suspend fun deletePerson()
    fun updatePersonRemote(person: Person): Flow<Person>
    suspend fun updatePersonLocal(person: Person): Int

    fun resetPassword(email: String): Flow<ReturnDTO>

    fun addHolding(coinHolding: CoinHolding): Flow<Holdings>
    fun deleteHolding(holdings: Holdings): Flow<Holdings>
    fun updateHolding(coinHolding: CoinHolding): Flow<Holdings>

    fun getPersonCoins(personId : Int, dataSource: DataSource): Flow<RequestState<List<CryptoValue>>>
    fun getChartData(ids: String): Flow<List<GeckoCoin>>

    fun getAllCoins(): Flow<RequestState<MutableList<Coin>>>

    fun getCoin(coinName : String): Flow<CryptoValue>

    fun getTotalValues(personId: Int): Flow<RequestState<TotalValues>>

    fun searchDatabase(searchQuery: String): Flow<List<CryptoValue>>

    fun insertAll(list : List<CryptoValue>) : Flow<List<Long>>

    suspend fun insertTotalValues(totalValues: TotalValues): Long

    suspend fun deleteAllCoins()

    suspend fun savePersonId(personId: Int)

    suspend fun getCurrentPersonId() : Flow<Int>

    suspend fun deleteTotalValues()

    suspend fun saveSortState(sortState: CoinSort)

    fun getSortState(): Flow<String>

    suspend fun saveUpdateTime(updateTime: Long)

    fun getUpdateTime(): Flow<Any>

    suspend fun saveCacheDuration(cacheDuration: String)

    fun getCacheDuration(): Flow<String>
}