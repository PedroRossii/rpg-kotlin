package com.example.rpg_front.model.classe

class Barbaro : Guerreiro() {
    override fun habilidadesDeClasse(): List<String> {
        return super.habilidadesDeClasse() + listOf(
            "Vigor Bárbaro: +2 PV por nível e +2 em Jogadas de Proteção contra Constituição",
            "Não pode usar itens mágicos nem armaduras metálicas",
            "Talentos Selvagens: escalar superfícies naturais, rastrear, sobreviver nos ermos"
        )
    }
}
