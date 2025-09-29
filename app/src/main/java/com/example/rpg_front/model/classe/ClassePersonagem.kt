package com.example.rpg_front.model.classe

open class ClassePersonagem(
    val nome: String,
    val descricao: String
) {
    open fun habilidadesDeClasse(): List<String> {
        return emptyList()
    }
}
