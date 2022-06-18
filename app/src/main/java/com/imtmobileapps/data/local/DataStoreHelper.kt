package com.imtmobileapps.data.local

import com.imtmobileapps.util.CoinSort

interface DataStoreHelper {

    suspend fun savePersonId(personId: Int)

    suspend fun saveUpdateTime(time: Long)

    suspend fun saveCacheDuration(cacheDuration : String)

    suspend fun saveSortState(coinSort: CoinSort)
}