package appjpm4everyone.plantrasberrypi.ui.server.dogs

import appjpm4everyone.plantrasberrypi.ui.server.ServiceManager
import appjpm4everyone.data.dogsOut.DogsResponse
import appjpm4everyone.data.servicesInterfaz.DogsServicesImages
import retrofit2.await


class DogsServicesData : DogsServicesImages {

    override suspend fun dogsGetImages(queryDogs: String): DogsResponse =
        ServiceManager.serviceDogs.getCharacterByName("$queryDogs/images").await()

}

