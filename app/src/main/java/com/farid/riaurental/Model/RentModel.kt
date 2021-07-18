package com.farid.riaurental.Model

data class RentModel(
    val type: Int = 1,
    val name: String = "",
    val detail: String = "",
    val price: Int = 0,
    val address: String = "",
    val duration: String = "",
    val ownerId: String = "",
    val createdAt: String = "",
    val quantity: Int = 0,
    val pictures: MutableList<Any> = mutableListOf(),
    val id: String = "")