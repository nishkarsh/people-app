package com.intentfilter.people.validators

import android.annotation.SuppressLint
import androidx.annotation.IdRes
import androidx.annotation.StringRes

@SuppressLint("UseSparseArrays")
class ValidationResult : HashMap<@IdRes Int, @StringRes Int>()
