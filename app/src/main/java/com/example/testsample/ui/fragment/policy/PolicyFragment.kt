package com.example.testsample.ui.fragment.policy

import android.Manifest
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testsample.data.model.PolicyModel
import com.example.testsample.databinding.FragmentPolicyBinding
import com.example.testsample.ui.activites.MainActivity
import com.example.testsample.ui.adapter.policy.PackageNameAdapter
import com.example.testsample.ui.viewmodel.policy.PolicyViewModel


class PolicyFragment : Fragment() {
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
        policyViewModel.deleteAll()
        val packageNames = getPhoneUsersApplications()
        for (packageName in packageNames) {
            val appName = packageName.shortName
            if (appName.isNotEmpty()) {
                policyViewModel.checkDuplicate(appName).observe(viewLifecycleOwner){isDuplicate->
                    if (!isDuplicate){
                        val policyModel = PolicyModel(0, packageName.shortName,packageName.appName,false)
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
        policyAdapter = PackageNameAdapter(requireContext())
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
                            val policyModel = PolicyModel(0, shortname,appInfo.packageName,false)
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
}