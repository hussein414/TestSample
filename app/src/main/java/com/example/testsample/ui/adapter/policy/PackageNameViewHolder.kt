package com.example.testsample.ui.adapter.policy

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.testsample.data.model.PolicyModel
import com.example.testsample.databinding.PackageNameItemBinding
import com.example.testsample.ui.event.OnItemClickListener
import com.example.testsample.ui.viewmodel.policy.PolicyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PackageNameViewHolder(val binding: PackageNameItemBinding) :
    ViewHolder(binding.root) {

    fun bindViews(model: PolicyModel, context: Context, onItemClickListener: OnItemClickListener) {
        binding.ApplicationName.text = model.shortName
        binding.selecting.isChecked = model.isSelected
        binding.selecting.setOnClickListener {
            val position=adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClicked(model, binding.selecting.isChecked)
            }
        }
    }
}