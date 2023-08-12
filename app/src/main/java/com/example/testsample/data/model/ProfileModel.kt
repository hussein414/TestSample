package com.example.testsample.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class ProfileModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val multiline: String
) : Parcelable
