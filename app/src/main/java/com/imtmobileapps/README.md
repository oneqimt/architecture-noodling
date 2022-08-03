

Square logcat
https://github.com/square/logcat

Application file onCreate():
Log all priorities in debug builds, no-op in release builds.
AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = VERBOSE)

// UNUSED methods PortfolioListViewModel

private fun getCachedPerson(id : Int){
_personCached.value = RequestState.Loading
viewModelScope.launch {
try{
val p = repository.getPerson(id)
_personCached.value = RequestState.Success(p)
logcat(TAG){"Cached Person from DB is ${_personCached.value}"}

            }catch (e: Exception){
                _personCached.value = RequestState.Error(e)
                logcat(TAG){"Error getting cached person ${e.localizedMessage}"}
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
println("${PortfolioListViewModel.TAG} in searchDatabase and coins are : $coin")
}

        } catch (e: Exception) {
            _searchedCoins.value = RequestState.Error(e)
            logcat(PortfolioListViewModel.TAG, LogPriority.ERROR) { e.localizedMessage as String }
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
if( it != -1){
getCachedPerson(it)
}

                }
            }catch (e: Exception){
                logcat(TAG) { "ERROR getting person from DB : ${e.localizedMessage}" }
            }
        }
    }


