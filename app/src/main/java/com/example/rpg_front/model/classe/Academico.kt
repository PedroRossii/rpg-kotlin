package com.example.rpg_front.model.classe

import classe.Clerigo

class Academico : Clerigo() {
    override fun habilidadesDeClasse(): List<String> {
        return super.habilidadesDeClasse() + listOf(
            "Estudioso da teologia e filosofia sagrada",
            "Pode identificar rituais e escrituras antigas",
            "Magias Divinas reforçadas em estudos religiosos"
        )
    }
}
