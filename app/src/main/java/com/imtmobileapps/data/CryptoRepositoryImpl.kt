package com.imtmobileapps.data

import com.imtmobileapps.data.local.LocalDataSource
import com.imtmobileapps.data.remote.RemoteDataSource
import com.imtmobileapps.model.*
import com.imtmobileapps.util.CoinSort
import com.imtmobileapps.util.Constants.CMC_LOGO_URL
import com.imtmobileapps.util.DataSource
import com.imtmobileapps.util.RequestState
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class CryptoRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,

    ) : CryptoRepository {

    val personId : Flow<Int> = localDataSource.getPersonId()

    override fun resetPassword(email: String): Flow<ReturnDTO> {
        return flow {
            val returnDTO = remoteDataSource.resetPassword(email)
            emit(returnDTO)
        }
    }

    override fun addHolding(coinHolding: CoinHolding): Flow<Holdings> {
        return flow {
            val holdingAdd = remoteDataSource.addHolding(coinHolding)
            emit(holdingAdd)
        }
    }

    override fun deleteHolding(holdings: Holdings): Flow<Holdings> {
        return flow {
            val holdingsDelete = remoteDataSource.deleteHolding(holdings)
            emit(holdingsDelete)
        }
    }

    override fun updateHolding(coinHolding: CoinHolding): Flow<Holdings> {
        return flow {
            val holdingsUpdate = remoteDataSource.updateHolding(coinHolding)
            emit(holdingsUpdate)
        }
    }

    override fun login(uname: String, pass: String): Flow<SignUp> {
        return flow {
            val signUp = remoteDataSource.login(uname, pass)
            emit(signUp)
        }
    }

    override suspend fun logout(): Boolean {
        return remoteDataSource.logout()
    }

    override fun signUp(signUp: SignUp): Flow<RequestState<SignUp>> {
        return flow {
            val signUpObj = remoteDataSource.signUp(signUp = signUp)
            emit(RequestState.Success(signUpObj))
        }
    }

    override suspend fun savePerson(person: Person): Long {
        return localDataSource.savePerson(person)
    }

    override suspend fun getPerson(personId: Int): Person {
        return localDataSource.getPerson(personId)
    }

    override suspend fun deletePerson() {
        localDataSource.deletePerson()
    }

    override fun updatePersonRemote(person: Person): Flow<Person> {
        return flow {
            val personRemote = remoteDataSource.updatePerson(person)
            emit(personRemote)
        }
    }

    override fun getStates(): Flow<List<State>> {
       return flow {
           val states = remoteDataSource.getStates()
           emit(states)
       }
    }

    override fun getPersonCoins(
        personId: Int,
        dataSource: DataSource,
    ): Flow<RequestState<List<CryptoValue>>> {
        return flow {

            when (dataSource) {
                DataSource.REMOTE -> {
                    val personCoins = remoteDataSource.getPersonCoins(personId)
                    personCoins.map {
                        val logo = CMC_LOGO_URL + it.coin.cmcId + ".png"
                        it.coin.smallCoinImageUrl = logo
                        it.coin.largeCoinImageUrl = logo
                    }
                    emit(RequestState.Success(personCoins))
                }
                DataSource.LOCAL -> {
                    val personCoinsFromDatabase =
                        localDataSource.getPersonCoins()

                    emit(RequestState.Success(personCoinsFromDatabase))
                }
            }
        }
    }


    override fun getAllCoins(): Flow<RequestState<MutableList<Coin>>> {
        return flow {
            val allCoins = remoteDataSource.getAllCoins()

            allCoins.map {
                val logo = CMC_LOGO_URL + it.cmcId + ".png"
                it.smallCoinImageUrl = logo
                it.largeCoinImageUrl = logo
            }

            emit(RequestState.Success(allCoins))
        }
    }

    override fun getCoin(coinName: String): Flow<CryptoValue> {
        return flow {
            val cryptoValue = localDataSource.getCoin(coinName = coinName)
            emit(cryptoValue)
        }
    }

    override fun getTotalValues(personId: Int): Flow<RequestState<TotalValues>> {
        return flow {

            val totalValues = remoteDataSource.getTotalValues(personId)

            emit(RequestState.Success(totalValues))
        }
    }

    override fun searchDatabase(searchQuery: String): Flow<List<CryptoValue>> {
        return flow {
            val searchResult = localDataSource.searchDatabase(searchQuery = searchQuery)

            emit(RequestState.Success(searchResult).data)
        }
    }

    override fun insertAll(list: List<CryptoValue>): Flow<List<Long>> {
        return flow {

            val insertResult = localDataSource.insertAll(*list.toTypedArray())

            emit(RequestState.Success(insertResult).data)
        }
    }

    override suspend fun insertTotalValues(totalValues: TotalValues): Long {
        return localDataSource.insertTotalValues(totalValues)
    }

    override suspend fun deleteAllCoins() {
        localDataSource.deleteAllCoins()
    }

    override suspend fun savePersonId(personId: Int) {
        localDataSource.savePersonId(personId)
    }

    override suspend fun getCurrentPersonId(): Flow<Int> {
        return localDataSource.getPersonId()
    }

    override suspend fun deleteTotalValues() {
        localDataSource.deleteTotalValues()
    }

    override suspend fun saveSortState(sortState: CoinSort) {
        localDataSource.saveSortState(sortState)
    }

    override fun getSortState(): Flow<String> {
        return localDataSource.getSortState()
    }

    override suspend fun saveUpdateTime(updateTime: Long) {
        localDataSource.saveUpdateTime(updateTime)
    }

    override fun getUpdateTime(): Flow<Any> {
        return localDataSource.getUpdateTime()
    }

    override suspend fun saveCacheDuration(cacheDuration: String) {
        localDataSource.saveCacheDuration(cacheDuration)
    }

    override fun getCacheDuration(): Flow<String> {
        return localDataSource.getCacheDuration()
    }

    override fun getChartData(ids: String): Flow<List<GeckoCoin>> {
        return flow {
            val chartData = remoteDataSource.getChartData(ids)
            emit(chartData)
        }
    }
}