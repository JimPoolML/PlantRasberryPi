package appjpm4everyone.data.repository

import appjpm4everyone.data.rasberryPiOut.DataRasberryOut
import appjpm4everyone.data.rasberryPiOut.Params
import appjpm4everyone.data.servicesInterfaz.RasberryPiServices

class RasberryPiRepository(private val rasberryPiServices: RasberryPiServices) {
    suspend fun getRasberryData(telemetry: String): DataRasberryOut {
        return try {
            return rasberryPiServices.rasberryPiMagnitudes(telemetry)
        } catch (e: Exception) {
            DataRasberryOut(
                0, e.message!!, Params(
                    0.0, 0.0, 0.0,
                    humidity = false,
                    temperature = false,
                    vibration = 0.0
                )
            )
        }
    }
}