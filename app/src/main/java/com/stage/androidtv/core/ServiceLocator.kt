package com.stage.androidtv.core

import android.content.Context

object ServiceLocator {
    lateinit var appContext: Context
        private set

    fun init(context: Context) {
        if (!::appContext.isInitialized) {
            appContext = context.applicationContext
        }
    }
}


