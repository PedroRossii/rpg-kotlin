package com.example.rpg_front.model.classe

import classe.Clerigo

class Druida : Clerigo() {
    override fun habilidadesDeClasse(): List<String> {
        return super.habilidadesDeClasse() + listOf(
            "Deve ter alinhamento neutro",
            "Não pode usar armas ou armaduras metálicas",
            "Perde acesso a Afastar Mortos-Vivos e Cura Milagrosa",
            "Magias Divinas da natureza",
            "Herbalismo: identificar plantas, animais e fontes de água potável"
        )
    }
}
