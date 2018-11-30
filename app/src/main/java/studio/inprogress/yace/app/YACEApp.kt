package studio.inprogress.yace.app

import android.app.Application
import com.arellomobile.mvp.MvpFacade
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import studio.inprogress.yace.app.di.ComponentManager
import timber.log.Timber

class YACEApp : Application() {
    var componentManager: ComponentManager = ComponentManager()
        private set

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Fabric.with(this, Crashlytics())
        initApp()
        initMVP()
        initDI()
    }

    private fun initApp() {
        app = this
    }

    private fun initMVP() {
        MvpFacade.init()
    }

    private fun initDI() {
        componentManager = ComponentManager()
    }

    companion object {

        var app: YACEApp? = null
            private set
    }
}
