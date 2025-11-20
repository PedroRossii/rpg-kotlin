package com.example.rpg_front.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpg_front.model.batalha.BatalhaRepository
import com.example.rpg_front.model.batalha.EstadoBatalha

class BatalhaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TelaBatalha(onVoltar = { finish() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaBatalha(onVoltar: () -> Unit) {
    // Observa os dados do Repositório
    val logs by BatalhaRepository.logs.collectAsState()
    val estado by BatalhaRepository.estadoBatalha.collectAsState()

    // Estado para auto-scroll
    val listState = rememberLazyListState()

    // Auto-scroll sempre que adicionar um novo log
    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            listState.animateScrollToItem(logs.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Relatório de Batalha") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E2E2E),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF121212)) // Fundo escuro para estilo RPG
        ) {
            // Cabeçalho de Status
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                when(estado) {
                    EstadoBatalha.RODANDO -> CircularProgressIndicator(color = Color.Red)
                    EstadoBatalha.VITORIA -> Text("VITÓRIA!", color = Color.Green, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    EstadoBatalha.DERROTA -> Text("DERROTA...", color = Color.Gray, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    else -> Text("Preparando...", color = Color.White)
                }
            }

            Divider(color = Color.Gray)

            // Lista de Logs
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(logs) { log ->
                    Text(
                        text = log,
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}