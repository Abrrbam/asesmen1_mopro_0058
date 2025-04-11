package com.abrahamputra0058.asesment1.ui.model

import androidx.annotation.DrawableRes

data class Agenda(
    val id: Long,
    val judul: String,
    val tipe: String,
    val tanggal: String,
    val waktu: String,
    val deskripsi: String,
    @DrawableRes val imageResId: Int
)
