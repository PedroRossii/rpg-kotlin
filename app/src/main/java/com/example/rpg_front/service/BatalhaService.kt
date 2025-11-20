package com.example.rpg_front.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.rpg_front.R
import com.example.rpg_front.controller.PersonagemController
import com.example.rpg_front.data.AppDatabase
import com.example.rpg_front.model.batalha.BatalhaRepository
import com.example.rpg_front.model.batalha.EstadoBatalha
import com.example.rpg_front.model.batalha.Inimigo
import com.example.rpg_front.model.batalha.SimuladorBatalha
import com.example.rpg_front.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BatalhaService : Service() {

    private val CHANNEL_ID = "RPG_BATALHA_CHANNEL"
    private val NOTIFICATION_ID_FOREGROUND = 101
    private val NOTIFICATION_ID_RESULTADO = 102

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val personagemId = intent?.getLongExtra("PERSONAGEM_ID", -1L) ?: -1L

        if (personagemId != -1L) {
            try {
                iniciarServicoForeground()
                realizarBatalha(personagemId)
            } catch (e: Exception) {
                Log.e("BatalhaService", "Erro: ${e.message}")
                stopSelf()
            }
        } else {
            stopSelf()
        }
        return START_NOT_STICKY
    }

    private fun iniciarServicoForeground() {
        criarCanalNotificacao()
        val notificacao = criarNotificacao("Em Batalha!", "Toque para ver o combate em tempo real.")

        if (Build.VERSION.SDK_INT >= 34) {
            startForeground(NOTIFICATION_ID_FOREGROUND, notificacao, ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE)
        } else {
            startForeground(NOTIFICATION_ID_FOREGROUND, notificacao)
        }
    }

    private fun realizarBatalha(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            // Reseta o estado no repositório para a tela ler
            BatalhaRepository.iniciarBatalha()

            val dao = AppDatabase.getDatabase(applicationContext).personagemDao()
            val entity = dao.getById(id).first()

            if (entity != null) {
                val controller = PersonagemController()
                val personagem = controller.criarPersonagem(
                    entity.nome, entity.atributos, entity.nomeRaca, entity.nomeClasse
                )

                // Pequeno delay inicial
                Thread.sleep(1000)

                val inimigo = Inimigo.gerarInimigoAleatorio()
                val simulador = SimuladorBatalha()

                // Passa o callback que atualiza o repositório
                val resultado = simulador.simular(personagem, inimigo) { mensagemLog ->
                    BatalhaRepository.adicionarLog(mensagemLog)
                }

                // Atualiza estado final
                BatalhaRepository.finalizarBatalha(resultado.vitoria)

                // Notificação final
                enviarNotificacaoResultado(resultado.vitoria, personagem.nome, inimigo.nome)
            }
            stopSelf()
        }
    }

    private fun enviarNotificacaoResultado(vitoria: Boolean, nomeHeroi: String, nomeInimigo: String) {
        val titulo = if (vitoria) "Vitória!" else "Derrota..."
        val mensagem = if (vitoria) "$nomeHeroi venceu $nomeInimigo!" else "$nomeHeroi caiu..."
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID_RESULTADO, criarNotificacao(titulo, mensagem))
    }

    private fun criarNotificacao(titulo: String, conteudo: String): android.app.Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(titulo)
            .setContentText(conteudo)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
    }

    private fun criarCanalNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Batalhas RPG", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}