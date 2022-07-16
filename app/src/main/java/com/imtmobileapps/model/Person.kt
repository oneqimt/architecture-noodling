package com.imtmobileapps.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Person(

    @SerializedName("personId")
    @ColumnInfo(name = "person_id")
    val personId: Int,

    @SerializedName("firstName")
    @ColumnInfo(name = "first_name")
    val firstName: String?,

    @SerializedName("lastName")
    @ColumnInfo(name = "last_name")
    val lastName: String?,

    var email: String?,

    val phone: String?,

    val address: String?,

    val city: String?,

    val zip: String?,

    @Embedded
    val state:State?



):Parcelable{
    @PrimaryKey(autoGenerate = true)
    @kotlinx.parcelize.IgnoredOnParcel
    var personuuid : Int = 0

    override fun toString(): String {
        return "Person(personId=$personId, firstName=$firstName, lastName=$lastName, email=$email, phone=$phone, address=$address, city=$city, zip=$zip, state=$state, personuuid=$personuuid)"
    }
}
