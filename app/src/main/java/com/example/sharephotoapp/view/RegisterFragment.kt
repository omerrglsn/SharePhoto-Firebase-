package com.example.sharephotoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.sharephotoapp.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : Fragment() {

    private var auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.haveAnAccount.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        userSignUp()

    }

    private fun userSignUp(){

        binding.registerBtnFragmentLogin.setOnClickListener {

            val email = binding.emailFragmentRegister.text.toString()
            val password = binding.passwordFragmentRegister.text.toString()

            auth.createUserWithEmailAndPassword(email , password)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        val action = RegisterFragmentDirections.actionRegisterFragmentToFeedFragment()
                        findNavController().navigate(action)

                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context , it.localizedMessage , Toast.LENGTH_LONG).show()
                }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}