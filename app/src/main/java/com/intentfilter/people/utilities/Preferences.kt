package com.intentfilter.people.utilities

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

open class Preferences @Inject constructor(private val sharedPreferences: SharedPreferences) {

    open fun isProfileCreated(): Boolean {
        return !sharedPreferences.getString(PROFILE_ID, null).isNullOrEmpty()
    }

    open fun saveProfile(profileId: String) {
        sharedPreferences.edit { putString(PROFILE_ID, profileId) }
    }

    open fun getProfile(): String? {
        return sharedPreferences.getString(PROFILE_ID, null)
    }

    companion object Keys {
        const val PROFILE_ID = "PROFILE_ID"
    }
}