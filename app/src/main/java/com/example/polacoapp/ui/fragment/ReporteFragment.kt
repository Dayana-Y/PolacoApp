package com.example.polacoapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.polacoapp.R
import com.example.polacoapp.adapter.ProductoAdapter
import com.example.polacoapp.viewmodel.ClienteViewModel
import kotlinx.android.synthetic.main.fragment_reporte.*

class ReporteFragment : Fragment() {

    private lateinit var clienteViewModel: ClienteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reporte, container, false)
        clienteViewModel =
            ViewModelProvider(this).get(ClienteViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        getClientes()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    fun getClientes(){
        //una funcion que se traiga los clientes en una lista y los imprima en un sout print
        clienteViewModel.getAllData().observe(
            viewLifecycleOwner,{
                val lista = it
                recyclerView.adapter = ProductoAdapter(lista, this)

                var porCobrar = 0.0
                var credito = 0.0
                var vendido = 0.0

                if(lista!=null){
                    lista.forEach { cliente ->
                        vendido += cliente.precio!!
                        txtTotal.text = vendido.toString()
                        credito += cliente.saldo!!
                        txtCredito.text = credito.toString()
                        porCobrar = vendido - credito
                        txtDeuda.text = porCobrar.toString()

                    }
                }
            }
        )
    }
}