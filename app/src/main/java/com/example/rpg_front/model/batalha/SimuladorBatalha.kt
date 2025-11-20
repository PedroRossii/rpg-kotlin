package com.example.rpg_front.model.batalha

import com.example.rpg_front.model.personagem.Personagem
import kotlin.math.max
import kotlin.random.Random

class SimuladorBatalha {

    // Agora a função recebe um callback 'onLog' para enviar mensagens em tempo real
    fun simular(heroi: Personagem, inimigo: Inimigo, onLog: (String) -> Unit): ResultadoBatalha {
        onLog("⚔️ Iniciando combate: ${heroi.nome} (PV: ${heroi.pontosDeVida}) vs ${inimigo.nome} (PV: ${inimigo.pontosDeVida})")

        // Pequena pausa dramática
        esperar(1000)

        var rodada = 1

        while (heroi.pontosDeVida > 0 && inimigo.pontosDeVida > 0) {
            onLog("\n--- Rodada $rodada ---")

            val atributoTeste = max(heroi.atributos.destreza, heroi.atributos.sabedoria)
            val rolagemIniciativa = d20()

            // Lógica de Iniciativa
            if (rolagemIniciativa <= atributoTeste) {
                turnoHeroi(heroi, inimigo, onLog)
                if (inimigo.pontosDeVida > 0) {
                    turnoInimigo(heroi, inimigo, onLog)
                }
            } else {
                turnoInimigo(heroi, inimigo, onLog)
                if (heroi.pontosDeVida > 0) {
                    turnoHeroi(heroi, inimigo, onLog)
                }
            }

            rodada++
            // Pausa entre rodadas para dar tempo de ler
            esperar(1500)
        }

        val vitoria = heroi.pontosDeVida > 0
        val msgFinal = if (vitoria) "🏆 VITÓRIA! ${heroi.nome} venceu!" else "💀 DERROTA... ${heroi.nome} caiu."
        onLog("\n" + msgFinal)

        return ResultadoBatalha(vitoria, heroi.pontosDeVida, "")
    }

    private fun turnoHeroi(heroi: Personagem, inimigo: Inimigo, onLog: (String) -> Unit) {
        val rolagem = d20()
        val modForca = (heroi.atributos.forca - 10) / 2
        val bonusAtaque = heroi.baseDeAtaque + modForca
        val totalAtaque = rolagem + bonusAtaque

        // Dano base simples
        val danoBase = Random.nextInt(1, 9)

        if (rolagem == 20) {
            val dano = max(1, (danoBase + modForca) * 2)
            inimigo.pontosDeVida -= dano
            onLog("💥 CRÍTICO! Você causou $dano de dano no ${inimigo.nome}!")
        } else if (rolagem == 1) {
            onLog("💨 Falha Crítica! Você tropeçou e errou.")
        } else if (totalAtaque >= inimigo.classeDeArmadura) {
            val dano = max(1, danoBase + modForca)
            inimigo.pontosDeVida -= dano
            onLog("⚔️ Acertou! (Roll $rolagem + $bonusAtaque = $totalAtaque). Dano: $dano. (Inimigo HP: ${inimigo.pontosDeVida})")
        } else {
            onLog("🛡️ Errou! (Roll $rolagem + $bonusAtaque = $totalAtaque vs CA ${inimigo.classeDeArmadura}).")
        }
        esperar(500) // Pausa entre ataques
    }

    private fun turnoInimigo(heroi: Personagem, inimigo: Inimigo, onLog: (String) -> Unit) {
        val rolagem = d20()
        val totalAtaque = rolagem + inimigo.baseDeAtaque

        if (rolagem == 20) {
            val dano = max(1, Random.nextInt(inimigo.danoMin, inimigo.danoMax + 1) * 2)
            heroi.pontosDeVida -= dano
            onLog("🩸 Inimigo CRITOU em você! Dano: $dano. (Seu HP: ${heroi.pontosDeVida})")
        } else if (rolagem == 1) {
            onLog("🤣 O ${inimigo.nome} errou feio!")
        } else if (totalAtaque >= heroi.classeDeArmadura) {
            val dano = max(1, Random.nextInt(inimigo.danoMin, inimigo.danoMax + 1))
            heroi.pontosDeVida -= dano
            onLog("🗡️ Inimigo te acertou. Dano: $dano. (Seu HP: ${heroi.pontosDeVida})")
        } else {
            onLog("🛡️ Você desviou do ataque do ${inimigo.nome}.")
        }
        esperar(500)
    }

    private fun d20() = Random.nextInt(1, 21)

    private fun esperar(ms: Long) {
        try { Thread.sleep(ms) } catch (e: Exception) {}
    }
}

data class ResultadoBatalha(val vitoria: Boolean, val pvRestante: Int, val log: String)