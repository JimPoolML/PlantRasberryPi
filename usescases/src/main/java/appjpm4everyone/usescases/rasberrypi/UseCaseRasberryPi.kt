package appjpm4everyone.usescases.rasberrypi

import appjpm4everyone.data.rasberryPiOut.DataRasberryOut
import appjpm4everyone.data.repository.RasberryPiRepository

class UseCaseRasberryPi (private val rasberryPiRepository: RasberryPiRepository){
    suspend fun invoke(telemetry: String): DataRasberryOut = rasberryPiRepository.getRasberryData(telemetry)
}