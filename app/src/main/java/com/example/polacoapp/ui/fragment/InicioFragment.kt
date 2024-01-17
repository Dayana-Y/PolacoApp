package com.example.polacoapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.polacoapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_inicio.*

class InicioFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inicio, container, false)

        auth = Firebase.auth
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogin.setOnClickListener {
            if (txtEmail.text.toString().length == 0 || txtPass.text.toString().length == 0){
                Toast.makeText(
                    requireContext(),
                    "Quedan espacios vacios",
                    Toast.LENGTH_LONG
                ).show()
            }else {
                Toast.makeText(
                    requireContext(),
                    "Ingresando..",
                    Toast.LENGTH_LONG
                ).show()

                auth.signInWithEmailAndPassword(txtEmail.text.toString(), txtPass.text.toString())
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            findNavController().navigate(R.id.action_inicioFragment_to_homeFragment)
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                requireContext(),
                                "No se pudo ingresar, verifique sus datos",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Toast.makeText(requireContext(), "Bienvenidx ${currentUser.email}", Toast.LENGTH_LONG).show()
            reload()
        }
    }

    private fun reload() {
        findNavController().navigate(R.id.action_inicioFragment_to_homeFragment)
    }

}