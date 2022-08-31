package ru.vlasov.weatherapp.domain.model

data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)
