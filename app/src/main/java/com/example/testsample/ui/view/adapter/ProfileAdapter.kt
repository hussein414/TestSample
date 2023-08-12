package com.example.testsample.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.testsample.databinding.SwitchItemBinding

class ProfileAdapter(private val context: Context) : Adapter<ProfileViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder =
        ProfileViewHolder(
            SwitchItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bindViews(context)
    }

    override fun getItemCount(): Int = 12
}