package rs.tafilovic.mojesdnevnik.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import rs.tafilovic.mojesdnevnik.repository.Repository
import rs.tafilovic.mojesdnevnik.rest.ApiClient
import rs.tafilovic.mojesdnevnik.rest.SessionManager
import rs.tafilovic.mojesdnevnik.util.PrefsHelper
import java.io.File
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Singleton
    @Provides
    fun provideSessionManager(): SessionManager {
        return SessionManager(providePrefsHelper())
    }

    @Singleton
    @Provides
    fun providePrefsHelper(): PrefsHelper {
        return PrefsHelper.getInstance(app)
    }

    @Singleton
    @Provides
    fun provideRepository(): Repository {
        return Repository(provideApiClient(), provideSessionManager())
    }

    @Provides
    fun provideContext(): Context {
        return app
    }

    @Singleton
    @Provides
    fun provideCookies(): String {
        return provideRepository().cookies
    }

    @Singleton
    @Provides
    fun provideCacheDir(): File {
        return app.cacheDir
    }

    @Singleton
    @Provides
    fun provideApiClient(): ApiClient {
        return ApiClient(provideCacheDir())
    }
}