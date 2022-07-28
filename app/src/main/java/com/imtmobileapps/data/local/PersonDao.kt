package com.imtmobileapps.data.local

import androidx.room.*
import com.imtmobileapps.model.Person
import com.imtmobileapps.util.RequestState
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePerson(person: Person):Long

    @Query(value = "SELECT * FROM person WHERE person_id = :personId")
    suspend fun getPerson(personId: Int): Person

    @Query(value = "DELETE FROM person")
    suspend fun deletePerson()
}