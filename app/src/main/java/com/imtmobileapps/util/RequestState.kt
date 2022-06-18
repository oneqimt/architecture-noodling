package com.imtmobileapps.util

sealed class RequestState<out R>{

    object Idle: RequestState<Nothing>()
    object Loading: RequestState<Nothing>()
    object LoggedOut: RequestState<Nothing>()
    data class Success<out T>(val data: T) : RequestState<T>()
    data class Error(val exception: Exception) : RequestState<Nothing>()

}
