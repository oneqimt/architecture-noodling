package com.imtmobileapps.data.local

import androidx.room.*
import com.imtmobileapps.model.Person

@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePerson(person: Person):Long

    @Update
    suspend fun updatePerson(person: Person):Int

    @Query(value = "SELECT * FROM person WHERE person_id = :personId")
    suspend fun getPerson(personId: Int): Person

    @Query(value = "DELETE FROM person")
    suspend fun deletePerson()
}