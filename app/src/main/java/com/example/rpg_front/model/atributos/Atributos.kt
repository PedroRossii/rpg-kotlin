package com.example.rpg_front.model.atributos

import kotlin.random.Random

data class Atributos(
    var forca: Int,
    var destreza: Int,
    var constituicao: Int,
    var inteligencia: Int,
    var sabedoria: Int,
    var carisma: Int
) {
    companion object {
        private fun d6() = Random.nextInt(1, 7)

        // Clássico: 3d6 em ordem
        fun gerarClassico(): Atributos {
            return Atributos(
                (1..3).sumOf { d6() },
                (1..3).sumOf { d6() },
                (1..3).sumOf { d6() },
                (1..3).sumOf { d6() },
                (1..3).sumOf { d6() },
                (1..3).sumOf { d6() }
            )
        }

        // Aventureiro: 3d6 distribuídos pelo jogador
        fun gerarAventureiro(): Atributos {
            val valores = List(6) { (1..3).sumOf { d6() } }
            return Atributos(
                valores[0],
                valores[1],
                valores[2],
                valores[3],
                valores[4],
                valores[5]
            )
        }

        // Heróico: 4d6, descarta o menor
        private fun rolarHeroico(): Int {
            val dados = List(4) { d6() }.sortedDescending()
            return dados.take(3).sum()
        }

        fun gerarHeroico(): Atributos {
            val valores = List(6) { rolarHeroico() }
            return Atributos(
                valores[0],
                valores[1],
                valores[2],
                valores[3],
                valores[4],
                valores[5]
            )
        }

        fun gerarValoresAventureiro(): List<Int> {
            return List(6) { (1..3).sumOf { d6() } }
        }

        fun gerarValoresHeroico(): List<Int> {
            return List(6) { rolarHeroico() }
        }
    }
}
