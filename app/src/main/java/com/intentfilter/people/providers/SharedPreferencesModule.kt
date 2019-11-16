package com.intentfilter.people.providers

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPreferencesModule {

    @Provides
    @Singleton
    fun providePreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("intentfilter", Context.MODE_PRIVATE)
    }
}