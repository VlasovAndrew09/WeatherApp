package ru.vlasov.weatherapp.data.remote

import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.Response

@Singleton
class RequestInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url
            .newBuilder()
            .addQueryParameter(
                ApiConstants.API_KEY_QUERY,
                ApiConstants.API_KEY_VALUE
            )
            .addQueryParameter(
                ApiConstants.WEATHER_UNIT_QUERY,
                ApiConstants.WEATHER_UNIT_VALUE
            )
            .build()
        val request = chain.request().newBuilder().url(url).build()
        return chain.proceed(request)
    }
}
