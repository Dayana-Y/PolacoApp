package com.example.polacoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.polacoapp.R
import com.example.polacoapp.base.BaseViewHolder
import com.example.polacoapp.model.Cliente
import com.example.polacoapp.ui.fragment.BottomSheetFragment
import kotlinx.android.synthetic.main.cliente_fila.view.*
import java.lang.IllegalArgumentException

class ClienteAdapter(var clientes: List<Cliente>, private val itemClickListener: BottomSheetFragment) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    interface onClienteClickListener{
        fun onItemClick(item: Cliente)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return ClientesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cliente_fila,parent,false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is ClientesViewHolder -> holder.bind(clientes[position], position)
            else -> IllegalArgumentException("Error")
        }
    }

    override fun getItemCount(): Int {
        return clientes.size
    }

    inner class ClientesViewHolder(itemView:View) : BaseViewHolder<Cliente>(itemView){
        override fun bind(item: Cliente, position: Int) {
            itemView.setOnClickListener { itemClickListener.onItemClick(item) }
            itemView.prefix.text = item.nombre[0].toString()
            itemView.username.text = "${item.nombre}, ${item.producto}"
        }

    }
}