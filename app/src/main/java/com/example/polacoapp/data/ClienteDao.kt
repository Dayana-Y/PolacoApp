package com.example.polacoapp.data

import android.util.Log
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import com.example.polacoapp.model.Cliente
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.toObject

class ClienteDao {
    private var firestore: FirebaseFirestore
    val TAG = "ClienteDao"
    init {

        //Se establace una "conexion" hacia la instancia de Firestore
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings
            .Builder().build()
    }

    fun saveCliente(cliente: Cliente) {
        val df : DocumentReference

            df = firestore
                .collection("Clientes")
                .document("${cliente.id}${cliente.producto}${cliente.precio}${cliente.fechaCreacion}")

        df.set(cliente) //Acá efectivamente se Registra la INFO
            .addOnSuccessListener {
                Log.i("ClientesApp","Lugar almacenado")
            }
            .addOnCanceledListener {
                Log.e("ClientesApp","Lugar NO almacenado")
            }
    }

    fun getClientes(): MutableLiveData<List<Cliente>> {
        val listaFinal = MutableLiveData<List<Cliente>>()
        firestore
            .collection("Clientes")
            .addSnapshotListener{ instantanea, e ->
                if (e != null) {
                    Log.w("ClientesApp","Error con instantánea")
                    return@addSnapshotListener
                }
                if (instantanea != null) {  //Se va a crear la lista...
                    val lista = ArrayList<Cliente>()
                    val documentos = instantanea.documents
                    documentos.forEach {
                        val cliente = it.toObject(Cliente::class.java)
                        if (cliente!=null) {
                            lista.add(cliente)
                        }
                    }
                    listaFinal.value = lista
                }
            }
        return listaFinal
    }

    fun updateCliente(cliente: Cliente, abono: Double): MutableLiveData<List<Cliente>> {
        val clienteFinal = MutableLiveData<List<Cliente>>()
        val docRef = firestore.collection("Clientes").document("${cliente.id}${cliente.producto}${cliente.precio}${cliente.fechaCreacion}")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    val c = document.toObject<Cliente>()!!
                    val lista = ArrayList<Cliente>()
                    c.saldo = c.saldo?.plus(abono)
                    abonar(c)
                    lista.add(c)
                    clienteFinal.value = lista
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        return clienteFinal
    }

    private fun abonar(cliente: Cliente) {
        val df : DocumentReference

        df = firestore
            .collection("Clientes")
            .document("${cliente.id}${cliente.producto}${cliente.precio}${cliente.fechaCreacion}")

        df.set(cliente) //Acá efectivamente se Registra la INFO
            .addOnSuccessListener {
                Log.i("ClientesApp","Lugar almacenado")
            }
            .addOnCanceledListener {
                Log.e("ClientesApp","Lugar NO almacenado")
            }
    }
}