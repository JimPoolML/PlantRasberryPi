package appjpm4everyone.plantrasberrypi.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import appjpm4everyone.plantrasberrypi.utils.Event
import appjpm4everyone.plantrasberrypi.utils.ScopedViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TIME_SLEEP: Long = 2000

class SplashViewModel : ScopedViewModel() {

    private val _navigateToLogin = MutableLiveData<Event<Long>>()
    val navigateToLogin: LiveData<Event<Long>> get() = _navigateToLogin

    private val _version = MutableLiveData<String>()
    val version: LiveData<String> get() = _version

    init {
        onGetVersion()
    }

    private fun onGetVersion() {
        launch {
            _version.value = onStringVersion()
            delay(TIME_SLEEP)
            _navigateToLogin.value = Event(0)
        }
    }

    private fun onStringVersion() = "1.0"

}
