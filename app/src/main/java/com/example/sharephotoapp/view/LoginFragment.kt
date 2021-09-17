package com.example.sharephotoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.sharephotoapp.R
import com.example.sharephotoapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logInUser()

        ifExistingUser()

        binding.dontYouHaveAnAccount.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
    }

    private fun logInUser(){

        binding.loginBtnFragmentLogin.setOnClickListener {
            val email = binding.emailFragmentLogin.text.toString()
            val password = binding.passwordFragmentLogin.text.toString()

            auth.signInWithEmailAndPassword(email , password)
                .addOnCompleteListener {
                    if (it.isSuccessful){

                        binding.loginBtnFragmentLogin.setOnClickListener {
                            val action = LoginFragmentDirections.actionLoginFragmentToFeedFragment()
                            findNavController().navigate(action)
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context , it.localizedMessage , Toast.LENGTH_LONG).show()
                }
        }

    }

    private fun ifExistingUser(){
        val currentUser = auth.currentUser

        if (currentUser != null){
            val action = LoginFragmentDirections.actionLoginFragmentToFeedFragment()
            findNavController().navigate(action)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}