package com.api.prayerapi.response

data class Method(
    val id: Int,
    val location: Location,
    val name: String,
    val params: Params
)