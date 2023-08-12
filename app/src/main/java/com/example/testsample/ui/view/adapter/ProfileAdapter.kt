package com.example.testsample.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.testsample.data.model.ProfileModel
import com.example.testsample.databinding.SwitchItemBinding


class ProfileAdapter(private val context: Context) : Adapter<ProfileViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<ProfileModel>() {
        override fun areItemsTheSame(oldItem: ProfileModel, newItem: ProfileModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProfileModel, newItem: ProfileModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder =
        ProfileViewHolder(
            SwitchItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bindViews(context, currentItem)
    }

    override fun getItemCount(): Int = differ.currentList.size
}