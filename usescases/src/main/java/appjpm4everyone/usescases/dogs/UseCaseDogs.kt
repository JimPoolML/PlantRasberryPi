package appjpm4everyone.usescases.dogs

import appjpm4everyone.data.dogsOut.DogsResponse
import appjpm4everyone.data.repository.DogsRepository

class UseCaseDogs(private val dogsRepository: DogsRepository){
    suspend fun invoke(urlDogs: String): DogsResponse = dogsRepository.getDogsImages(urlDogs)
}
