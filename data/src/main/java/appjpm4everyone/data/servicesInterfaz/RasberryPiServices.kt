package appjpm4everyone.data.servicesInterfaz

import appjpm4everyone.data.rasberryPiOut.DataRasberryOut

interface RasberryPiServices {
    suspend fun rasberryPiMagnitudes(telemetry : String) : DataRasberryOut
}