package appjpm4everyone.plantrasberrypi

import android.app.Application
import appjpm4everyone.plantrasberrypi.di.component.AppComponent
import appjpm4everyone.plantrasberrypi.di.component.DaggerAppComponent
import timber.log.Timber

class PlantRasberryPiApp : Application() {

    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        component = DaggerAppComponent
            .factory()
            .create(this)
    }
}