package com.example.rpg_front.controller

import classe.Clerigo
import com.example.rpg_front.model.atributos.Atributos
import com.example.rpg_front.model.personagem.Personagem
import com.example.rpg_front.model.classe.*
import com.example.rpg_front.model.raca.*

class PersonagemController {

    fun criarPersonagem(
        nome: String,
        atributos: Atributos,
        raca: String,
        classe: String
    ): Personagem {
        val racaEscolhida = when (raca) {
            "Humano" -> Humano()
            "Elfo" -> Elfo()
            "Anão" -> Anao()
            "Halfling" -> Halfling()
            else -> Humano()
        }

        val classeEscolhida = when (classe) {
            "Guerreiro" -> Guerreiro()
            "Clérigo" -> Clerigo()
            "Mago" -> Mago()
            "Paladino" -> Paladino()
            "Bárbaro" -> Barbaro()
            "Druida" -> Druida()
            "Acadêmico" -> Academico()
            "Ilusionista" -> Ilusionista()
            "Necromante" -> Necromante()
            else -> Guerreiro()
        }

        return Personagem(nome, atributos, racaEscolhida, classeEscolhida)
    }
}
