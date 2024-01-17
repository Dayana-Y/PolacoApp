package com.example.polacoapp.ui.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.polacoapp.R
import com.example.polacoapp.viewmodel.ClienteViewModel
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_info_cliente.*
import kotlinx.android.synthetic.main.fragment_info_cliente.txtCompra
import java.io.File

class InfoClienteFragment : Fragment() {
    val args by navArgs<InfoClienteFragmentArgs>()
    private lateinit var clienteViewModel: ClienteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info_cliente, container, false)
        clienteViewModel =
            ViewModelProvider(this).get(ClienteViewModel::class.java)

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textChanged()
        super.onViewCreated(view, savedInstanceState)
        tvIdentificacion.text = args.cliente.id
        tvNombre.text = args.cliente.nombre
        tvApellido.text = args.cliente.apellido
        tvDireccion.text = args.cliente.direccion
        txtCompra.text = args.cliente.producto
        val precio = args.cliente.precio.toString().toDouble()
        val saldo = args.cliente.saldo.toString().toDouble()
        val mensaje = precio-saldo
        txtSaldo.text = mensaje.toString()

        btnMap.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:${args.cliente.latitud},${args.cliente.longitud}?z8"))
            startActivity(intent)
        }

        val storageRef = FirebaseStorage.getInstance().reference.child("files/imagen/${args.cliente.rutaImagen}")
        val localFile = File.createTempFile("tempImage", ".jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            imagenView.setImageBitmap(bitmap)
        }.addOnFailureListener{

        }

        btnAbonar.setOnClickListener {
            if (txtAbonar.text.toString().toDouble() <= 0) {
                Toast.makeText(
                    requireContext(),
                    "El abono debe ser mayor a 0",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Ingresando abono",
                    Toast.LENGTH_LONG
                ).show()

                clienteViewModel.updateCliente(
                    args.cliente,
                    txtAbonar.text.toString().toDouble()
                ).observe(
                    viewLifecycleOwner, { cliente ->
                        if (it != null) {
                            txtSaldo.text = (cliente[0].precio!! - cliente[0].saldo!!).toString()
                        }
                    }
                )
            }
        }
    }

    fun textChanged(){
        txtAbonar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

}