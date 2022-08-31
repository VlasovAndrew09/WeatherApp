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
    wind = wind.toWind()
)

fun ForecastResponseDto.toForecastResponse() = ForecastResponse(
    city = city.toCity(),
    cnt = cnt,
    cod = cod,
    message = message,
    list = list.toListForecastListItem()
)

fun List<ForecastListItemDto>.toListForecastListItem(): List<ForecastListItem> {
    return map { it.toForecastListItem() }
}

fun ForecastListItemDto.toForecastListItem() = ForecastListItem(
    dt = dt,
    main = main.toWeatherMain(),
    weather = weather.toListWeatherItem(),
    wind = wind.toWind(),
    dtTxt = SimpleDateFormat("dd.MM HH:mm").format(dt * 1000).toString()
)


fun List<WeatherItemDto>.toListWeatherItem(): List<WeatherItem> {
    return map { it.toWeatherItem() }
}

fun WeatherMainDto.toWeatherMain() = WeatherMain(
    temp = "${temp.roundToInt()}°",
    humidity = "$humidity%",
    pressure = "${(pressure*0.75).roundToInt()} мм.рт.ст"
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