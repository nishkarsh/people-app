package com.intentfilter.people.views.profile

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.intentfilter.people.R
import com.squareup.picasso.Picasso

object ProfileBindingAdapter {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, uri: String?) {
        val height = view.context.resources.getDimensionPixelSize(R.dimen.profile_picture_min_height)
        uri?.let { Picasso.with(view.context).load(uri).resize(view.width, height).centerCrop().into(view) }
    }
}