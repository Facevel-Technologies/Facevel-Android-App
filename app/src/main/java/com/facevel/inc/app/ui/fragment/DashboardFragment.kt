package com.facevel.inc.app.ui.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.facevel.inc.app.R
import com.facevel.inc.app.databinding.FragmentDashboardBinding
import com.facevel.inc.app.repository.UserDataLoadingState
import com.facevel.inc.app.utils.ActionUtils.showNormalSnackbar
import com.facevel.inc.app.utils.NavigationAnimations
import com.facevel.inc.app.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var binding: FragmentDashboardBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        binding = FragmentDashboardBinding.bind(view)

        lifecycleScope.launch(Dispatchers.IO) {
            setUserData()
        }

        binding.profilePageUserProfilePicture.setOnClickListener {
            Toast.makeText(requireContext(), "User Log out!", Toast.LENGTH_SHORT).show()
            viewModel.logUserOut()
            findNavController().safeNavigate(
                R.id.action_dashboardFragment_to_loginFragment,
                null,
                NavigationAnimations.getAlphaAnimation()
            )
        }
        return view
    }

    private suspend fun setUserData() {
        viewModel.getUserDataState().collect {
            when (it) {
                is UserDataLoadingState.Success -> {
                    withContext(Dispatchers.Main) {
                        binding.profilePageUserName.text = it.donationCategoryData.name
                        binding.profilePageUserProfilePicture.load(it.donationCategoryData.profile_photo)
                        binding.dashboardDepartment.text = it.donationCategoryData.department
                        binding.dashboardEmployeeId.text = it.donationCategoryData.email
                    }
                }
                is UserDataLoadingState.Error -> {
                    withContext(Dispatchers.Main) {
                        it.message.showNormalSnackbar(binding.root, false)
                    }
                }
                else -> Unit
            }
        }
    }
}