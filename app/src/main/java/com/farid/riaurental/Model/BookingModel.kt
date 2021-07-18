package com.farid.riaurental.Model

data class BookingModel(
    val rentId: String = "",
    val tenantId: String = "",
    val ownerId: String = "",
    val rentDate: String = "",
    val rentDuration: Int = 0,
    val price: Int = 0,
    val rentCode: String = "",
    val createdAt: String = "",
    val id: String = "")