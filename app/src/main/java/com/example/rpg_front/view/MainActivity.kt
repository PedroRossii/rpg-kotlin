package com.example.rpg_front.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.rpg_front.data.AppDatabase
import com.example.rpg_front.data.PersonagemEntity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = AppDatabase.getDatabase(this).personagemDao()

        setContent {
            val personagens by dao.getAll().collectAsState(initial = emptyList())
            MainScreen(
                personagens = personagens,
                onAddPersonagem = {
                    startActivity(Intent(this, CriacaoActivity::class.java))
                },
                // NOVO: Adicionando a ação de clique
                onPersonagemClick = { personagemId ->
                    val intent = Intent(this, FichaActivity::class.java).apply {
                        putExtra("PERSONAGEM_ID", personagemId)
                    }
                    startActivity(intent)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    personagens: List<PersonagemEntity>,
    onAddPersonagem: () -> Unit,
    onPersonagemClick: (Long) -> Unit // NOVO: Callback para o clique
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Meus Personagens") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddPersonagem) {
                Icon(Icons.Default.Add, contentDescription = "Criar Personagem")
            }
        }
    ) { paddingValues ->
        if (personagens.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhum personagem criado ainda.")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
                items(personagens) { p ->
                    PersonagemCard(
                        personagem = p,
                        // NOVO: Passando o ID no clique
                        onClick = { onPersonagemClick(p.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun PersonagemCard(
    personagem: PersonagemEntity,
    onClick: () -> Unit // NOVO: Callback de clique
) {
    Card(
        // MODIFICADO: Adicionado o modifier clickable
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(personagem.nome, style = MaterialTheme.typography.titleLarge)
            Text("${personagem.nomeRaca} | ${personagem.nomeClasse}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}