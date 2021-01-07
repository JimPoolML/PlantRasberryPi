package appjpm4everyone.plantrasberrypi.di.module

import appjpm4everyone.data.repository.DogsRepository
import appjpm4everyone.data.repository.RasberryPiRepository
import appjpm4everyone.data.servicesInterfaz.DogsServicesImages
import appjpm4everyone.data.servicesInterfaz.RasberryPiServices
import dagger.Module
import dagger.Provides

@Module
class SdkModule {
    @Provides
    fun dogsImagesProvider(dogsServicesImages: DogsServicesImages) = DogsRepository(dogsServicesImages)

    @Provides
    fun rasberryPisProvider(rasberryPiServices: RasberryPiServices) = RasberryPiRepository(rasberryPiServices)
}