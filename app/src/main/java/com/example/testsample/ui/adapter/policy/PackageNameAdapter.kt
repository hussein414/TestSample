package com.example.testsample.ui.adapter.policy

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.example.testsample.data.model.PolicyModel
import com.example.testsample.databinding.PackageNameItemBinding
import com.example.testsample.ui.event.OnItemClickListener

class PackageNameAdapter(
    private val context: Context,
    private val onItemClickListener: OnItemClickListener
) : Adapter<PackageNameViewHolder>() {


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
        holder.bindViews(currentItem, context, onItemClickListener)
    }

    override fun getItemCount(): Int = differ.currentList.size

}