package ru.vlasov.weatherapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vlasov.weatherapp.data.WeatherRepositoryImpl
import ru.vlasov.weatherapp.domain.WeatherRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository
}