package com.example.rpg_front.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rpg_front.model.atributos.Atributos

@Entity(tableName = "personagens")
data class PersonagemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val nomeRaca: String,
    val nomeClasse: String,
    @Embedded
    val atributos: Atributos
)