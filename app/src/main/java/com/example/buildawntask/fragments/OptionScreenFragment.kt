package com.example.buildawntask.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.buildawntask.R
import kotlinx.android.synthetic.main.fragment_option_screen.*

/**
 * A simple [Fragment] subclass.
 */
class OptionScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_option_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Navigate to the camera screen on click of the options card views
        mark_attendance_cardView.setOnClickListener {
            it.findNavController().navigate(R.id.action_optionScreenFragment_to_permissionsFragment)
        }
        register_employees_cardView.setOnClickListener {
            it.findNavController().navigate(R.id.action_optionScreenFragment_to_permissionsFragment)
        }
        employee_data_cardView.setOnClickListener {
            it.findNavController().navigate(R.id.action_optionScreenFragment_to_permissionsFragment)
        }
    }
}