package com.intentfilter.people.views.profile

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.intentfilter.people.views.common.CircleTransform
import com.squareup.picasso.Picasso

object ProfileBindingAdapter {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, uri: String?) {
        uri?.let { Picasso.with(view.context).load(uri).fit().centerCrop().into(view) }
    }

    @JvmStatic
    @BindingAdapter("roundImageUrl")
    fun loadImageRounded(view: ImageView, uri: String?) {
        uri?.let { Picasso.with(view.context).load(uri).transform(CircleTransform()).fit().centerCrop().into(view) }
    }
}