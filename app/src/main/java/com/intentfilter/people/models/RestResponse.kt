package com.intentfilter.people.models

import androidx.lifecycle.MutableLiveData

class RestResponse<T> {
    val error = MutableLiveData<Exception>()
    val success = MutableLiveData<T>()
}