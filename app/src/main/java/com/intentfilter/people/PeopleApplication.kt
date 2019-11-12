package com.intentfilter.people

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class PeopleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}