package com.example.testsample.ui.adapter.profile

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.testsample.data.model.ProfileModel
import com.example.testsample.databinding.SwitchItemBinding
import com.example.testsample.ui.event.DeleteClickListener
import com.example.testsample.utils.VpnCaller
import com.example.testsample.vpnclient.vpn.Constance


class ProfileAdapter(
    private val context: Context,
    private val deleteClickListener: DeleteClickListener
) : Adapter<ProfileViewHolder>() {
    private val vpnCaller = VpnCaller(context)
    var selectedItemId: Int? = null

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

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bindViews(context, currentItem, deleteClickListener)
        holder.binding.switchButton.onButton.setOnClickListener {
            holder.binding.switchButton.offButton.visibility = View.VISIBLE
            holder.binding.switchButton.onButton.visibility = View.GONE
            holder.binding.delete.visibility = View.VISIBLE
            if (Constance.isMyVpnServiceRunning) {
                vpnCaller.stopVpn()
                Constance.activeProfile = -1
            }
        }
        holder.binding.switchButton.offButton.setOnClickListener {
            holder.binding.switchButton.onButton.visibility = View.VISIBLE
            holder.binding.switchButton.offButton.visibility = View.GONE
            holder.binding.delete.visibility = View.INVISIBLE
            if (Constance.vpnPermission) {
                if (Constance.isMyVpnServiceRunning) {
                    vpnCaller.stopVpn()
                }
                Toast.makeText(context, currentItem.multiline, Toast.LENGTH_SHORT).show()
                vpnCaller.startVpn(currentItem.multiline)
                selectedItemId = currentItem.id
                Constance.activeProfile = selectedItemId as Int
                notifyDataSetChanged()
            }
        }
        val isItemSelected = currentItem.id == selectedItemId

        holder.binding.switchButton.onButton.visibility =
            if (isItemSelected) View.VISIBLE else View.GONE
        holder.binding.switchButton.offButton.visibility =
            if (isItemSelected) View.GONE else View.VISIBLE

        holder.binding.delete.visibility =
            if (isItemSelected) View.VISIBLE else View.INVISIBLE
        holder.binding.delete.visibility =
            if (isItemSelected) View.INVISIBLE else View.VISIBLE

    }

    override fun getItemCount(): Int = differ.currentList.size
}