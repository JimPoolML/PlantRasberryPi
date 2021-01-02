package appjpm4everyone.plantrasberrypi.di.component

import android.app.Application
import appjpm4everyone.plantrasberrypi.di.module.AppModule
import appjpm4everyone.plantrasberrypi.di.module.SdkModule
import appjpm4everyone.plantrasberrypi.ui.main.MainActivityComponent
import appjpm4everyone.plantrasberrypi.ui.main.MainActivityModule

import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, SdkModule::class])
interface AppComponent {

    fun plus(module: MainActivityModule): MainActivityComponent

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }

}