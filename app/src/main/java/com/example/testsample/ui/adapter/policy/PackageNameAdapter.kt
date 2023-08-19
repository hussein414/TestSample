package com.example.testsample.ui.adapter.policy

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.testsample.data.model.PolicyModel
import com.example.testsample.databinding.PackageNameItemBinding

class PackageNameAdapter(private val context: Context) : Adapter<PackageNameViewHolder>() {

    private val selectedItems = mutableListOf<PolicyModel>()


    private val differCallback = object : DiffUtil.ItemCallback<PolicyModel>() {
        override fun areItemsTheSame(oldItem: PolicyModel, newItem: PolicyModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PolicyModel, newItem: PolicyModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageNameViewHolder =
        PackageNameViewHolder(
            PackageNameItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: PackageNameViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bindViews(currentItem, selectedItems, context)
    }

    override fun getItemCount(): Int = differ.currentList.size


    fun getSelectedItems(): List<PolicyModel> {
        return selectedItems
    }
}