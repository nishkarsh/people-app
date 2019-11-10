package com.intentfilter.people.views.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.intentfilter.people.R

class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, parent, false)
    }
}
