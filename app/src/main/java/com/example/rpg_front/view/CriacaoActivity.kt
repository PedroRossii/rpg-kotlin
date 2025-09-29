package com.example.rpg_front.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rpg_front.model.atributos.Atributos

class CriacaoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CriacaoScreen { nome, raca, classe, atributos ->
                val intent = Intent(this, FichaActivity::class.java).apply {
                    putExtra("nome", nome)
                    putExtra("raca", raca)
                    putExtra("classe", classe)
                    putExtra("for", atributos.forca)
                    putExtra("des", atributos.destreza)
                    putExtra("con", atributos.constituicao)
                    putExtra("int", atributos.inteligencia)
                    putExtra("sab", atributos.sabedoria)
                    putExtra("car", atributos.carisma)
                }
                startActivity(intent)
            }
        }
    }
}

@Composable
fun CriacaoScreen(
    onConfirmar: (String, String, String, Atributos) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var raca by remember { mutableStateOf("Humano") }
    var classe by remember { mutableStateOf("Guerreiro") }

    // Estados para controlar a rolagem de atributos
    val metodosRolagem = listOf("Clássico", "Aventureiro", "Heróico")
    var metodoSelecionado by remember { mutableStateOf(metodosRolagem.first()) }
    var valoresRolados by remember { mutableStateOf<List<Int>?>(null) }
    var atributosFinais by remember { mutableStateOf<Atributos?>(null) }
    var podeCriar by remember { mutableStateOf(false) }


    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()) // Adicionado para a tela ser rolável
    ) {
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome do Personagem") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))
        Text("Raça:")
        DropdownMenuDemo(listOf("Humano", "Elfo", "Anão", "Halfling")) { raca = it }

        Spacer(Modifier.height(16.dp))
        Text("Classe:")
        DropdownMenuDemo(listOf("Guerreiro", "Clérigo", "Mago", "Paladino", "Bárbaro", "Druida", "Acadêmico", "Ilusionista", "Necromante")) { classe = it }

        Spacer(Modifier.height(24.dp))

        // --- SEÇÃO DE ROLAGEM DE ATRIBUTOS ---
        Text("Método de Rolagem de Atributos:", style = MaterialTheme.typography.titleMedium)
        Row(Modifier.fillMaxWidth()) {
            metodosRolagem.forEach { metodo ->
                Row(
                    Modifier
                        .selectable(
                            selected = (metodo == metodoSelecionado),
                            onClick = {
                                metodoSelecionado = metodo
                                valoresRolados = null
                                atributosFinais = null
                                podeCriar = false
                            }
                        )
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (metodo == metodoSelecionado),
                        onClick = {
                            metodoSelecionado = metodo
                            valoresRolados = null
                            atributosFinais = null
                            podeCriar = false
                        }
                    )
                    Text(text = metodo)
                }
            }
        }

        Button(onClick = {
            when (metodoSelecionado) {
                "Clássico" -> {
                    atributosFinais = Atributos.gerarClassico()
                    valoresRolados = null
                    podeCriar = true
                }
                "Aventureiro" -> {
                    valoresRolados = Atributos.gerarValoresAventureiro()
                    atributosFinais = null
                    podeCriar = false
                }
                "Heróico" -> {
                    valoresRolados = Atributos.gerarValoresHeroico()
                    atributosFinais = null
                    podeCriar = false
                }
            }
        }) {
            Text("Rolar Atributos")
        }

        Spacer(Modifier.height(16.dp))

        if (metodoSelecionado == "Clássico" && atributosFinais != null) {
            Text("Atributos Rolados (Clássico):")
            Text("FOR:${atributosFinais!!.forca}, DES:${atributosFinais!!.destreza}, CON:${atributosFinais!!.constituicao}, " +
                    "INT:${atributosFinais!!.inteligencia}, SAB:${atributosFinais!!.sabedoria}, CAR:${atributosFinais!!.carisma}")
        }

        if (valoresRolados != null && metodoSelecionado != "Clássico") {
            AtributosAssignmentScreen(
                rolledValues = valoresRolados!!,
                onAttributesAssigned = {
                    atributosFinais = it
                    podeCriar = true
                }
            )
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { onConfirmar(nome, raca, classe, atributosFinais!!) },
            enabled = podeCriar && nome.isNotBlank()
        ) {
            Text("Criar Personagem")
        }
    }
}

@Composable
fun AtributosAssignmentScreen(
    rolledValues: List<Int>,
    onAttributesAssigned: (Atributos) -> Unit
) {
    var forca by remember { mutableStateOf<Int?>(null) }
    var destreza by remember { mutableStateOf<Int?>(null) }
    var constituicao by remember { mutableStateOf<Int?>(null) }
    var inteligencia by remember { mutableStateOf<Int?>(null) }
    var sabedoria by remember { mutableStateOf<Int?>(null) }
    var carisma by remember { mutableStateOf<Int?>(null) }

    val assignments = listOfNotNull(forca, destreza, constituicao, inteligencia, sabedoria, carisma)

    LaunchedEffect(assignments) {
        if (assignments.size == rolledValues.size) {
            onAttributesAssigned(
                Atributos(forca!!, destreza!!, constituicao!!, inteligencia!!, sabedoria!!, carisma!!)
            )
        }
    }

    val remainingValues = rolledValues.toMutableList()
    assignments.forEach { assignedValue ->
        remainingValues.remove(assignedValue)
    }

    Column {
        Text("Valores Rolados: ${rolledValues.joinToString()}", style = MaterialTheme.typography.titleMedium)
        Text("Atribua cada valor a um atributo:")

        AttributeDropdown("Força (FOR)", forca, (remainingValues + listOfNotNull(forca)).sortedDescending()) { forca = it }
        AttributeDropdown("Destreza (DES)", destreza, (remainingValues + listOfNotNull(destreza)).sortedDescending()) { destreza = it }
        AttributeDropdown("Constituição (CON)", constituicao, (remainingValues + listOfNotNull(constituicao)).sortedDescending()) { constituicao = it }
        AttributeDropdown("Inteligência (INT)", inteligencia, (remainingValues + listOfNotNull(inteligencia)).sortedDescending()) { inteligencia = it }
        AttributeDropdown("Sabedoria (SAB)", sabedoria, (remainingValues + listOfNotNull(sabedoria)).sortedDescending()) { sabedoria = it }
        AttributeDropdown("Carisma (CAR)", carisma, (remainingValues + listOfNotNull(carisma)).sortedDescending()) { carisma = it }
    }
}

@Composable
fun AttributeDropdown(
    label: String,
    selectedValue: Int?,
    options: List<Int>,
    onValueSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Box {
            Button(onClick = { expanded = true }) {
                Text(selectedValue?.toString() ?: "Selecione")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.toString()) },
                        onClick = {
                            onValueSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun DropdownMenuDemo(options: List<String>, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(options.first()) }

    Box {
        Button(onClick = { expanded = true }) { Text(selected) }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selected = option
                        expanded = false
                        onSelected(option)
                    }
                )
            }
        }
    }
}