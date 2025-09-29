package com.example.rpg_front.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rpg_front.controller.PersonagemController
import com.example.rpg_front.model.atributos.Atributos
import com.example.rpg_front.model.personagem.Personagem

class FichaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nome = intent.getStringExtra("nome") ?: "Sem Nome"
        val raca = intent.getStringExtra("raca") ?: "Humano"
        val classe = intent.getStringExtra("classe") ?: "Guerreiro"

        val atributos = Atributos(
            intent.getIntExtra("for", 10),
            intent.getIntExtra("des", 10),
            intent.getIntExtra("con", 10),
            intent.getIntExtra("int", 10),
            intent.getIntExtra("sab", 10),
            intent.getIntExtra("car", 10),
        )

        val personagem = PersonagemController().criarPersonagem(nome, atributos, raca, classe)

        setContent {
            FichaScreen(
                p = personagem,
                onCriarOutro = {
                    val intent = Intent(this, CriacaoActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun FichaScreen(
    p: Personagem,
    onCriarOutro: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("===== FICHA =====", style = MaterialTheme.typography.titleLarge)
        Text("Nome: ${p.nome}")
        Text("Raça: ${p.raca.nome}")
        Text("Classe: ${p.classe.nome}")
        Spacer(Modifier.height(8.dp))
        Text("Atributos:")
        Text("FOR: ${p.atributos.forca}, DES: ${p.atributos.destreza}, CON: ${p.atributos.constituicao}")
        Text("INT: ${p.atributos.inteligencia}, SAB: ${p.atributos.sabedoria}, CAR: ${p.atributos.carisma}")
        Spacer(Modifier.height(8.dp))
        Text("PV: ${p.pontosDeVida}, CA: ${p.classeDeArmadura}")
        Spacer(Modifier.height(8.dp))
        Text("Descrição Classe: ${p.classe.descricao}")
        Text("Habilidades: ${p.classe.habilidadesDeClasse().joinToString()}")

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onCriarOutro,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Criar outro personagem")
        }
    }
}