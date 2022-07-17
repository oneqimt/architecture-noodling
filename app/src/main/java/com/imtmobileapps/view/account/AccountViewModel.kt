package com.imtmobileapps.view.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtmobileapps.data.CryptoRepository
import com.imtmobileapps.model.Person
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

    private var _personAcct: MutableStateFlow<Person?> = MutableStateFlow<Person?>(null)
    var personAcct: StateFlow<Person?> = _personAcct.asStateFlow()

    fun updatePerson(person: Person) {
        viewModelScope.launch {
            try {
                // update person on server
                repository.updatePersonRemote(person).collect {
                    _personAcct.value = it
                    // now, update person in database
                    repository.updatePersonLocal(person)

                    val dbPerson = repository.getPerson(it.personId)

                   logcat(TAG) { "UPDATED LOCAL person is : $dbPerson" }
                   logcat(TAG) { "UPDATED REMOTE person is : ${_personAcct.value}" }
                }
            } catch (e: Exception) {
               _personAcct.value = null
                logcat(TAG) { "ERROR updating person is : ${_personAcct.value}" }
            }
        }
    }

    companion object{
        private val TAG = AccountViewModel::class.java.simpleName
    }


}