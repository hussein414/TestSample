package com.example.testsample.ui.fragment.policy

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.testsample.R
import com.example.testsample.data.model.PolicyModel
import com.example.testsample.data.model.ProfileModel
import com.example.testsample.databinding.FragmentPolicyBinding
import com.example.testsample.ui.event.DeleteClickListener
import com.example.testsample.ui.viewmodel.policy.PolicyViewModel


class PolicyFragment : Fragment() {
    private lateinit var binding: FragmentPolicyBinding
    private lateinit var policyViewModel: PolicyViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPolicyBinding.inflate(inflater, container, false)
        bindViews()
        return binding.root
    }

    private fun bindViews() {
        binding.apply {
            addButton.setOnClickListener {
                findNavController().navigate(R.id.action_policyFragment_to_policyAllFragment)
            }
        }
    }
}