package appjpm4everyone.plantrasberrypi.utils

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import appjpm4everyone.domain.common.Scope

abstract class ScopedViewModel : ViewModel(), Scope by Scope.Implementation() {

    init {
        this.initScope()
    }

    @CallSuper
    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }
}