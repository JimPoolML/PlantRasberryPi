package appjpm4everyone.plantrasberrypi.di.module

import android.app.Application
import android.content.Context
import appjpm4everyone.plantrasberrypi.ui.server.dogs.DogsServicesData
import appjpm4everyone.data.servicesInterfaz.DogsServicesImages
import appjpm4everyone.data.servicesInterfaz.RasberryPiServices
import appjpm4everyone.plantrasberrypi.ui.server.rasberrypi.RasberryPiServicesData
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context = application

    @Provides
    fun dogsDataDataProvider(): DogsServicesImages = DogsServicesData()

    @Provides
    fun rasberryPiDataDataProvider(): RasberryPiServices = RasberryPiServicesData()

}