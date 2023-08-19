package com.example.testsample.ui.event

import com.example.testsample.data.model.PolicyModel
import com.example.testsample.data.model.ProfileModel

interface DeleteClickListener {
    fun onProfileDeleteClickItem(profileModel: ProfileModel)
    fun onPolicyDeleteClickItem(policyModel: PolicyModel)
}