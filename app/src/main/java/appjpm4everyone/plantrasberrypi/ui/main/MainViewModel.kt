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
import java.sql.SQLSyntaxErrorException

class MainViewModel(
    private val useCaseDogs: UseCaseDogs,
    private val useCaseRasberryPi: UseCaseRasberryPi
) : ScopedViewModel(){

    sealed class UiModel {
        object Loading : UiModel()
        class ShowDogsError(var errorStatus: String) : UiModel()
        class ShowRasberryError(var errorException: String) : UiModel()
        object ShowEmptyList : UiModel()
        class ShowDogList(var dogsList: List<String>) : UiModel()
        class ShowRasberryData(var dataRasberryOut: DataRasberryOut) : UiModel()
    }

    private val _model = MutableLiveData<UiModel>()
    val modelChooseBusiness: LiveData<UiModel> get() = _model

    private lateinit var responseDogs : DogsResponse
    private lateinit var responseRasberryPi: DataRasberryOut

    fun searchDogByName(queryDogs: String) {
        //Show loading
        _model.value = UiModel.Loading
        onRequestDogs(queryDogs)
    }

    //Coroutines
    private fun onRequestDogs(queryDogs: String) = launch{
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
    private fun onRequestRasberrry(telemetry: String) = launch{
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
                _model.value = UiModel.ShowRasberryData(responseRasberryPi)
            }
        }
    }
}

