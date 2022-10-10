package com.example.bkservice.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(val name :String, val author : String): Parcelable {
}