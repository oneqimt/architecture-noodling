package com.imtmobileapps.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.imtmobileapps.model.Person

@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePerson(person: Person):Long

    @Query(value = "SELECT * FROM person WHERE person_id = :personId")
    suspend fun getPerson(personId: Int): Person

    @Query(value = "DELETE FROM person")
    suspend fun deletePerson()
}