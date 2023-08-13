package com.example.testsample.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.testsample.R
import com.example.testsample.data.model.ProfileModel
import com.example.testsample.databinding.FragmentAddProfileBinding
import com.example.testsample.ui.view.activites.MainActivity
import com.example.testsample.ui.viewmodel.ProfileViewModel
import com.google.android.material.snackbar.Snackbar


class AddProfileFragment : Fragment() {
    private lateinit var binding: FragmentAddProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddProfileBinding.inflate(inflater, container, false)
        bindViews()
        return binding.root
    }

    private fun bindViews() {
        binding.save.setOnClickListener {
            profileViewModel = (activity as MainActivity).profileViewModel
            val name = binding.inputName.text.toString().trim()
            val multiType = binding.inputType.text.toString().trim()
            if (name.isNotEmpty() && multiType.isNotEmpty()) {
                profileViewModel.checkDuplicate(name).observe(viewLifecycleOwner){isDuplicate->
                    if (isDuplicate){
                        Toast.makeText(requireContext(),"There is duplicate data",Toast.LENGTH_SHORT).show()
                    }else{
                        insertProfile(name,multiType)
                    }
                }
            }else {
                Toast.makeText(requireContext(), "please Finished filed", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.skip.setOnClickListener {
            findNavController().navigate(R.id.action_addProfileFragment_to_homeFragment)
        }
    }

    private fun insertProfile(name:String,multiType:String){
        val profileModel = ProfileModel(0, name, multiType)
        profileViewModel.addProfile(profileModel)
        Snackbar.make(
            binding.root,
            "Successes",
            Snackbar.LENGTH_SHORT
        ).show()
        findNavController().navigate(R.id.action_addProfileFragment_to_homeFragment)
    }
}