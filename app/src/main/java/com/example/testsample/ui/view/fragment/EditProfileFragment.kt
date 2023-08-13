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
import com.example.testsample.databinding.FragmentEditProfileBinding
import com.example.testsample.ui.view.activites.MainActivity
import com.example.testsample.ui.viewmodel.ProfileViewModel


class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var currentProfile: ProfileModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        bindViews()
        return binding.root
    }

    private fun bindViews() {
        viewModel = (activity as MainActivity).profileViewModel
        val args = requireArguments()
        val receivedProfile: ProfileModel? = args.getParcelable("profile_key")
        if (receivedProfile != null){
            currentProfile = receivedProfile
            binding.inputName.setText(args.getString("NAME").toString())
            binding.inputType.setText(args.getString("LINE").toString())
            binding.edite.setOnClickListener {
                updateProfile()
            }
        }
    }

    private fun updateProfile() {
        val name = binding.inputName.text.toString().trim()
        val multyLine = binding.inputType.text.toString().trim()
        if (name.isNotEmpty() && multyLine.isNotEmpty()) {
            val profile = ProfileModel(currentProfile.id, name, multyLine)
            viewModel.updateProfile(profile)
            Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_editProfileFragment_to_homeFragment)
        } else {
            Toast.makeText(requireContext(), "Profile could not be updated", Toast.LENGTH_SHORT)
                .show()
        }
    }
}