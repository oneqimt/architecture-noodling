package com.imtmobileapps.view.holding

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtmobileapps.data.CryptoRepository
import com.imtmobileapps.model.Coin
import com.imtmobileapps.model.CryptoValue
import com.imtmobileapps.util.DataType
import com.imtmobileapps.util.RequestState
import com.imtmobileapps.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import logcat.LogPriority
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class HoldingViewModel @Inject constructor(
    private val repository: CryptoRepository,
) : ViewModel() {

    private val _searchState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchState: State<SearchAppBarState> = _searchState

    private val _searchTextState: MutableState<String> = mutableStateOf("")
    val searchTextState: State<String> = _searchTextState

    private val _dataType: MutableState<String> = mutableStateOf(DataType.NONE)
    val dataType: State<String> = _dataType

    private val _allCoins =
        MutableStateFlow<RequestState<MutableList<Coin>>>(RequestState.Idle)
    val allCoins: StateFlow<RequestState<MutableList<Coin>>> = _allCoins.asStateFlow()

    private val _filteredCoins =
        MutableStateFlow<RequestState<MutableList<Coin>>>(RequestState.Idle)
    val filteredCoins: StateFlow<RequestState<MutableList<Coin>>> = _filteredCoins.asStateFlow()

    private var searchCoinList: MutableList<Coin> = mutableListOf()

    private val _selectedCoin: MutableStateFlow<Coin?> = MutableStateFlow(null)
    val selectedCoin: StateFlow<Coin?> = _selectedCoin.asStateFlow()

    private val _selectedCryptoValue: MutableStateFlow<CryptoValue?> =
        MutableStateFlow(value = null)
    val selectedCryptoValue: StateFlow<CryptoValue?> = _selectedCryptoValue.asStateFlow()

    init {
        fetchAllCoinsFromRemote()
    }

    fun setSelectedCoin(coin: Coin) {
        _selectedCoin.value = coin
    }

    fun updateSearchState(newVal: SearchAppBarState) {
        _searchState.value = newVal
    }

    fun updateSearchTextState(newVal: String) {
        _searchTextState.value = newVal
    }

    fun filterListForSearch() {

        _dataType.value = DataType.FILTERED_COINS
        val allCoinsList = (allCoins.value as RequestState.Success).data
        println("$TAG and searchTextState is ${searchTextState.value}")
        // clear the lists
        _filteredCoins.value = RequestState.Success(mutableStateListOf())
        searchCoinList.clear()

        allCoinsList.let {
            for (coin in it.listIterator()) {
                if (coin.coinName?.startsWith(searchTextState.value, true) == true) {
                    searchCoinList.add(coin)
                }
            }
            val temp = RequestState.Success(searchCoinList.toMutableStateList())
            _filteredCoins.value = RequestState.Success(temp).data
        }
    }

    fun fetchAllCoinsFromRemote() {
        _dataType.value = DataType.ALL_COINS
        _allCoins.value = RequestState.Loading
        _filteredCoins.value = RequestState.Loading

        try {
            viewModelScope.launch {
                repository.getAllCoins().collect {
                    _allCoins.value = RequestState.Success(it).data
                    _filteredCoins.value = RequestState.Success(it).data
                }
            }
        } catch (e: Exception) {
            _allCoins.value = RequestState.Error(e)
            _filteredCoins.value = RequestState.Error(e)
        }
    }

    // Calls the DATABASE to get the corresponding CryptoValue object.
    fun getSelectedCryptoValue(coinName: String) {
        try {
            viewModelScope.launch {
                repository.getCoin(coinName).collect {
                    _selectedCryptoValue.value = it
                    logcat(TAG) { "selectedCryptoValue is: ${selectedCryptoValue.value}" }
                }
            }
        } catch (e: Exception) {
            logcat(TAG, LogPriority.ERROR) { e.localizedMessage as String }
        }
    }


    companion object {
        private val TAG = HoldingViewModel::class.java.simpleName
    }


}