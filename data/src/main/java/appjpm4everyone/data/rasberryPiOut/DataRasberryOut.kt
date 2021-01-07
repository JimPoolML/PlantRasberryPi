package appjpm4everyone.data.rasberryPiOut

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataRasberryOut(
    @SerializedName(value = "id", alternate = ["Id"])
    @Expose
    var id:Int,
    @SerializedName(value = "method", alternate = ["Method"])
    @Expose
    var method: String,
    @SerializedName(value = "params", alternate = ["Params"])
    @Expose
    var params: Params?
){
    constructor() : this(0, "", null )
}

