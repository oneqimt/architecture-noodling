package com.imtmobileapps.view.portfoliolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtmobileapps.data.CryptoRepository
import com.imtmobileapps.model.CryptoValue
import com.imtmobileapps.model.Person
import com.imtmobileapps.model.TotalValues
import com.imtmobileapps.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import logcat.LogPriority
import logcat.logcat
import javax.inject.Inject


@HiltViewModel
class PortfolioListViewModel @Inject constructor(
    private val repository: CryptoRepository,
) : ViewModel() {

    private val _portfolioCoins =
        MutableStateFlow<RequestState<List<CryptoValue>>>(RequestState.Idle)
    val portfolioCoins: StateFlow<RequestState<List<CryptoValue>>> = _portfolioCoins.asStateFlow()

    private val _totalValues = MutableStateFlow<RequestState<TotalValues?>>(RequestState.Idle)
    val totalValues: StateFlow<RequestState<TotalValues?>> = _totalValues.asStateFlow()

    private val _personId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val personId: StateFlow<Int> = _personId.asStateFlow()

    private var _person: MutableStateFlow<Person?> = MutableStateFlow(null)
    var person: StateFlow<Person?> = _person.asStateFlow()

    private val _selectedCryptoValue: MutableStateFlow<CryptoValue?> = MutableStateFlow(null)
    val selectedCryptoValue: StateFlow<CryptoValue?> = _selectedCryptoValue.asStateFlow()

    private val _isLoggedIn = MutableStateFlow<RequestState<Boolean>>(RequestState.Idle)
    val isLoggedIn: StateFlow<RequestState<Boolean>> = _isLoggedIn.asStateFlow()

    //SORT
    private val _sortState =
        MutableStateFlow<RequestState<CoinSort>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<CoinSort>> = _sortState.asStateFlow()

    // SEARCH
    private val _searchedCoins =
        MutableStateFlow<RequestState<List<CryptoValue>>>(RequestState.Idle)
    private val searchedCoins: StateFlow<RequestState<List<CryptoValue>>> =
        _searchedCoins.asStateFlow()

    fun setSelectedCryptoValue(cryptoValue: CryptoValue) {
        _selectedCryptoValue.value = cryptoValue
    }

    private var loginJob: Job? = null

    fun login(uname: String, pass: String) {
        if (loginJob != null){
            return
        }
        loginJob = viewModelScope.launch {
            try {
                _isLoggedIn.update {
                    RequestState.Loading
                }
                delay(500L)
                repository.login(uname, pass).collect { signUp ->
                    signUp.person.let { person ->
                        _person.value = person
                        _personId.value = person.personId
                        // save personId to dataStore
                        repository.savePersonId(person.personId)
                        // clear person table first
                        repository.deletePerson()
                        // now save the person to the database
                        val result: Long = repository.savePerson(person)
                        person.personuuid = result.toInt()
                        logcat(TAG) { "_person is : ${_person.value}" }
                        _isLoggedIn.update {
                            RequestState.Success(true)
                        }
                        logcat(TAG) { "isLoggedIn is : ${isLoggedIn.value}" }

                        fetchUserDataFromRemote()

                    }
                }
            } catch (e: Exception) {
                logcat(TAG) { "LOGIN Error ${e.localizedMessage}" }
                // There was an error, set to false
                _isLoggedIn.update {
                    RequestState.Error(e)
                }
            }finally {
                loginJob = null
            }
        }
    }

    fun logout() {
        _isLoggedIn.value = RequestState.Loading
        viewModelScope.launch {
            try {
                val loggedOut = repository.logout()
                logcat(TAG) { "LOGOUT and loggedOut is : $loggedOut" }
                // clear all existing values
                _portfolioCoins.value = RequestState.Success(mutableListOf())
                _person.value = null
                _personId.value = -1 // on view model
                _totalValues.value = RequestState.Success(getDummyTotalsValue())
                _searchedCoins.value = RequestState.Success(mutableListOf())
                clearDatabase()
                clearPersonId() // datastore
                // NOTE: The sensitive file is deleted in PortfolioList
                _isLoggedIn.value = RequestState.LoggedOut
                logcat(TAG) { "isLoggedIn is : ${isLoggedIn.value}" }

            } catch (e: Exception) {
                logcat(TAG, LogPriority.ERROR) { e.localizedMessage as String }
            }
        }

    }

    private fun fetchUserDataFromRemote() {
        _portfolioCoins.value = RequestState.Loading
        viewModelScope.launch {
            // Trigger repository requests in parallel
            try {
                val coinsDeferred = async {
                    repository.getPersonCoins(personId = personId.value, DataSource.REMOTE)
                }
                RequestState.Success(coinsDeferred.await().collect {
                    _portfolioCoins.value = it
                })
                val portfolioCoinsList =
                    (portfolioCoins.value as RequestState.Success<List<CryptoValue>>).data
                // SET A DEFAULT SORT on the LIST
                val sortedlist = sortCryptoValueList(portfolioCoinsList, CoinSort.NAME)
                _portfolioCoins.value = RequestState.Success(sortedlist)
                // Call deleteAllCoins on database first (prices vary tremendously)
                deleteAllCoins()
                storeCoinsInDatabase(portfolioCoinsList)
                // initial sort state
                saveSortState(CoinSort.NAME)

            } catch (e: Exception) {
                logcat(TAG) { "Error getting person coins from remote ${e.localizedMessage}" }
                _portfolioCoins.value = RequestState.Error(e)
            }

            try {
                val totalsDeferred = async {
                    repository.getTotalValues(personId = personId.value)
                }
                RequestState.Success(totalsDeferred.await().collect {
                    _totalValues.value = it
                })
                // Delete existing TotalValues
                deleteTotalValues()

                val totalsToStore = (totalValues.value as RequestState.Success<*>).data

                logcat(TAG) { "TotalValues that are stored in DB $totalsToStore" }
                storeTotalValuesInDatabase(totalsToStore as TotalValues)

            } catch (e: Exception) {
                logcat(TAG) { "Error getting total values from remote ${e.localizedMessage}" }
                _totalValues.value = RequestState.Error(e)

            }
        }
    }

    //DATABASE
    private fun clearDatabase() {
        viewModelScope.launch {
            try {
                repository.deleteAllCoins()
                repository.deletePerson()
            } catch (e: Exception) {
                logcat(TAG, LogPriority.ERROR) { e.localizedMessage as String }
            }
        }
    }

    private fun storeCoinsInDatabase(list: List<CryptoValue>) {
        viewModelScope.launch {
            try {
                repository.insertAll(list).collect {
                    println("$TAG INSERT ALL and result is $it")
                }
            } catch (e: Exception) {
                logcat(TAG, LogPriority.ERROR) { e.localizedMessage as String }
            }
        }
    }

    private fun storeTotalValuesInDatabase(totalValues: TotalValues) {
        viewModelScope.launch {
            try {
                repository.insertTotalValues(totalValues)
            } catch (e: Exception) {
                logcat(TAG, LogPriority.ERROR) { e.localizedMessage as String }
            }
        }
    }

    private fun deleteTotalValues() {
        viewModelScope.launch {
            try {
                repository.deleteTotalValues()
            } catch (e: Exception) {
                logcat(TAG, LogPriority.ERROR) { e.localizedMessage as String }
            }
        }
    }

    fun searchDatabase(searchQuery: String) {
        _searchedCoins.value = RequestState.Loading
        viewModelScope.launch {
            try {
                repository.searchDatabase(searchQuery = "%$searchQuery%")
                    .collect {
                        _searchedCoins.value = RequestState.Success(it)
                        val coin =
                            (searchedCoins.value as RequestState.Success<List<CryptoValue>>).data
                        println("$TAG in searchDatabase and coins are : $coin")
                    }

            } catch (e: Exception) {
                _searchedCoins.value = RequestState.Error(e)
                logcat(TAG, LogPriority.ERROR) { e.localizedMessage as String }
            }
        }
    }

    /****************** SORT ****************/
    fun saveSortState(coinSort: CoinSort) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveSortState(coinSort)
            println("$TAG saving sort state : ${coinSort.name}")
        }

    }

    fun getSortState() {
        _sortState.value = RequestState.Loading
        viewModelScope.launch {
            try {
                repository.getSortState().map {
                    CoinSort.valueOf(it)
                }.collect {
                    _sortState.value = RequestState.Success(it)
                    val sortStateCache = (RequestState.Success(it).data)
                    val portfolioCoinsList =
                        (portfolioCoins.value as RequestState.Success<List<CryptoValue>>).data
                    val sortedlist = sortCryptoValueList(portfolioCoinsList, sortStateCache)
                    _portfolioCoins.value = RequestState.Success(sortedlist)
                }
            } catch (e: Exception) {
                _sortState.value = RequestState.Error(e)
                logcat(TAG, LogPriority.ERROR) { e.localizedMessage as String }
            }
        }

    }


    private fun fetchTotalValuesFromDatabase() {
        _totalValues.value = RequestState.Loading
        viewModelScope.launch {
            try {
                repository.getTotalValues(1).collect {
                    _totalValues.value = RequestState.Success(it).data
                }
            } catch (e: Exception) {
                logcat(TAG, LogPriority.ERROR) { e.localizedMessage as String }
            }
        }
    }

    // DATASTORE
    private fun clearPersonId() {
        viewModelScope.launch {
            try {
                repository.savePersonId(-1)
            } catch (e: Exception) {
                logcat(TAG, LogPriority.ERROR) { e.localizedMessage as String }
            }
        }
    }

    private fun deleteAllCoins() {
        viewModelScope.launch {
            try {
                repository.deleteAllCoins()
            } catch (e: Exception) {
                logcat(TAG, LogPriority.ERROR) { e.localizedMessage as String }
            }
        }
    }

    companion object {
        private val TAG = PortfolioListViewModel::class.java.simpleName
    }
}