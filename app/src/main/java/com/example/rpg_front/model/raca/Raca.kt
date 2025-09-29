package com.example.rpg_front.model.raca

abstract class Raca(
    val nome: String,
    val movimento: Int,
    val infravisao: Int,
    val alinhamento: String,
    val habilidades: List<String>
)
