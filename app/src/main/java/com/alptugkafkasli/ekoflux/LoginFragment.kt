package com.alptugkafkasli.ekoflux

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.alptugkafkasli.ekoflux.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            if (user?.isEmailVerified == true) {
                                // Login success and email is verified
                                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                            } else {
                                // Email not verified
                                Snackbar.make(view, "Please verify your email address.", Snackbar.LENGTH_LONG).show()
                                auth.signOut() // Optional: Log out the user if email is not verified
                            }
                        } else {
                            // If login fails, display a message to the user
                            Snackbar.make(view, "Authentication failed.", Snackbar.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Snackbar.make(view, "Please enter email and password.", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.signupTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
