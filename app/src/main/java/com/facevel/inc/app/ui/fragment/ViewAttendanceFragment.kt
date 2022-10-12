package com.facevel.inc.app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facevel.inc.app.R
import com.facevel.inc.app.databinding.FragmentViewAttendanceBinding

class ViewAttendanceFragment : Fragment() {

    private lateinit var binding: FragmentViewAttendanceBinding
    private lateinit var viewModel: ViewAttendanceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_attendance, container, false)

        return view
    }

}