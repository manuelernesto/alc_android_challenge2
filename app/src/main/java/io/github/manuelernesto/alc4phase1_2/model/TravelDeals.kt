package io.github.manuelernesto.alc4phase1_2.model

import java.io.Serializable

data class TravelDeals(
    val id: String?,
    val title: String?,
    val descripton: String?,
    val price: String?,
    val imageUrl: String?
) : Serializable