package com.example.rpg_front.model.classe

open class Mago : ClassePersonagem(
    nome = "Mago",
    descricao = "Usuário das artes arcanas, mestre de grimórios."
) {
    override fun habilidadesDeClasse(): List<String> {
        return listOf(
            "Pode usar apenas armas pequenas",
            "Não pode usar armaduras ou escudos",
            "Pode usar todos os tipos de itens mágicos",
            "Magias Arcanas diárias",
            "Necessita de grimório para memorizar magias",
            "Magias Iniciais no grimório",
            "Ler Magias (decifrar inscrições arcanas)",
            "Detectar Magias"
        )
    }
}
