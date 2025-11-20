package com.example.rpg_front.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.rpg_front.controller.PersonagemController
import com.example.rpg_front.data.AppDatabase
import com.example.rpg_front.data.PersonagemEntity
import com.example.rpg_front.model.atributos.Atributos
import com.example.rpg_front.model.personagem.Personagem
import com.example.rpg_front.service.BatalhaService
import kotlinx.coroutines.launch

class FichaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionType = intent.getStringExtra("ACTION_TYPE")
        val personagemId = intent.getLongExtra("PERSONAGEM_ID", -1L)
        val dao = AppDatabase.getDatabase(applicationContext).personagemDao()
        val personagemController = PersonagemController()
        val activity = this

        if (actionType == "SAVE_NEW") {
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
                nome = nome, nomeRaca = raca, nomeClasse = classe, atributos = atributos
            )
            setContent {
                LaunchedEffect(Unit) {
                    launch {
                        dao.insert(personagemEntity)
                        Toast.makeText(activity, "$nome salvo!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(activity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                }
                Box(modifier = Modifier.fillMaxSize()) { Text("Salvando...") }
            }
        } else {
            if (personagemId == -1L) { finish(); return }

            setContent {
                val personagemEntity by dao.getById(personagemId).collectAsState(initial = null)
                val entity = personagemEntity

                if (entity != null) {
                    val personagemCompleto = personagemController.criarPersonagem(
                        entity.nome, entity.atributos, entity.nomeRaca, entity.nomeClasse
                    )
                    FichaDetalhadaScreen(
                        p = personagemCompleto,
                        personagemId = personagemId,
                        onNavigateBack = { activity.finish() }
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize()) { CircularProgressIndicator() }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FichaDetalhadaScreen(
    p: Personagem,
    personagemId: Long,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            iniciarServicoBatalha(context, personagemId)
        } else {
            Toast.makeText(context, "Permissão necessária para batalhar", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ficha de ${p.nome}") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
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

            Spacer(Modifier.height(24.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
                            PackageManager.PERMISSION_GRANTED
                        ) {
                            iniciarServicoBatalha(context, personagemId)
                        } else {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    } else {
                        iniciarServicoBatalha(context, personagemId)
                    }
                }
            ) {
                Text("Explorar Masmorra (Batalha)")
            }
        }
    }
}

fun iniciarServicoBatalha(context: android.content.Context, id: Long) {
    // 1. Inicia o Serviço (Lógica em Background)
    val intentService = Intent(context, BatalhaService::class.java).apply {
        putExtra("PERSONAGEM_ID", id)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intentService)
    } else {
        context.startService(intentService)
    }

    // 2. Abre a Tela de Batalha (Visualização)
    val intentActivity = Intent(context, BatalhaActivity::class.java)
    context.startActivity(intentActivity)
}