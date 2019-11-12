package com.intentfilter.people.views.common.itemchooser

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.intentfilter.people.BuildConfig
import com.intentfilter.people.models.NamedAttribute
import org.parceler.Parcels

class SingleAttributeChooserFragment : DialogFragment(), DialogInterface.OnClickListener {
    private lateinit var viewModel: SingleAttributeChooserViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = title()
        val namedAttributes = namedAttributes()

        viewModel = ViewModelProvider(this).get(SingleAttributeChooserViewModel::class.java).apply {
            setNamedAttributes(namedAttributes)
        }

        val itemsAdapter = ChoiceItemsAdapter(context!!, android.R.layout.simple_list_item_1, namedAttributes)

        return AlertDialog.Builder(context).setTitle(title)
            .setSingleChoiceItems(itemsAdapter, -1, this)
            .create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        viewModel.selectAttribute(which)
        this.viewModelStore.clear()
        dismiss()
    }

    private fun title() = arguments?.getString(EXTRA_TITLE)

    private fun namedAttributes() =
        Parcels.unwrap<List<NamedAttribute>>(arguments?.getParcelable<Parcelable>(EXTRA_ATTRIBUTES)).toTypedArray()

    companion object {
        const val EXTRA_ATTRIBUTES = BuildConfig.APPLICATION_ID + ".namedAttributes"
        const val EXTRA_TITLE = BuildConfig.APPLICATION_ID + ".title"

        fun newInstance(namedAttributes: Array<NamedAttribute>, title: String): SingleAttributeChooserFragment {
            return SingleAttributeChooserFragment().apply {

                arguments = Bundle().apply {
                    putParcelable(EXTRA_ATTRIBUTES, Parcels.wrap(namedAttributes.toList()))
                    putString(EXTRA_TITLE, title)
                }
            }
        }
    }
}