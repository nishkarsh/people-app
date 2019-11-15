package com.intentfilter.people.utilities

import android.content.SharedPreferences
import androidx.core.content.edit

class Preferences(private val sharedPreferences: SharedPreferences) {

    fun isProfileCreated(): Boolean {
        return !sharedPreferences.getString(PROFILE_ID, null).isNullOrEmpty()
    }

    fun saveProfile(profileId: String) {
        sharedPreferences.edit { putString(PROFILE_ID, profileId) }
    }

    fun getProfile(): String? {
        return sharedPreferences.getString(PROFILE_ID, null)
    }

    companion object Keys {
        const val PROFILE_ID = "PROFILE_ID"
    }
}