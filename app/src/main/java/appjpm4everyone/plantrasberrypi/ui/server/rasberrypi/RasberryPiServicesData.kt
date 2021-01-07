package appjpm4everyone.plantrasberrypi.ui.server.rasberrypi

import appjpm4everyone.data.rasberryPiOut.DataRasberryOut
import appjpm4everyone.data.servicesInterfaz.RasberryPiServices
import appjpm4everyone.plantrasberrypi.ui.server.ServiceManager
import retrofit2.await

class RasberryPiServicesData : RasberryPiServices {

    override suspend fun rasberryPiMagnitudes(telemetry: String): DataRasberryOut =
        ServiceManager.serviceRasberry.getRasberryMagnitudes("rpc?timeout=$telemetry").await()

}