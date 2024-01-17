package com.example.polacoapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.polacoapp.R
import com.example.polacoapp.viewmodel.ClienteViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_registro.*

class HomeFragment : Fragment()  {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        auth = Firebase.auth
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        //Se establecen los elementos y el objeto para audios

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnReporte.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_reporteFragment)
        }
        btnMantenimiento.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_registroFragment)
        }
        btnDeslogin.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_homeFragment_to_inicioFragment)
        }
    }
}