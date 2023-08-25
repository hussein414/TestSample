package com.example.testsample.ui.event

import com.example.testsample.data.model.ProfileModel

interface OnItemClickListener {
    fun onItemClick(item: ProfileModel, position: Int)
}