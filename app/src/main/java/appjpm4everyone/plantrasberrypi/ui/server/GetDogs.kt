package appjpm4everyone.plantrasberrypi.ui.server

import appjpm4everyone.data.dogsOut.DogsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface GetDogs {
    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET
    fun getCharacterByName(@Url url:String): Call<DogsResponse>

}