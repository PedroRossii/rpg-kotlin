package classe

import com.example.rpg_front.model.classe.ClassePersonagem

open class Clerigo : ClassePersonagem(
    nome = "Clérigo",
    descricao = "Servo devoto das divindades, conjurador de magias divinas."
) {
    override fun habilidadesDeClasse(): List<String> {
        return listOf(
            "Pode usar apenas armas impactantes",
            "Pode usar todas as armaduras",
            "Pode usar itens mágicos ordeiros",
            "Pode conjurar magias divinas",
            "Afastar Mortos-Vivos",
            "Cura Milagrosa (substitui magias por cura)"
        )
    }
}
