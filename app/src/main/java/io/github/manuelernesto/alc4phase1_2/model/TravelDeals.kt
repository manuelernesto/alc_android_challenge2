package io.github.manuelernesto.alc4phase1_2.model

import java.io.Serializable

data class TravelDeals(
    var id: String? = null,
    var title: String? = null,
    var descripton: String? = null,
    var price: String? = null,
    var imageUrl: String? = null
) : Serializable