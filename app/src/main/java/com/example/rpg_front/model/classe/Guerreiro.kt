package com.example.rpg_front.model.classe

open class Guerreiro : ClassePersonagem(
    nome = "Guerreiro",
    descricao = "Combatente especializado em armas e armaduras, sempre na linha de frente."
) {
    override fun habilidadesDeClasse(): List<String> {
        return listOf(
            "Pode usar todas as armas",
            "Pode usar todas as armaduras",
            "Não pode usar cajados, varinhas e pergaminhos mágicos (exceto pergaminhos de proteção)",
            "Aparar: sacrificar arma ou escudo para anular dano",
            "Maestria em Arma: bônus em dano com armas escolhidas",
            "Ataque Extra a partir do 6º nível"
        )
    }
}
