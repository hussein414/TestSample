package com.example.testsample.ui.view.fragment

import android.os.Bundle
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
import com.example.testsample.databinding.FragmentHomeBinding
import com.example.testsample.ui.view.adapter.ProfileAdapter
import com.example.testsample.ui.view.activites.MainActivity

import com.example.testsample.ui.viewmodel.ProfileViewModel


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    lateinit var profileAdapter: ProfileAdapter
    private lateinit var profileViewModel: ProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
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

                    else -> false
                }
            })
            popup.show()
        }

        binding.app.setOnClickListener {

        }

        profileAdapter = ProfileAdapter(requireContext())
        activity?.let {
            profileViewModel.getAllProfile().observe(viewLifecycleOwner) { note ->
                profileAdapter.differ.submitList(note)
            }
        }
        binding.recyclerView.apply {
            adapter = profileAdapter
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )
            setHasFixedSize(true)
        }
    }
}