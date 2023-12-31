package com.example.testsample.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class PolicyModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val shortName: String,
    val appName: String,
    var isSelected: Boolean = false,
) : Parcelable
