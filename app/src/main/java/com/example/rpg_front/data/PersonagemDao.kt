package com.example.rpg_front.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonagemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(personagem: PersonagemEntity)

    @Query("SELECT * FROM personagens ORDER BY nome ASC")
    fun getAll(): Flow<List<PersonagemEntity>>

    @Query("SELECT * FROM personagens WHERE id = :id")
    fun getById(id: Long): Flow<PersonagemEntity?>
}