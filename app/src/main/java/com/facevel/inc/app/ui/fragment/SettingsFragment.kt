package com.facevel.inc.app.ui.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facevel.inc.app.R
import com.facevel.inc.app.databinding.FragmentSettingsBinding
import com.facevel.inc.app.databinding.LoaderSheetBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        binding = FragmentSettingsBinding.bind(view.rootView)

        return view
    }
}