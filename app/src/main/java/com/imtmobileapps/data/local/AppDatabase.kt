package com.imtmobileapps.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.imtmobileapps.model.CryptoValue
import com.imtmobileapps.model.Person
import com.imtmobileapps.model.TotalValues

@Database(entities = [CryptoValue::class, Person::class, TotalValues::class], version = 3, exportSchema = false)
@TypeConverters(BigDecimalTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun personDao() : PersonDao
    abstract fun cryptoValueDao() : CryptoValuesDao
    abstract fun totalValuesDao() : TotalValuesDao

    //SINGLETON
    companion object {
        @Volatile private var instance : AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also{
                instance = it
            }

        }
        // fallbackToDestructiveMigration(),
        // just means there is no migration policy, so delete and build, which loses all data.
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, AppDatabase::class.java, "architecture_database"
        ).fallbackToDestructiveMigration().build()
    }


}