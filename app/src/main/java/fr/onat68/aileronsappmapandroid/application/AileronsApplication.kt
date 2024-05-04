package fr.onat68.aileronsappmapandroid.application

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.maps.MapsInitializer
import dagger.hilt.android.HiltAndroidApp
import fr.onat68.aileronsappmapandroid.data.AppDatabase
import io.github.cdimascio.dotenv.dotenv
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

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