package com.example.rpg_front.model.classe

class Necromante : Mago() {
    override fun habilidadesDeClasse(): List<String> {
        return super.habilidadesDeClasse() + listOf(
            "Especialista em artes sombrias e necromancia",
            "Pode conjurar magias exclusivas como Aterrorizar e Criar Mortos-Vivos",
            "Perde acesso a magias de círculos elevados (7º ao 9º)"
        )
    }
}
