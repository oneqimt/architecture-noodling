package com.imtmobileapps.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.imtmobileapps.util.CoinSort
import com.imtmobileapps.util.Constants.CACHE_DURATION_KEY
import com.imtmobileapps.util.Constants.PERSON_ID_KEY
import com.imtmobileapps.util.Constants.PREFERENCE_NAME
import com.imtmobileapps.util.Constants.SORT_STATE_KEY
import com.imtmobileapps.util.Constants.UPDATE_TIME_KEY
import kotlinx.coroutines.flow.map


val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name= PREFERENCE_NAME)

class DataStoreHelperImpl(private val context: Context):DataStoreHelper {

    private object PreferenceKeys {
        val sortState = stringPreferencesKey(name = SORT_STATE_KEY)
        val personId = intPreferencesKey(name = PERSON_ID_KEY)
        val updateTime = longPreferencesKey(name = UPDATE_TIME_KEY)
        val cacheDuration = stringPreferencesKey(name = CACHE_DURATION_KEY)
    }
    // This returns a Flow<Int>
    val personId = context.dataStore.data.map {
        it[PreferenceKeys.personId] ?: 0
    }

    val updateTime = context.dataStore.data.map {
        it[PreferenceKeys.updateTime] ?: ""
    }

    val cacheDuration = context.dataStore.data.map {
        it[PreferenceKeys.cacheDuration] ?: ""
    }

    val sortState = context.dataStore.data.map {
        it[PreferenceKeys.sortState] ?: ""
    }

    override suspend fun savePersonId(personId: Int) {
        context.dataStore.edit {
            it[PreferenceKeys.personId] = personId
        }
    }

    override suspend fun saveUpdateTime(time: Long) {
        context.dataStore.edit {
            it[PreferenceKeys.updateTime] = time
        }
    }

    override suspend fun saveCacheDuration(cacheDuration: String) {
        context.dataStore.edit {
            it[PreferenceKeys.cacheDuration] = cacheDuration
        }
    }

    override suspend fun saveSortState(coinSort: CoinSort) {
        context.dataStore.edit {
            it[PreferenceKeys.sortState] = coinSort.name
        }
    }
}