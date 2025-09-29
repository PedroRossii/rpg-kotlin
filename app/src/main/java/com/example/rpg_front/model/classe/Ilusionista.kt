package com.example.rpg_front.model.classe

class Ilusionista : Mago() {
    override fun habilidadesDeClasse(): List<String> {
        return super.habilidadesDeClasse() + listOf(
            "Especialista em ilusões e manipulação da realidade",
            "Pode criar imagens, sons e disfarces ilusórios",
            "Restrições específicas de magias arcanas"
        )
    }
}
