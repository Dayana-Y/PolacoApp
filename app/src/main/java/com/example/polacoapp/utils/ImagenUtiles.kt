package com.example.polacoapp.utils

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import com.example.polacoapp.BuildConfig
import java.io.File

class ImagenUtiles(
    private val contexto: Context,
    private val btFoto: ImageButton,
    private val btRotarL: ImageButton,
    private val btRotarR: ImageButton,
    private val imagen: ImageView,
    private var tomarFotoActivity: ActivityResultLauncher<Intent>) {

    init {
        btFoto.setOnClickListener { tomarFoto() }

        btRotarL.setOnClickListener {
            imagen.rotation = imagen.rotation - 90f
        }

        btRotarR.setOnClickListener {
            imagen.rotation = imagen.rotation + 90f
        }
    }
    lateinit var imagenFile: File
    private lateinit var currentFotoPath: String

    private fun tomarFoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //Se verifica si la foto se tomó...
        if (intent.resolveActivity(contexto.packageManager) != null) {
            imagenFile = crearArchivoImagen()
            val fotoURI = FileProvider.getUriForFile(
                contexto,
                BuildConfig.APPLICATION_ID+".provider",
                imagenFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT,fotoURI)
            tomarFotoActivity.launch(intent)
        }
    }

    private fun crearArchivoImagen(): File {
        val archivo = OtrosUtiles.getTempFile("imagen_")
        val storageDir: File? =
            contexto.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imagen = File.createTempFile(archivo,".jpg",storageDir)
        currentFotoPath = imagen.absolutePath
        return imagen
    }

    fun actualizaFoto() {
        imagen.setImageBitmap(BitmapFactory.decodeFile(imagenFile.absolutePath))
    }
}