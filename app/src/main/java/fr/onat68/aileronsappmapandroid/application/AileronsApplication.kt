package fr.onat68.aileronsappmapandroid.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AileronsApplication : Application() {
    companion object {
        // WARNING: Do not use this for UI
        lateinit var instance: AileronsApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

    }
}