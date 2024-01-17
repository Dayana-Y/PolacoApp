package com.example.polacoapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.polacoapp.R
import java.io.File
import java.io.IOException

class AudioUtiles(
    private val actividad: Activity,
    private val contexto: Context,
    private val btMicro: ImageButton,
    private val btPlay: ImageButton,
    private val btDelete: ImageButton,
    private val msgIniciaAudio: String,
    private val msgDetieneAudio: String,
    private val msgEliminaAudio: String) {

    init {  //Se inicializan los botones...
        btMicro.setOnClickListener { grabaStopAudio() }
        btPlay.setOnClickListener { playAudio() }
        btDelete.setOnClickListener { deleteAudio() }
        btPlay.isEnabled = false
        btDelete.isEnabled = false
    }

    //Objeto para grabar audio
    private var mediaRecorder: MediaRecorder?=null
    //Archivo donde quedará el audio (mp3)
    var audioFile: File = File.createTempFile("audio_",".mp3")
    //Para saber si se está grabando o no
    private var grabando:Boolean = false;

    private fun grabaStopAudio() = if (ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.RECORD_AUDIO) !=
            PackageManager.PERMISSION_GRANTED) {  //Si no se ha otorgado el permiso...
        //Se pide el permiso
        val permisos = arrayOf(Manifest.permission.RECORD_AUDIO)
        ActivityCompat.requestPermissions(actividad,permisos,0)
    } else {  //Si SI se han otorgado los permisos para actividar el micro...
        grabando= if (!grabando) {  //Se inicia la grabación
            mediaRecorderInit()
            iniciaGrabacion()
            true
        } else {  //Estaba grabando... se detiene la grabacion
            detenerAudio()
            false
        }
    }

    private fun mediaRecorderInit() {
        if (audioFile.exists() && audioFile.isFile) {
            audioFile.delete()
        }
        val archivo = OtrosUtiles.getTempFile("audio_")
        audioFile = File.createTempFile(archivo,".mp3")
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder!!.setOutputFile(audioFile)
    }

    private fun iniciaGrabacion() {
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            Toast.makeText(contexto,msgIniciaAudio,Toast.LENGTH_SHORT).show()
            btMicro.setImageResource(R.drawable.ic_stop)
            btPlay.isEnabled = false
            btDelete.isEnabled = false
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun detenerAudio() {
        mediaRecorder?.stop()
        mediaRecorder?.release()
        btPlay.isEnabled = true
        btDelete.isEnabled = true
        Toast.makeText(contexto,msgDetieneAudio,Toast.LENGTH_SHORT).show()
        btMicro.setImageResource(R.drawable.ic_mic)
    }

    private fun playAudio() {
        try {
            if (audioFile.exists() && audioFile.canRead()) {
                val mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(audioFile.path)
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun deleteAudio() {
        try {
            if (audioFile.exists()) {
                audioFile.delete()
                btPlay.isEnabled = false
                btDelete.isEnabled = false
                Toast.makeText(contexto,msgEliminaAudio,Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}