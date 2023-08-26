package com.example.testsample.ui.adapter.policy

import android.content.Context
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.testsample.data.model.PolicyModel
import com.example.testsample.databinding.PackageNameItemBinding

class PackageNameViewHolder(private val binding: PackageNameItemBinding) : ViewHolder(binding.root) {

    fun bindViews(model: PolicyModel, selectedItems: MutableList<PolicyModel>, context: Context) {
        binding.ApplicationName.text = model.shortName
        binding.root.setOnClickListener {
            if (selectedItems.contains(model)) {
                selectedItems.remove(model)
                binding.selecting.isChecked = false
            } else {
                selectedItems.add(model)
                binding.selecting.isChecked = true
            }
        }
    }
}