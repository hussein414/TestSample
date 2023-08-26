package com.example.testsample.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testsample.R
import com.example.testsample.data.model.PolicyModel
import com.example.testsample.data.model.ProfileModel
import com.example.testsample.databinding.FragmentHomeBinding
import com.example.testsample.ui.adapter.profile.ProfileAdapter
import com.example.testsample.ui.event.DeleteClickListener
import com.example.testsample.ui.activites.MainActivity
import com.example.testsample.ui.viewmodel.policy.PolicyViewModel

import com.example.testsample.ui.viewmodel.profile.ProfileViewModel
import com.example.testsample.utils.Constance


class HomeFragment : Fragment(), DeleteClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var profileViewModel: ProfileViewModel
    lateinit var policyViewModel: PolicyViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        bindViews()
        return binding.root
    }

    private fun bindViews() {
        profileViewModel = (activity as MainActivity).profileViewModel
        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addProfileFragment)
        }
        binding.poUpMenu.setOnClickListener {
            val popup = PopupMenu(requireContext(), binding.poUpMenu)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.popup_menu, popup.menu)
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentHost) as NavHostFragment?
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.aboutFragment -> {
                        navHostFragment!!.findNavController()
                            .navigate(R.id.action_homeFragment_to_aboutFragment)
                        return@OnMenuItemClickListener true
                    }

                    R.id.donateFragment -> {
                        navHostFragment!!.findNavController()
                            .navigate(R.id.action_homeFragment_to_donateFragment)
                        return@OnMenuItemClickListener true
                    }

                    R.id.policy -> {
                        navHostFragment!!.findNavController()
                            .navigate(R.id.action_homeFragment_to_policyFragment)
                        return@OnMenuItemClickListener true
                    }

                    else -> false
                }
            })
            popup.show()
        }

        binding.app.setOnClickListener {

        }

        profileAdapter = ProfileAdapter(requireContext(), this)
        activity?.let {
            profileViewModel.getAllProfile().observe(viewLifecycleOwner) { note ->
                profileAdapter.differ.submitList(note)
            }
        }
        binding.recyclerViewProfile.apply {
            adapter = profileAdapter
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )
            setHasFixedSize(true)
        }
    }



    override fun onProfileDeleteClickItem(profileModel: ProfileModel) {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Profile")
            setMessage("Are you sure to delete this profile?")
            setPositiveButton("Yes") { _, _ ->
                profileViewModel.deleteProfile(profileModel)
            }
            setNegativeButton("No", null)
        }.create().show()
    }

    override fun onPolicyDeleteClickItem(policyModel: PolicyModel) {
        TODO("Not yet implemented")
    }

    override fun onStart() {
        super.onStart()
        policyViewModel = (activity as MainActivity).policyViewModel
        activity?.let {
            policyViewModel.getSelectedPolicy().observe(viewLifecycleOwner) { string ->
                Constance.policyList = string as ArrayList<String>
            }
        }
        if (Constance.isMyVpnServiceRunning && Constance.activeProfile != -1) {
            profileAdapter.selectedItemId = Constance.activeProfile
        }
    }
}