package com.imtmobileapps.data.local

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalTypeConverter {
    @TypeConverter
    fun bigDecimalToDouble(input: BigDecimal?): Double {
        return input?.toDouble() ?: 0.0
    }

    @TypeConverter
    fun stringToBigDecimal(input: Double?): BigDecimal {
        if (input == null) return BigDecimal.ZERO
        return BigDecimal.valueOf(input) ?: BigDecimal.ZERO
    }
}