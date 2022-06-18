package com.imtmobileapps.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class TotalValues(

    @ColumnInfo(name = "person_id")
    val personId: Int?,

    @ColumnInfo(name = "total_cost")
    val totalCost: String?,

    @ColumnInfo(name = "total_value")
    val totalValue: String?,

    @SerializedName("totalPercentageIncreaseDecrease")
    @ColumnInfo(name = "total_change")
    val totalChange: String?,

    @ColumnInfo(name = "increase_decrease")
    val increaseDecrease: String?


):Parcelable{
    @PrimaryKey(autoGenerate = true)
    @IgnoredOnParcel
    var uuid: Int = 0

    override fun toString(): String {
        return "TotalValues(personId=$personId, totalCost=$totalCost, totalValue=$totalValue, totalChange=$totalChange, increaseDecrease=$increaseDecrease, uuid=$uuid)"
    }
}
