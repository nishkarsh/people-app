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
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE
import com.intentfilter.people.DaggerPeopleComponent
import com.intentfilter.people.R
import com.intentfilter.people.databinding.FragmentProfileBinding
import com.intentfilter.people.models.ViewableProfile
import com.intentfilter.people.utilities.Logger
import com.intentfilter.people.views.profile.ProfileViewModel
import com.intentfilter.people.views.profile.ProfileViewModelFactory
import kotlinx.android.synthetic.main.layout_profile_main_section.*
import javax.inject.Inject

class ProfileFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory

    private lateinit var unbinder: Unbinder
    private lateinit var snackBar: Snackbar
    private lateinit var binding: FragmentProfileBinding
    private val logger = Logger.loggerFor(ProfileFragment::class)

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        binding = inflate(inflater, R.layout.fragment_profile, parent, false)
        binding.lifecycleOwner = viewLifecycleOwner

        unbinder = ButterKnife.bind(this, binding.root)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeLoaderSnackbar()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        DaggerPeopleComponent.factory().newInstance(requireContext()).inject(this)

        ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java).apply {
            val viewableProfileObserver = Observer<ViewableProfile> { profile ->
                logger.d("Got viewable profile: $profile")
                binding.profile = profile

                mainSection.visibility = View.VISIBLE
                snackBar.dismiss()
            }

            viewableProfile.observe(viewLifecycleOwner, viewableProfileObserver)
            error.observe(viewLifecycleOwner, Observer { error -> onError(error) })
        }
    }

    @OnClick(R.id.editIcon)
    fun editProfile() {
        findNavController().navigate(R.id.editProfileFragment)
    }

    override fun onDestroyView() {
        unbinder.unbind()
        super.onDestroyView()
    }

    private fun ProfileViewModel.onError(error: String) {
        snackBar.setText(error)
            .setAction(R.string.label_retry) { triggerFetch(); initializeLoaderSnackbar() }
            .show()
    }

    private fun initializeLoaderSnackbar() {
        Snackbar.make(requireView(), R.string.message_loading, LENGTH_INDEFINITE).also {
            snackBar = it; it.show()
        }
    }
}
