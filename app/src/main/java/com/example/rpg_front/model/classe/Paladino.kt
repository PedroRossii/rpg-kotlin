package com.example.rpg_front.model.classe

class Paladino : Guerreiro() {
    override fun habilidadesDeClasse(): List<String> {
        return super.habilidadesDeClasse() + listOf(
            "Deve ter alinhamento ordeiro",
            "Aura de coragem: aliados próximos recebem bônus em testes de moral",
            "Pode curar ferimentos leves uma vez por dia",
            "Afastar Mortos-Vivos como um Clérigo de nível inferior"
        )
    }
}
