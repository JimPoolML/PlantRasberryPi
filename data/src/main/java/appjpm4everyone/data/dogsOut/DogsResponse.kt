package appjpm4everyone.data.dogsOut

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DogsResponse (
    @SerializedName(value = "status", alternate = ["Status"])
    @Expose
    var status:String,
    @SerializedName(value = "message", alternate = ["Message"])
    @Expose
    var message: List<String>
){
    constructor() : this("", emptyList() )
}
