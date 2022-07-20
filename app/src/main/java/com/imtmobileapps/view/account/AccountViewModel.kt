package com.imtmobileapps.view.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtmobileapps.data.CryptoRepository
import com.imtmobileapps.model.Person
import com.imtmobileapps.model.State
import com.imtmobileapps.util.RequestState
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

    private var _personCached = MutableStateFlow<RequestState<Person>>(RequestState.Idle)
    var personCached: StateFlow<RequestState<Person>> = _personCached.asStateFlow()

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
                    logcat(TAG){"Cached PersonId is $it"}
                    getCachedPerson()
                }
            }catch (e: Exception){
                logcat(TAG) { "ERROR getting person from DB : ${e.localizedMessage}" }
            }
        }
    }

    private fun getCachedPerson(){
        _personCached.value = RequestState.Loading
        viewModelScope.launch {
            try{
                //val person = (cachedPerson.value as RequestState.Success<Person>).data
                val p = repository.getPerson(personId.value)
                _personCached.value = RequestState.Success(p)
                logcat(TAG){"Cached Person from DB is ${_personCached.value}"}

            }catch (e: Exception){
                _personCached.value = RequestState.Error(e)
                logcat(TAG){"Error getting cached person ${e.localizedMessage}"}
            }

        }
    }

    fun updatePerson(person: Person) {
        viewModelScope.launch {
            _personCached.value = RequestState.Loading
            try {
                // update person on server
                repository.updatePersonRemote(person).collect {
                   _personCached.value = RequestState.Success(it)
                    logcat(TAG){"updatePersonRemote and person is $it"}
                    // now, update person in database
                  // repository.updatePersonLocal(person)
                 //  val dbPerson = repository.getPerson(person.personId)

                  // logcat(TAG) { "UPDATED LOCAL person is : $dbPerson" }
                  // logcat(TAG) { "UPDATED REMOTE person is : ${_personCached.value}" }
                }
            } catch (e: Exception) {
                _personCached.value = RequestState.Error(e)
                logcat(TAG) { "ERROR updating person is : ${_personCached.value}" }
            }
        }
    }

    companion object{
        private val TAG = AccountViewModel::class.java.simpleName
    }


}