package com.intentfilter.people.views.profile

import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.intentfilter.people.R
import com.intentfilter.people.validators.ValidationResult
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

    @JvmStatic
    @BindingAdapter("validateAgainst")
    fun setError(view: TextInputLayout, errors: ValidationResult?) {
        errors?.get(view.id)?.let { errorResId ->
            view.error = view.context.getString(errorResId)
        }
    }

    @JvmStatic
    @BindingAdapter("required")
    fun makeRequired(view: TextInputLayout, required: Boolean) {
        if (required) {
            val errorRequired = view.context?.getString(R.string.error_required)

            view.editText?.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) = when {
                    text.isEmpty() -> view.error = errorRequired
                    else -> view.error = null
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable) {}
            })
        }
    }
}