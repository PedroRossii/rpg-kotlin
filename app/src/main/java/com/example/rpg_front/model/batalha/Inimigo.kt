package com.example.rpg_front.model.batalha

data class Inimigo(
    val nome: String,
    var pontosDeVida: Int,
    val classeDeArmadura: Int,
    val baseDeAtaque: Int,
    val danoMin: Int,
    val danoMax: Int
) {
    companion object {
        fun gerarInimigoAleatorio(): Inimigo {
            // Exemplo simples de inimigo baseado nas regras
            return Inimigo(
                nome = "Goblin Saqueador",
                pontosDeVida = 7, // Média de 1d8+...
                classeDeArmadura = 12, // Armadura leve
                baseDeAtaque = 1,
                danoMin = 1,
                danoMax = 6 // Dano de espada curta (1d6)
            )
        }
    }
}