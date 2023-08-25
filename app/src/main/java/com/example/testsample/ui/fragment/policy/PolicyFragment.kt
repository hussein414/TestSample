package com.example.testsample.ui.fragment.policy

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testsample.R
import com.example.testsample.data.model.PolicyModel
import com.example.testsample.data.model.ProfileModel
import com.example.testsample.databinding.FragmentPolicyBinding
import com.example.testsample.ui.activites.MainActivity
import com.example.testsample.ui.adapter.policy.PackageNameAdapter
import com.example.testsample.ui.event.DeleteClickListener
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
        val packageNames = getApplicationsWithInternetAccess()
        for (packageName in packageNames) {
            val appName = packageName.name
            if (appName.isNotEmpty()) {
                val policyModel = PolicyModel(0, appName)
                policyViewModel.addPolicy(policyModel)
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

    private fun getApplicationsWithInternetAccess(): List<PolicyModel> {
        val packageManager = requireActivity().packageManager
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val appsWithInternetAccess = mutableListOf<PolicyModel>()

        for (appInfo in installedApps) {
            try {
                val packageInfo = packageManager.getPackageInfo(
                    appInfo.packageName,
                    PackageManager.GET_PERMISSIONS
                )
                val permissions = packageInfo.requestedPermissions
                if (permissions != null) {
                    for (permission in permissions) {
                        if (permission == Manifest.permission.INTERNET) {
                            // حذف بخش‌های مشخص از نام بسته
                            val packageNameWithoutPrefix = removePackagePrefix(appInfo.packageName)
                            val policyModel = PolicyModel(0, packageNameWithoutPrefix)
                            appsWithInternetAccess.add(policyModel)
                            break
                        }
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
        }

        return appsWithInternetAccess
    }

    private fun removePackagePrefix(packageName: String): String {
        val prefixToRemove = listOf("com.google.", "com.android.","com.android.android.","android.ext.")
        var cleanedName = packageName
        for (prefix in prefixToRemove) {
            if (packageName.startsWith(prefix)) {
                cleanedName = packageName.substring(prefix.length)
                break
            }
        }
        return cleanedName
    }
}