package appjpm4everyone.data.servicesInterfaz

import appjpm4everyone.data.dogsOut.DogsResponse

interface DogsServicesImages {
    suspend fun dogsGetImages(queryDogs : String) : DogsResponse
}