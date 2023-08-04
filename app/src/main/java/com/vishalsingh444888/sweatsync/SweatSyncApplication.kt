package com.vishalsingh444888.sweatsync

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SweatSyncApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }
}