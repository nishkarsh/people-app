package com.intentfilter.people.views.profile

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

object ProfileBindingAdapter {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, uri: String?) {
        uri?.let { Picasso.with(view.context).load(uri).into(view) }
    }
}