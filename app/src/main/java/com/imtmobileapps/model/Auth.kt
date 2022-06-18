package com.imtmobileapps.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Auth(
    val auth_id: Int?,
    var username: String,
    var password: String,
    val person_id: Int?,
    val role:String?,
    val enabled: Int?

):Parcelable{
    override fun toString(): String {
        return "Auth(auth_id=$auth_id, username='$username', password='$password', person_id=$person_id, role=$role, enabled=$enabled)"
    }
}
