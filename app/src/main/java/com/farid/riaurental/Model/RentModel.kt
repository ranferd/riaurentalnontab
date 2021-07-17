package com.farid.riaurental.Model

data class RentModel(
    val id: String = "",
    val type: Int = 1,
    val name: String = "",
    val detail: String = "",
    val price: Int = 0,
    val address: String = "",
    val duration: String = "",
    val ownerID: String = "",
    val createdAt: String = "",
    val pictures: MutableList<Any> = mutableListOf()
) {

}