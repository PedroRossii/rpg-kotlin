package com.example.rpg_front.model.raca

class Humano : Raca(
    nome = "Humano",
    movimento = 9,
    infravisao = 0,
    alinhamento = "Qualquer",
    habilidades = listOf("Adaptabilidade", "Aprendizado rápido")
)
