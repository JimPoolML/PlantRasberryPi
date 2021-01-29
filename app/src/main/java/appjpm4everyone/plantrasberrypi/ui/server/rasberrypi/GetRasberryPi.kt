package appjpm4everyone.plantrasberrypi.ui.server.rasberrypi

import appjpm4everyone.data.dogsOut.DogsResponse
import appjpm4everyone.data.rasberryPiOut.DataRasberryOut
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface GetRasberryPi {
    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET
    fun getRasberryMagnitudes(@Url tem:String): Call<DataRasberryOut>
}