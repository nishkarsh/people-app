package com.intentfilter.people.views.profile.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
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

    private lateinit var unbinder: Unbinder
    private lateinit var binding: FragmentProfileBinding
    private val logger = Logger.loggerFor(ProfileFragment::class)

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        binding = inflate(inflater, R.layout.fragment_profile, parent, false)
        binding.lifecycleOwner = viewLifecycleOwner

        unbinder = ButterKnife.bind(this, binding.root)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        DaggerPeopleComponent.factory().newInstance(requireContext()).inject(this)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
        viewModel.viewableProfile.observe(viewLifecycleOwner, Observer {
            logger.d("Fetched profile from server: $it. Initializing view.")
            binding.profile = it
        })
    }

    @OnClick(R.id.editIcon)
    fun editProfile() {
        findNavController().navigate(R.id.editProfileFragment)
    }

    override fun onDestroyView() {
        unbinder.unbind()
        super.onDestroyView()
    }
}
