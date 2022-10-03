package com.facevel.inc.app.ui.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facevel.inc.app.R
import com.facevel.inc.app.databinding.FragmentDashboardBinding
import com.facevel.inc.app.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        binding = FragmentLoginBinding.bind(view)
        val username = binding.idEdtUserName.editableText.toString()
        binding.idBtnLogin.setOnClickListener {
            Toast.makeText(requireContext(), "Username is : $username", Toast.LENGTH_SHORT).show()
        }
        return view
    }
}

