package com.example.testsample.ui.event

import com.example.testsample.data.model.PolicyModel
import com.example.testsample.data.model.ProfileModel

interface OnItemClickListener {
    fun onItemClicked(item: PolicyModel, isChecked: Boolean)
}