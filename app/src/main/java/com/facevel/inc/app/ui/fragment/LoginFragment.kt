package com.facevel.inc.app.ui.fragment

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.facevel.inc.app.R
import com.facevel.inc.app.databinding.FragmentDashboardBinding
import com.facevel.inc.app.databinding.FragmentLoginBinding
import com.facevel.inc.app.repository.AuthRepository
import com.facevel.inc.app.repository.authLog
import com.facevel.inc.app.utils.ActionUtils.showSnackbar
import com.facevel.inc.app.utils.NavigationAnimations
import com.facevel.inc.app.utils.safeNavigate
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding
    private var os = ""
    private lateinit var loginStateFetchJob: Job

    @Inject
    lateinit var loader: Loader


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)


        binding = FragmentLoginBinding.bind(view)

        //setPolicyTextView()

        if(viewModel.isUserLoggedIn()){
            findNavController().safeNavigate(
                R.id.action_loginFragment_to_dashboardFragment,
                null,
                NavigationAnimations.getAlphaAnimation()
            )
        }

        loginStateFetchJob = lifecycleScope.launch { fetchLoginState() }
/*
        arguments?.getInt("return")?.let {
            *//*There is a return location specified*//*
            println(
                    "Return specified"
            )
            viewModel.loginReturnLocationId = it
        }*/

//        binding.back.setOnClickListener {
//            requireActivity().onBackPressed()
//        }

        binding.emailLoginButton.setOnClickListener {
            loader.startLoader()
            val emailFromInput = binding.emailInput.text.toString()
            val passwordFromInput = binding.passInput.text.toString()
            viewModel.signInWithEmail(emailFromInput, passwordFromInput)
        }

        return view
    }

    private suspend fun fetchLoginState() {
        viewModel.getLoginState()
            .onCompletion {
                viewModel.resetAuthState()
            }.collect {
                when (it) {
                    is AuthRepository.AuthStateClass.Error -> {
                        "Login failed".authLog()
                        it.message.apply {
                            "Auth error - $this".authLog()
                            this.showSnackbar(binding.root, true)
                        }
                        loader.stopLoading()
                    }
                    is AuthRepository.AuthStateClass.Success -> {
                        Toast.makeText(requireContext(), "Login Successful!", Toast.LENGTH_SHORT).show()
                        findNavController().safeNavigate(
                            R.id.action_loginFragment_to_dashboardFragment,
                            null,
                            NavigationAnimations.getAlphaAnimation()
                        )
                        "Login successful".authLog()
                        loader.stopLoading()
                    }
                    is AuthRepository.AuthStateClass.InputError -> {
                        "Auth error - ${it.message}"
                        when {
                            it.message.contains("email") -> {
                                binding.emailInput.error = it.message
                                it.message.showSnackbar(binding.root, true)
                            }
                            it.message.contains("password") -> {
                                it.message.showSnackbar(binding.root, true)
                                binding.passInput.error = it.message
                            }
                        }
                        loader.stopLoading()
                    }
                    else -> Unit
                }
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        if (this::loginStateFetchJob.isInitialized) {
            loginStateFetchJob.cancel()
        }
    }

    override fun onStop() {
        super.onStop()
        if (this::loginStateFetchJob.isInitialized) {
            loginStateFetchJob.cancel()
        }
    }
}