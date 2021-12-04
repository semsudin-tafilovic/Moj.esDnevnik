package rs.tafilovic.mojesdnevnik

import androidx.multidex.MultiDexApplication
import rs.tafilovic.mojesdnevnik.di.AppComponent
import rs.tafilovic.mojesdnevnik.di.AppModule
import rs.tafilovic.mojesdnevnik.di.DaggerAppComponent
import javax.inject.Singleton

class MyApp : MultiDexApplication() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    @Singleton
    fun appComponent() = appComponent
}