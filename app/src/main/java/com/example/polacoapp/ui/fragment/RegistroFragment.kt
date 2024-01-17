package com.example.polacoapp.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.polacoapp.R
import com.example.polacoapp.model.Cliente
import com.example.polacoapp.utils.AudioUtiles
import com.example.polacoapp.utils.ImagenUtiles
import com.example.polacoapp.viewmodel.ClienteViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_registro.*
import java.time.LocalDateTime


class RegistroFragment : Fragment() {
    private lateinit var audioUtiles: AudioUtiles

    //Se define el objeto para acceder a todo lo correspondiente a la foto
    private lateinit var imagenUtiles: ImagenUtiles

    //se define el objeto activity que refresa la imagen tomada.
    private lateinit var tomarFotoActivity: ActivityResultLauncher<Intent>
    private lateinit var clienteViewModel: ClienteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        clienteViewModel =
            ViewModelProvider(this).get(ClienteViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_registro, container, false)
        //Se establecen los elementos y el objeto para audios

        audioUtiles = AudioUtiles(
            requireActivity(),
            requireContext(),
            view.findViewById(R.id.btnMicro),
            view.findViewById(R.id.btnPlay),
            view.findViewById(R.id.btnDelete),
            getString(R.string.msgGrabandoAudio),
            getString(R.string.msgDeteniendoAudio),
            getString(R.string.msgEliminandoAudio)
        )

        //Se establece el activity que refresca la imagen
        tomarFotoActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { resultado ->
            if (resultado.resultCode == Activity.RESULT_OK) {
                imagenUtiles.actualizaFoto()
            }
        }

        imagenUtiles = ImagenUtiles(
            requireContext(),
            view.findViewById(R.id.bt_photo),
            view.findViewById(R.id.bt_rota_l),
            view.findViewById(R.id.bt_rota_r),
            view.findViewById(R.id.imagen),
            tomarFotoActivity
        )

        return view
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnAgregar.setOnClickListener {
          validos()
        }
        btnList.setOnClickListener {
            findNavController().navigate(R.id.action_registroFragment_to_bottomSheetFragment)
        }

        btnCalcular.setOnClickListener {
            ubicaGPS()
        }
    }

    private var conPermisos = true

    private fun ubicaGPS() {
        //Se ubica el sevicio de localización
        val fusedLocation: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 105
            )
        }
        if (conPermisos) {
            fusedLocation.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        tvLatitud.text = "${location.latitude}"
                        tvLongitud.text = "${location.longitude}"
                        tvAltura.text = "${location.altitude}"
                    } else {
                        tvLatitud.text = getString(R.string.msg_error)
                        tvLongitud.text = getString(R.string.msg_error)
                        tvAltura.text = getString(R.string.msg_error)
                    }
                }
        }
    }

    private fun validos() {
        var registrarse = true;
        var precio = 0.0
        if (txtNombre.text.isEmpty() || txtApellido.text.isEmpty() || txtIdentificacion.text.isEmpty()
            || txtDireccion.text.isEmpty() || txtCompra.text.isEmpty() || txtPrecio.text.isEmpty()
        ) {
            Toast.makeText(
                requireContext(),
                "Quedan espacios vacios",
                Toast.LENGTH_LONG
            ).show()
            registrarse = false
        }
        precio = txtPrecio.text.toString().toDouble()
        if (precio <= 0.0) {
            Toast.makeText(
                requireContext(),
                "El precio debe ser mayor a 0",
                Toast.LENGTH_LONG
            ).show()
            registrarse = false
        }

        if (registrarse) {
            Toast.makeText(
                requireContext(),
                "Procesando registro...",
                Toast.LENGTH_LONG
            ).show()
            subeNotaAudio()
        }
    }
    private fun subeNotaAudio() {
        Toast.makeText(requireContext(), "Subiendo audio...", Toast.LENGTH_LONG).show()
        val audioFile = audioUtiles.audioFile
        if (audioFile.exists() && audioFile.isFile && audioFile.canRead()) {
            //Si hay nota de audio... se sube la nota de audio en el storage...

            //La ruta del archivo de audio "local" en el cel... temporal...
            val ruta = Uri.fromFile(audioFile)

            //Referencia al archivo donde quedará la nota de audio...
            val referencia: StorageReference =
                Firebase.storage.reference.child(
                    "files/audio/${audioFile.name}"
                )

            referencia.putFile(ruta)
                .addOnSuccessListener {
                    val urlDescarga = referencia.downloadUrl
                    urlDescarga.addOnSuccessListener {
                        //SI se subió la nota de audio... y se sabe cual es la ruta pública de ese audio...
                        val rutaAudio = it.toString()
                        Toast.makeText(requireContext(), "Audio subido correctamente..\n Subiendo foto.", Toast.LENGTH_LONG).show()
                        subeFoto(rutaAudio)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error al subir el audio..\n Subiendo foto.", Toast.LENGTH_LONG).show()

                    subeFoto("")  //No se subio nota de audio por error, ruta vacía...
                }

        } else {  //Si por algo no se puede acceder al archivo de audio...
            subeFoto("")  //No hay nota de audio, ruta vacía...
        }
    }

    private fun subeFoto(rutaAudio: String) {
        //Se muestra un mensaje subiendo foto...

        val imagenFile = imagenUtiles.imagenFile
        if (imagenFile.exists() && imagenFile.isFile && imagenFile.canRead()) {
            //Si hay Foto del lugar... se sube la Foto en el storage...

            //La ruta de la foto "local" en el cel... temporal...
            val ruta = Uri.fromFile(imagenFile)

            //Referencia al archivo donde quedará la foto del lugar...
            val referencia: StorageReference =
                Firebase.storage.reference.child(
                    "files/imagen/${imagenFile.name}"
                )

            referencia.putFile(ruta)
                .addOnSuccessListener {
                    val urlDescarga = referencia.downloadUrl
                    urlDescarga.addOnSuccessListener {
                        //SI se subió la foto... y se sabe cual es la ruta pública de esa foto...
                        Toast.makeText(requireContext(), "Foto subida correctamente..\n Subiendo registro.", Toast.LENGTH_LONG).show()
                        agregarLugar(rutaAudio, imagenFile.name)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error al subir la foto..\n Subiendo registro.", Toast.LENGTH_LONG).show()

                    agregarLugar(rutaAudio, "")  //No se subio la foto por error, ruta vacía...
                }

        } else {  //Si por algo no se puede acceder al archivo de la foto...
            agregarLugar(rutaAudio, "")  //No hay foto, ruta vacía...
        }
    }

    private fun agregarLugar(rutaAudio: String, rutaImagen: String) {
        val nombre = txtNombre.text.toString()
        val apellido = txtApellido.text.toString()
        val id = txtIdentificacion.text.toString()
        val direccion = txtDireccion.text.toString()
        val producto = txtCompra.text.toString()
        val latitud = tvLatitud.text.toString().toDouble()
        val longitud = tvLongitud.text.toString().toDouble()
        val altura = tvAltura.text.toString().toDouble()
        val precio = txtPrecio.text.toString().toDouble()
        val saldo = 0.0;
        val cliente = Cliente(
            id, nombre, apellido, direccion, LocalDateTime.now().toString(), producto, longitud, latitud,
            altura, precio, saldo, rutaAudio, rutaImagen
        )
        clienteViewModel.addCliente(cliente)
        Toast.makeText(
            requireContext(),
            getString(R.string.msgLugarAgregado),
            Toast.LENGTH_LONG
        ).show()

    }

}