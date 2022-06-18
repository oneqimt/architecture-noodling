package com.imtmobileapps.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class State(
    val id:Int?=null,
    val name:String?=null,
    val country:String?=null,
    val abbreviation:String?=null

):Parcelable{
    override fun toString(): String {
        return "State(id=$id, name=$name, country=$country, abbreviation=$abbreviation)"
    }
}
