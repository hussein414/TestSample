package com.example.testsample.ui.view.adapter

import android.content.Context
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.testsample.R
import com.example.testsample.data.model.ProfileModel

import com.example.testsample.databinding.SwitchItemBinding


class ProfileViewHolder(private val binding: SwitchItemBinding) : ViewHolder(binding.root) {
    fun bindViews(context: Context, model: ProfileModel) {
        binding.apply {
            switchButton.onButton.setOnClickListener {
                binding.switchButton.offButton.visibility = View.VISIBLE
                binding.switchButton.onButton.visibility = View.GONE
            }
            switchButton.offButton.setOnClickListener {
                binding.switchButton.onButton.visibility = View.VISIBLE
                binding.switchButton.offButton.visibility = View.GONE

            }
            parent.setOnLongClickListener {
                val popup = PopupMenu(context, binding.parent)
                val inflater: MenuInflater = popup.menuInflater
                inflater.inflate(R.menu.item_menu, popup.menu)
                popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.status -> {

                            return@OnMenuItemClickListener true
                        }

                        R.id.edit -> {

                            return@OnMenuItemClickListener true
                        }

                        R.id.delete -> {

                            return@OnMenuItemClickListener true
                        }

                        else -> false
                    }
                })
                popup.show()
                true
            }
            name.text = model.name
        }
    }
}