package appjpm4everyone.data.rasberryPiOut

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Params (
    @SerializedName(value = "distanceSource", alternate = ["DistanceSource"])
    @Expose
    var distanceSource: Double,
    @SerializedName(value = "temperatureSource", alternate = ["TemperatureSource"])
    @Expose
    var temperatureSource:Double,
    @SerializedName(value = "humiditySource", alternate = ["HumiditySource"])
    @Expose
    var humiditySource:Double,

    @SerializedName(value = "humidity", alternate = ["Humidity"])
    @Expose
    var humidity:Boolean,
    @SerializedName(value = "temperature", alternate = ["Temperature"])
    @Expose
    var temperature:Boolean,
    @SerializedName(value = "vibration", alternate = ["Vibration"])
    @Expose
    var vibration:Double
){
    constructor() : this(0.0,0.0,0.0, false, false, 0.0 )
}
