package com.imtmobileapps.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.imtmobileapps.model.CryptoValue

@Dao
interface CryptoValuesDao {

    @Insert
    suspend fun insertAll(vararg coins: CryptoValue) : List<Long>

    @Query(value = "SELECT * FROM cryptovalue")
    suspend fun getPersonCoins(): List<CryptoValue>

    @Query(value = "SELECT * FROM cryptovalue WHERE coin_name = :coinName")
    suspend fun getCoin(coinName: String):CryptoValue

    @Query(value = "DELETE FROM cryptovalue")
    suspend fun deleteAllCoins()

    @Query("SELECT * FROM cryptovalue WHERE cryptovalue.coin_name LIKE :searchQuery OR cryptovalue.coin_symbol LIKE :searchQuery")
    suspend fun searchDatabase(searchQuery: String): List<CryptoValue>

}