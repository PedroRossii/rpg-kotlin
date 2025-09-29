package com.example.rpg_front.model.raca

class Elfo : Raca(
    nome = "Elfo",
    movimento = 9,
    infravisao = 18,
    alinhamento = "Neutro",
    habilidades = listOf("Percepção Natural", "Gracioso", "Arma Racial", "Imunidade")
)
