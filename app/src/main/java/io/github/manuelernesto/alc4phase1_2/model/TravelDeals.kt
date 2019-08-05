package io.github.manuelernesto.alc4phase1_2.model

import java.io.Serializable

data class TravelDeals(
    var id: String? = null,
    val title: String? = null,
    val descripton: String? = null,
    val price: String? = null,
    val imageUrl: String? = null
) : Serializable