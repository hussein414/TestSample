package com.example.testsample.ui.adapter.profile

import android.content.Context
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.testsample.R
import com.example.testsample.data.model.ProfileModel

import com.example.testsample.databinding.SwitchItemBinding
import com.example.testsample.ui.activites.MainActivity
import com.example.testsample.ui.event.DeleteClickListener
import com.example.testsample.utils.VpnCaller
import com.example.testsample.vpnclient.vpn.Constance


class ProfileViewHolder(private val binding: SwitchItemBinding) : ViewHolder(binding.root) {

    fun bindViews(
        context: Context,
        model: ProfileModel,
        deleteClickListener: DeleteClickListener,
    ) {
        val vpnCaller = VpnCaller(context)
        binding.apply {
            switchButton.onButton.setOnClickListener {
                binding.switchButton.offButton.visibility = View.VISIBLE
                binding.switchButton.onButton.visibility = View.GONE
                binding.delete.visibility = View.VISIBLE
                val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerViewProfile)

            }
            switchButton.offButton.setOnClickListener {
                binding.switchButton.onButton.visibility = View.VISIBLE
                binding.switchButton.offButton.visibility = View.GONE
                binding.delete.visibility = View.INVISIBLE
                if (Constance.vpnPermission) {
                    vpnCaller.startVpn()
                }
            }
            parent.setOnClickListener {
                val popup = PopupMenu(context, binding.parent)
                val inflater: MenuInflater = popup.menuInflater
                inflater.inflate(R.menu.item_menu, popup.menu)
                popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.status -> {
                            binding.root.findNavController()
                                .navigate(R.id.action_homeFragment_to_statusProfileFragment)
                            return@OnMenuItemClickListener true
                        }

                        R.id.edit -> {
                            controllerEdit(model)
                            return@OnMenuItemClickListener true
                        }

                        else -> false
                    }
                })
                popup.show()
            }
            binding.delete.setOnClickListener {
                deleteClickListener.onProfileDeleteClickItem(model)
            }
            name.text = model.name
        }
    }

    private fun controllerEdit(model: ProfileModel) {
        val bundle = Bundle().apply {
            putParcelable("profile_key", model)
            putString("NAME", model.name)
            putString("LINE", model.multiline)
        }
        binding.root.findNavController()
            .navigate(R.id.action_homeFragment_to_editProfileFragment, bundle)
    }

}