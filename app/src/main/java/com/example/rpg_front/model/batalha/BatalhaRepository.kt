package com.example.rpg_front.model.batalha

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Objeto compartilhado que guarda o estado da batalha em tempo real.
 * O Service escreve aqui, a Activity lê daqui.
 */
object BatalhaRepository {
    // Lista de mensagens de log (o texto que vai aparecer na tela)
    private val _logs = MutableStateFlow<List<String>>(emptyList())
    val logs = _logs.asStateFlow()

    // Estado da batalha (Rodando, Vitória, Derrota)
    private val _estadoBatalha = MutableStateFlow(EstadoBatalha.PARADO)
    val estadoBatalha = _estadoBatalha.asStateFlow()

    fun adicionarLog(mensagem: String) {
        val listaAtual = _logs.value.toMutableList()
        listaAtual.add(mensagem)
        _logs.value = listaAtual
    }

    fun iniciarBatalha() {
        _logs.value = emptyList()
        _estadoBatalha.value = EstadoBatalha.RODANDO
    }

    fun finalizarBatalha(vitoria: Boolean) {
        _estadoBatalha.value = if (vitoria) EstadoBatalha.VITORIA else EstadoBatalha.DERROTA
    }
}

enum class EstadoBatalha {
    PARADO, RODANDO, VITORIA, DERROTA
}