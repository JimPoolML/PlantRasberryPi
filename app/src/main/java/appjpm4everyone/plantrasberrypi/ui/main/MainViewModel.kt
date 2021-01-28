package appjpm4everyone.plantrasberrypi.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import appjpm4everyone.plantrasberrypi.utils.ScopedViewModel
import appjpm4everyone.data.dogsOut.DogsResponse
import appjpm4everyone.data.rasberryPiOut.DataRasberryOut
import appjpm4everyone.usescases.dogs.UseCaseDogs
import appjpm4everyone.usescases.rasberrypi.UseCaseRasberryPi
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel(
    private val useCaseDogs: UseCaseDogs,
    private val useCaseRasberryPi: UseCaseRasberryPi
) : ScopedViewModel() {

    sealed class UiModel {
        object Loading : UiModel()
        class ShowDogsError(var errorStatus: String) : UiModel()
        class ShowRasberryError(var errorException: String) : UiModel()
        object ShowEmptyList : UiModel()
        class ShowDogList(var dogsList: List<String>) : UiModel()
        class ShowRasberryData(var dataRasberryOut: DataRasberryOut, var environmentState: Int) :
            UiModel()
    }

    private val _model = MutableLiveData<UiModel>()
    val modelChooseBusiness: LiveData<UiModel> get() = _model

    private lateinit var responseDogs: DogsResponse
    private lateinit var responseRasberryPi: DataRasberryOut
    private var environmentState: Int = 0
    private var tempCounter: Int = 0
    private var humCounter: Int = 0


    fun searchDogByName(queryDogs: String) {
        //Show loading
        _model.value = UiModel.Loading
        onRequestDogs(queryDogs)
    }

    //Coroutines
    private fun onRequestDogs(queryDogs: String) = launch {
        try {
            val response = useCaseDogs.invoke(queryDogs)
            responseDogs = response
            verifyDogList(responseDogs)
        } catch (e: IOException) {
            e.printStackTrace()
            _model.value = UiModel.ShowDogsError(e.message.orEmpty())
        }
    }

    private fun verifyDogList(responseDogs: DogsResponse) {
        when {
            responseDogs.status != "success" -> {
                //Server error
                tempCounter = 0
                humCounter = 0
                environmentState = 0
                _model.value = UiModel.ShowDogsError(responseDogs.status)
            }
            responseDogs.message.isNullOrEmpty() -> {
                //Empty list
                _model.value = UiModel.ShowEmptyList
            }
            else -> {
                _model.value = UiModel.ShowDogList(responseDogs.message)
            }
        }
    }

    fun getValuesRasberry(telemetry: String) {
        //Show loading
        onRequestRasberrry(telemetry)
    }

    //Coroutines
    private fun onRequestRasberrry(telemetry: String) = launch {
        try {
            val response = useCaseRasberryPi.invoke(telemetry)
            responseRasberryPi = response
            verifyDataRasberry(responseRasberryPi)
        } catch (e: IOException) {
            e.printStackTrace()
            _model.value = UiModel.ShowDogsError(e.message.orEmpty())
        }
    }

    private fun verifyDataRasberry(responseRasberryPi: DataRasberryOut) {
        when {
            responseRasberryPi.method != "notifyUser" -> {
                //Server error
                _model.value = UiModel.ShowRasberryError(responseRasberryPi.method)
            }
            else -> {
                setEnvironmentState(responseRasberryPi)
                _model.value = UiModel.ShowRasberryData(responseRasberryPi, environmentState)
            }
        }
    }

    private fun setEnvironmentState(responseRasberryPi: DataRasberryOut) {
        if (responseRasberryPi.params!!.temperature) {
        //if (responseRasberryPi.params!!.temperatureSource >25) {
            //Add counter
            tempCounter++
        } else {
            //Restart counter
            tempCounter = 0;
        }
        if (responseRasberryPi.params!!.humidity) {
            //Add counter
            humCounter++
        } else {
            //Restart counter
            humCounter = 0;
        }

        if (tempCounter >= 5 && humCounter >= 5) {
            environmentState = 3
        } else if (tempCounter >= 5 && humCounter < 5) {
            environmentState = 2
        } else if (tempCounter < 5 && humCounter >= 5) {
            environmentState = 1
        } else if (tempCounter < 5 && humCounter < 5) {
            environmentState = 0
        }

    }
}


