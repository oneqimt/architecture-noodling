package com.imtmobileapps.view.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtmobileapps.data.CryptoRepository
import com.imtmobileapps.model.Person
import com.imtmobileapps.model.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: CryptoRepository
):ViewModel() {

    private var _personCached: MutableStateFlow<Person?> = MutableStateFlow(null)
    var personCached: StateFlow<Person?> = _personCached.asStateFlow()

    private var _personId : MutableStateFlow<Int> = MutableStateFlow(0)
    var personId : StateFlow<Int> = _personId.asStateFlow()

    private var _states = MutableStateFlow<List<State>>(emptyList())
    var states : StateFlow<List<State>> = _states.asStateFlow()

    init {
        getCachedPersonId()
        getStates()
    }

    private fun getStates(){
        viewModelScope.launch {
            try {
                repository.getStates().collect{
                    _states.value = it
                }
            }catch (e: Exception){
                logcat(TAG){"Error getting states ${e.localizedMessage}"}
            }
        }
    }

    private fun getCachedPersonId(){
        viewModelScope.launch {
            try {
                // get the id first
                repository.getCurrentPersonId().collect{
                    _personId.value = it
                    getCachedPerson()
                }
            }catch (e: Exception){
                logcat(TAG) { "ERROR getting person from DB : ${e.localizedMessage}" }
            }
        }
    }

    private fun getCachedPerson(){
        logcat(TAG){"getCachedPerson()"}
        viewModelScope.launch {
           _personCached.value =  repository.getPerson(_personId.value)
            logcat(TAG){"_personCached is ${_personCached.value}"}
        }
    }

    fun updatePerson(person: Person) {
        viewModelScope.launch {
            try {
                // update person on server
                repository.updatePersonRemote(person).collect {
                    _personCached.value = it
                    // now, update person in database
                    repository.updatePersonLocal(person)

                    val dbPerson = repository.getPerson(it.personId)

                   logcat(TAG) { "UPDATED LOCAL person is : $dbPerson" }
                   logcat(TAG) { "UPDATED REMOTE person is : ${_personCached.value}" }
                }
            } catch (e: Exception) {
                logcat(TAG) { "ERROR updating person is : ${_personCached.value}" }
            }
        }
    }

    companion object{
        private val TAG = AccountViewModel::class.java.simpleName
    }


}