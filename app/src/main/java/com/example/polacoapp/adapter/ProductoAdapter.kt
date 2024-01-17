package com.example.polacoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.polacoapp.R
import com.example.polacoapp.base.BaseViewHolder
import com.example.polacoapp.model.Cliente
import com.example.polacoapp.ui.fragment.BottomSheetFragment
import com.example.polacoapp.ui.fragment.ReporteFragment
import kotlinx.android.synthetic.main.cliente_fila.view.*
import kotlinx.android.synthetic.main.producto_fila.view.*
import java.lang.IllegalArgumentException

class ProductoAdapter(var clientes: List<Cliente>, private val itemClickListener: ReporteFragment) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    interface onClienteClickListener{
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return ClientesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.producto_fila,parent,false))
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
            itemView.product.text = "${item.producto} de ${item.nombre}"
            itemView.price.text = item.precio.toString()
        }

    }
}