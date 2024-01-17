package com.example.polacoapp.repository

import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import com.example.polacoapp.data.ClienteDao
import com.example.polacoapp.model.Cliente

class ClienteRepository (private val clienteDao: ClienteDao) {

    fun getAllData(): MutableLiveData<List<Cliente>>{
        return clienteDao.getClientes()
    }

    fun addCliente(cliente: Cliente) {
        clienteDao.saveCliente(cliente)
    }

    fun updateCliente(cliente: Cliente, abono: Double) : MutableLiveData<List<Cliente>>{
        return clienteDao.updateCliente(cliente, abono)
    }
}