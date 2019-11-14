package com.intentfilter.people.views.profile

import android.content.Context
import android.widget.ArrayAdapter
import com.intentfilter.people.models.City

class LocationsAdapter(context: Context, cities: Array<City>) :
    ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, cities.map { it.name })