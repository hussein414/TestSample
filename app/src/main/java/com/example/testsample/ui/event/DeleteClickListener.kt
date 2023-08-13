package com.example.testsample.ui.event

import com.example.testsample.data.model.ProfileModel

interface DeleteClickListener {
    fun onDeleteClickItem(profileModel: ProfileModel)
}