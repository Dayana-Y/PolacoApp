package com.example.polacoapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.polacoapp.R
import com.example.polacoapp.adapter.ClienteAdapter
import com.example.polacoapp.model.Cliente
import com.example.polacoapp.viewmodel.ClienteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*

class BottomSheetFragment : BottomSheetDialogFragment(), ClienteAdapter.onClienteClickListener {
    private lateinit var clienteViewModel: ClienteViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
        clienteViewModel =
            ViewModelProvider(this).get(ClienteViewModel::class.java)

        // Inflate the layout for this fragment
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
                recyclerView.adapter = ClienteAdapter(lista, this)
            }
        )
    }

    override fun onItemClick(item: Cliente) {
        findNavController().navigate(BottomSheetFragmentDirections.actionBottomSheetFragmentToInfoClienteFragment(item))
    }
}