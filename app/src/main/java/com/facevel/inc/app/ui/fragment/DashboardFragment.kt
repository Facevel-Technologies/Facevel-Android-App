package com.facevel.inc.app.ui.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.facevel.inc.app.R
import com.facevel.inc.app.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

fun NavController.safeNavigate(action: Int, arguments: Bundle?, options: NavOptions) {
    /*
    * We have to check if destination is the same as the current fragment
    *  */
    val destinationId = currentDestination?.getAction(action)?.destinationId
    val currentDestinationId = this.currentDestination?.id
    if (destinationId != currentDestinationId && destinationId != null) {
        navigate(action, arguments, options)
    }
}

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        binding = FragmentDashboardBinding.bind(view)

        return view
    }
}