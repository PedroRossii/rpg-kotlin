package com.example.rpg_front.model.personagem


import com.example.rpg_front.model.atributos.Atributos
import com.example.rpg_front.model.raca.Raca
import classe.*
import com.example.rpg_front.model.classe.Academico
import com.example.rpg_front.model.classe.Barbaro
import com.example.rpg_front.model.classe.ClassePersonagem
import com.example.rpg_front.model.classe.Druida
import com.example.rpg_front.model.classe.Guerreiro
import com.example.rpg_front.model.classe.Ilusionista
import com.example.rpg_front.model.classe.Mago
import com.example.rpg_front.model.classe.Necromante
import com.example.rpg_front.model.classe.Paladino

class Personagem(
    val nome: String,
    val atributos: Atributos,
    val raca: Raca,
    val classe: ClassePersonagem
) {

    var pontosDeVida: Int = calcularPV()
    var classeDeArmadura: Int = calcularCA()
    var baseDeAtaque: Int = calcularBA()
    var jogadaDeProtecao: Int = calcularJP()

    private fun calcularPV(classeInstancia: ClassePersonagem = this.classe): Int {
        return when (classeInstancia) {
            is Guerreiro, is Paladino, is Barbaro -> 10 + modificadorConstituicao()
            is Clerigo, is Druida, is Academico -> 8 + modificadorConstituicao()
            is Mago, is Necromante, is Ilusionista -> 4 + modificadorConstituicao()
            else -> 0
        }
    }

    private fun calcularCA(classeInstancia: ClassePersonagem = this.classe): Int {
        val bonusDestreza = (atributos.destreza - 10) / 2
        return 10 + bonusDestreza
    }

    private fun calcularBA(classeInstancia: ClassePersonagem = this.classe): Int {
        return when (classeInstancia) {
            is Guerreiro, is Paladino, is Barbaro -> 1
            is Clerigo, is Druida, is Academico -> 1
            is Mago, is Necromante, is Ilusionista -> 0
            else -> 0
        }
    }

    private fun calcularJP(classeInstancia: ClassePersonagem = this.classe): Int {
        return when (classeInstancia) {
            is Guerreiro, is Paladino, is Barbaro -> 16
            is Clerigo, is Druida, is Academico -> 15
            is Mago, is Necromante, is Ilusionista -> 13
            else -> 10
        }
    }

    private fun modificadorConstituicao(): Int {
        return (atributos.constituicao - 10) / 2
    }

    private fun modificadorDestreza(): Int {
        return (atributos.destreza - 10) / 2
    }

    private fun modificadorForca(): Int {
        return (atributos.forca - 10) / 2
    }


    fun exibirFicha() {
        println("===== FICHA DE PERSONAGEM =====")
        println("Nome: $nome")
        println("Raça: ${raca.nome} | Classe: ${classe.nome}")
        println("Movimento: ${raca.movimento}m | Infravisão: ${raca.infravisao}m")
        println("Alinhamento: ${raca.alinhamento}")
        println("Atributos: FOR=${atributos.forca}, DES=${atributos.destreza}, CON=${atributos.constituicao}, " +
                "INT=${atributos.inteligencia}, SAB=${atributos.sabedoria}, CAR=${atributos.carisma}")
        println("PV=${this.pontosDeVida}, CA=${this.classeDeArmadura}, BA=${this.baseDeAtaque}," +
                "JP=${this.jogadaDeProtecao}")
        println("Habilidades Raciais: ${raca.habilidades.joinToString()}")
        println("Descrição da Classe: ${classe.descricao}")
        println("Habilidades da Classe/Subclasse: ${classe.habilidadesDeClasse().joinToString()}")
        println("================================\n")
    }
}
