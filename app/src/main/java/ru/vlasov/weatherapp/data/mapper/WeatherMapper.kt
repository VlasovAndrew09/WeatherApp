package ru.vlasov.weatherapp.data.mapper

import com.faskn.app.weatherapp.domain.model.WeatherItemDto
import com.faskn.app.weatherapp.domain.model.WeatherMainDto
import ru.vlasov.weatherapp.data.remote.ApiConstants
import ru.vlasov.weatherapp.data.remote.model.*
import ru.vlasov.weatherapp.domain.model.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

fun CurrentWeatherResponseDto.toCurrentWeatherResponse() = CurrentWeatherResponse(
    cod = cod,
    name = name,
    main = main.toWeatherMain(),
    weather = weather.toListWeatherItem(),
    wind = wind.toWind(),
    coord = coord.toCoord()
)

fun ForecastResponseDto.toForecastResponse() = ForecastResponse(
    city = city.toCity(),
    cnt = cnt,
    cod = cod,
    message = message,
    list = list.toListForecastListItem(),
    listDays = list.toListForecastDayItem()
)

fun List<ForecastListItemDto>.toListForecastListItem(): List<ForecastListItem> {
    return map { it.toForecastListItem() }
}

fun List<ForecastListItemDto>.toListForecastDayItem(): List<ForecastDayItem> {
    val listForecastDayItem = arrayListOf<ForecastDayItem>()
    val daysForFilter = arrayListOf<String>()
    this.forEachIndexed { _, listItem ->
        val day = SimpleDateFormat("dd", Locale.getDefault()).format(listItem.dt * 1000)
        if (daysForFilter.contains(day).not()) {
            daysForFilter.add(day)
        }
    }
    if (daysForFilter.size == 6) {
        daysForFilter.removeAt(5)
    }
    daysForFilter.forEach { dayForFilter ->
        val listForecastListItemDto = this.filter {
            SimpleDateFormat(
                "dd",
                Locale.getDefault()
            ).format(it.dt * 1000) == dayForFilter
        }
        val day = SimpleDateFormat(
            "d MMMM",
            Locale.getDefault()
        ).format(listForecastListItemDto[0].dt * 1000).toString()
        val dayOfWeek = SimpleDateFormat(
            "EEEE",
            Locale.getDefault()
        ).format(listForecastListItemDto[0].dt * 1000).toString()
            .replaceFirstChar(Char::uppercase)
        val tempMin = "${listForecastListItemDto.minOf { it.main.temp }.roundToInt()}°"
        val tempMax = "${listForecastListItemDto.maxOf { it.main.temp }.roundToInt()}°"
        val listForecastListItem = listForecastListItemDto.map { it.toForecastListItem() }
        listForecastDayItem.add(
            ForecastDayItem(
                day,
                dayOfWeek,
                tempMin,
                tempMax,
                listForecastListItem
            )
        )
    }
    return listForecastDayItem
}

fun ForecastListItemDto.toForecastListItem() = ForecastListItem(
    dt = dt,
    main = main.toWeatherMain(),
    weather = weather.toListWeatherItem(),
    wind = wind.toWind(),
    time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(dt * 1000).toString()
)


fun List<WeatherItemDto>.toListWeatherItem(): List<WeatherItem> {
    return map { it.toWeatherItem() }
}

fun WeatherMainDto.toWeatherMain() = WeatherMain(
    temp = "${temp.roundToInt()}°",
    humidity = "$humidity%",
    pressure = "${(pressure * 0.75).roundToInt()} мм.рт.ст"
)

fun WeatherItemDto.toWeatherItem() = WeatherItem(
    id = id,
    main = main,
    description = description.replaceFirstChar(Char::uppercase),
    icon = "${ApiConstants.IMAGE_URL}$icon@4x.png"
)

fun WindDto.toWind() = Wind(
    speed = "${speed.roundToInt()} м/с",
    deg = deg.toString(),
    gust = "${gust.roundToInt()} м/с"
)

fun CityDto.toCity() = City(
    id = id,
    name = name,
    coord = coord.toCoord(),
    country = country,
    sunrise = sunrise,
    sunset = sunset
)

fun CoordDto.toCoord() = Coord(
    lat = lat,
    lon = lon
)