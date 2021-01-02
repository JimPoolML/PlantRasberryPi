package appjpm4everyone.plantrasberrypi.di.module

import appjpm4everyone.data.repository.DogsRepository
import appjpm4everyone.data.servicesInterfaz.DogsServicesImages
import dagger.Module
import dagger.Provides

@Module
class SdkModule {
    @Provides
    fun dogsImagesProvider(dogsServicesImages: DogsServicesImages) = DogsRepository(dogsServicesImages)
}