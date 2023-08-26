package com.example.testsample.ui.fragment.policy

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testsample.R
import com.example.testsample.data.model.PolicyModel
import com.example.testsample.databinding.FragmentPolicyBinding
import com.example.testsample.ui.activites.MainActivity
import com.example.testsample.ui.adapter.policy.PackageNameAdapter
import com.example.testsample.ui.event.OnItemClickListener
import com.example.testsample.ui.viewmodel.policy.PolicyViewModel


class PolicyFragment : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentPolicyBinding
    private lateinit var policyViewModel: PolicyViewModel
    private lateinit var policyAdapter: PackageNameAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPolicyBinding.inflate(inflater, container, false)
        bindViews()
        return binding.root
    }

    private fun bindViews() {
        policyViewModel = (activity as MainActivity).policyViewModel
        getPackageName()

    }

    private fun getPackageName() {
        //policyViewModel.deleteAll()
        val packageNames = getPhoneUsersApplications()
        for (packageName in packageNames) {
            val appName = packageName.shortName
            if (appName.isNotEmpty()) {
                policyViewModel.checkDuplicate(appName).observe(viewLifecycleOwner) { isDuplicate ->
                    if (!isDuplicate) {
                        val policyModel =
                            PolicyModel(0, packageName.shortName, packageName.appName)
                        policyViewModel.addPolicy(policyModel)
                    }
                }
            } else {
                Toast.makeText(requireContext(), "i cant find package name", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        activity?.let {
            policyViewModel.getAllPolicy().observe(viewLifecycleOwner) { note ->
                policyAdapter.differ.submitList(note)
            }
        }
        policyAdapter = PackageNameAdapter(requireContext(), this)
        binding.recyclerViewSelected.apply {
            adapter = policyAdapter
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )
            setHasFixedSize(true)
        }
    }

    private fun getPhoneUsersApplications(): List<PolicyModel> {
        val packageManager = requireActivity().packageManager
        val installedApps = packageManager.getInstalledApplications(0)
        val phoneApps = mutableListOf<PolicyModel>()

        for (appInfo in installedApps) {
            try {
                if (appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0)
                    continue
                val packageInfo = packageManager.getPackageInfo(
                    appInfo.packageName,
                    PackageManager.GET_PERMISSIONS
                )
                val permissions = packageInfo.requestedPermissions
                if (permissions != null) {
                    for (permission in permissions) {
                        val shortname = packageManager.getApplicationLabel(appInfo).toString()
                        if (permission == Manifest.permission.INTERNET && !shortname.isEmpty()) {
                            val policyModel = PolicyModel(0, shortname, appInfo.packageName, false)
                            phoneApps.add(policyModel)
                        }
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
        }
        phoneApps.sortBy { it.shortName }
        return phoneApps
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemClicked(item: PolicyModel, isChecked: Boolean) {
        policyViewModel.onTaskCheckedChanged(policyModel = item, isChecked)
        if (isChecked) {
            binding.adding.visibility = View.VISIBLE
            binding.adding.animation =
                AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)
            val bundle = Bundle().apply {
                putParcelable("PolicyModel", item)
                putString("shortName", item.shortName)
                putString("longName", item.appName)
                putBoolean("isSelected", item.isSelected)
            }
            binding.adding.setOnClickListener {
                findNavController().navigate(R.id.action_policyFragment_to_homeFragment, bundle)
                Toast.makeText(requireContext(), "Added${item.shortName}", Toast.LENGTH_SHORT).show()
            }
        } else {
            binding.adding.visibility = View.GONE
        }
    }
}