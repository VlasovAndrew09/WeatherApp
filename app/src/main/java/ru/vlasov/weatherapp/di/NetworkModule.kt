package ru.vlasov.weatherapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vlasov.weatherapp.data.remote.ApiConstants.BASE_URL
import ru.vlasov.weatherapp.data.remote.RequestInterceptor
import ru.vlasov.weatherapp.data.remote.WeatherApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .addInterceptor(RequestInterceptor())

    @Provides
    @Singleton
    fun provideWeatherApi(okHttpClientBuilder: OkHttpClient.Builder): WeatherApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
}