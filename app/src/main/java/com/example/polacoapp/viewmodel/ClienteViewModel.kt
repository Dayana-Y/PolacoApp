package com.example.polacoapp.viewmodel

import android.app.Application
import android.widget.ProgressBar
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.polacoapp.data.ClienteDao
import com.example.polacoapp.model.Cliente
import com.example.polacoapp.repository.ClienteRepository

class ClienteViewModel(aplication : Application)
    : AndroidViewModel(aplication){
    private val repository: ClienteRepository = ClienteRepository(ClienteDao())

    fun getAllData(): MutableLiveData<List<Cliente>> {
        return repository.getAllData()
    }

    //Implementamos las funciones CRUD
    fun addCliente(cliente: Cliente) {
        repository.addCliente(cliente)
    }

    //Implementamos las funciones CRUD
    fun updateCliente(cliente: Cliente, abono: Double): MutableLiveData<List<Cliente>> {
        return repository.updateCliente(cliente, abono)
    }
}