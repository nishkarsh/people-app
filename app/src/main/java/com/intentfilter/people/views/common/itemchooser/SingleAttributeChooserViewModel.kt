package com.intentfilter.people.views.common.itemchooser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intentfilter.people.models.NamedAttribute

class SingleAttributeChooserViewModel : ViewModel() {
    private lateinit var namedAttributes: Array<NamedAttribute>
    val selectedAttribute = MutableLiveData<NamedAttribute>()

    fun setNamedAttributes(namedAttributes: Array<NamedAttribute>) {
        this.namedAttributes = namedAttributes
    }

    fun selectAttribute(position: Int) {
        selectedAttribute.value = namedAttributes[position]
    }
}