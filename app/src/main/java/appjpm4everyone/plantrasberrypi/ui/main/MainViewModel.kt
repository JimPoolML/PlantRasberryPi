package appjpm4everyone.plantrasberrypi.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import appjpm4everyone.plantrasberrypi.utils.ScopedViewModel
import appjpm4everyone.data.dogsOut.DogsResponse
import appjpm4everyone.usescases.dogs.UseCaseDogs
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel(private val useCaseDogs: UseCaseDogs) : ScopedViewModel(){

    sealed class UiModel {
        object Loading : UiModel()
        class ShowDogsError(var errorStatus: String) : UiModel()
        object ShowEmptyList : UiModel()
        class ShowDogList(var dogsList: List<String>) : UiModel()
    }

    private val _model = MutableLiveData<UiModel>()
    val modelChooseBusiness: LiveData<UiModel> get() = _model

    private lateinit var responseDogs : DogsResponse

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
}

