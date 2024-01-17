package com.example.polacoapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Cliente(
    var id: String,
    val nombre: String,
    val apellido: String,
    val direccion: String?,
    val fechaCreacion: String?,
    val producto: String?,
    val longitud: Double?,
    val latitud: Double?,
    val altura: Double?,
    val precio: Double?,
    var saldo: Double?,
    val rutaAudio: String?,
    val rutaImagen: String?
) : Parcelable {
    constructor(): this("","", "","", LocalDateTime.now().toString(),"",0.0,0.0,0.0,0.0,0.0,"","")
}
