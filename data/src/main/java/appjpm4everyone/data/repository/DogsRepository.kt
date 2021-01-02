package appjpm4everyone.data.repository

import appjpm4everyone.data.dogsOut.DogsResponse
import appjpm4everyone.data.servicesInterfaz.DogsServicesImages
import java.io.IOException

class DogsRepository(private val dogsServicesImages: DogsServicesImages) {
    suspend fun getDogsImages(urlDogs: String): DogsResponse {
        return try{
            dogsServicesImages.dogsGetImages(urlDogs)
        } catch (e: IOException) {
            DogsResponse(e.message.toString(), emptyList())
        }
    }
}