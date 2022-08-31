package ru.vlasov.weatherapp.domain.location

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?

    fun checkSelfPermissions(): Boolean

    fun checkGpsEnabled(): Boolean
}