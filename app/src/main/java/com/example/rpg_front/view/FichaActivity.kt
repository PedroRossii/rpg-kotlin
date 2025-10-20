package com.example.rpg_front.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.rpg_front.controller.PersonagemController
import com.example.rpg_front.data.AppDatabase
import com.example.rpg_front.data.PersonagemEntity
import com.example.rpg_front.model.atributos.Atributos
import com.example.rpg_front.model.personagem.Personagem
import kotlinx.coroutines.launch

class FichaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionType = intent.getStringExtra("ACTION_TYPE")
        val personagemId = intent.getLongExtra("PERSONAGEM_ID", -1L)

        val dao = AppDatabase.getDatabase(applicationContext).personagemDao()
        val personagemController = PersonagemController()
        val activity = this

        // A tela de Ficha agora tem duas funções: salvar um novo personagem ou exibir um existente.
        if (actionType == "SAVE_NEW") {
            // Modo: Salvar Novo Personagem
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
            val personagemEntity = PersonagemEntity(
                nome = nome,
                nomeRaca = raca,
                nomeClasse = classe,
                atributos = atributos
            )

            // Usamos setContent para mostrar uma tela de "Salvando..." e executar a lógica.
            setContent {
                LaunchedEffect(Unit) {
                    launch {
                        dao.insert(personagemEntity)
                        Toast.makeText(activity, "$nome salvo!", Toast.LENGTH_SHORT).show()
                        // Volta para a MainActivity
                        val intent = Intent(activity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Salvando personagem...")
                }
            }

        } else {
            // Modo: Exibir Personagem Existente
            if (personagemId == -1L) {
                finish()
                return
            }

            setContent {
                val personagemEntity by dao.getById(personagemId).collectAsState(initial = null)
                val entity = personagemEntity

                if (entity != null) {
                    val personagemCompleto = personagemController.criarPersonagem(
                        nome = entity.nome,
                        raca = entity.nomeRaca,
                        classe = entity.nomeClasse,
                        atributos = entity.atributos
                    )
                    FichaDetalhadaScreen(
                        p = personagemCompleto,
                        onNavigateBack = { activity.finish() }
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FichaDetalhadaScreen(
    p: Personagem,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ficha de ${p.nome}") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Raça: ${p.raca.nome} | Classe: ${p.classe.nome}", style = MaterialTheme.typography.titleMedium)
            Text("Alinhamento: ${p.raca.alinhamento}")
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text("Atributos", style = MaterialTheme.typography.titleMedium)
            Text("FOR: ${p.atributos.forca}, DES: ${p.atributos.destreza}, CON: ${p.atributos.constituicao}")
            Text("INT: ${p.atributos.inteligencia}, SAB: ${p.atributos.sabedoria}, CAR: ${p.atributos.carisma}")
            Spacer(Modifier.height(8.dp))
            Text("PV: ${p.pontosDeVida}, CA: ${p.classeDeArmadura}, BA: ${p.baseDeAtaque}, JP: ${p.jogadaDeProtecao}")
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text("Habilidades Raciais", style = MaterialTheme.typography.titleMedium)
            Text(p.raca.habilidades.joinToString(", "))
            Spacer(Modifier.height(8.dp))
            Text("Habilidades de Classe", style = MaterialTheme.typography.titleMedium)
            Text(p.classe.habilidadesDeClasse().joinToString(separator = "\n- ", prefix = "- "))
        }
    }
}