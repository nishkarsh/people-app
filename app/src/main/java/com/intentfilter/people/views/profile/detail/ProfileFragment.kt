package com.intentfilter.people.views.profile.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.intentfilter.people.DaggerPeopleComponent
import com.intentfilter.people.R
import com.intentfilter.people.databinding.FragmentProfileBinding
import com.intentfilter.people.utilities.Logger
import com.intentfilter.people.views.profile.ProfileViewModel
import com.intentfilter.people.views.profile.ProfileViewModelFactory
import javax.inject.Inject

class ProfileFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory

    private var binding: FragmentProfileBinding? = null
    private val logger = Logger.loggerFor(ProfileFragment::class)

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        binding = inflate(inflater, R.layout.fragment_profile, parent, false)
        binding?.lifecycleOwner = viewLifecycleOwner
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        DaggerPeopleComponent.builder().sharedPreferencesModule(SharedPreferencesModule(requireContext())).build()
            .inject(this)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
        viewModel.viewableProfile.observe(viewLifecycleOwner, Observer {
            binding?.profile = it
            logger.d("Got profile: $it")
        })
    }
}
