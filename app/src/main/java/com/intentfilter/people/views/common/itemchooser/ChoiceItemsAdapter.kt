package com.intentfilter.people.views.common.itemchooser

import android.content.Context
import android.widget.ArrayAdapter
import com.intentfilter.people.models.NamedAttribute

class ChoiceItemsAdapter(
    context: Context, resource: Int, private val objects: Array<NamedAttribute>
) : ArrayAdapter<String>(context, resource) {

    override fun getItem(position: Int): String? {
        return objects[position].name
    }

    override fun getCount(): Int {
        return objects.size
    }
}