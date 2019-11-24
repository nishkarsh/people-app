package com.intentfilter.people


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.intentfilter.people.utilities.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashFragment : Fragment() {
    @Inject
    lateinit var preferences: Preferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.Default) {
            DaggerPeopleComponent.factory().newInstance(requireContext()).inject(this@SplashFragment)

            delay(500)

            when (preferences.getProfile()) {
                null -> findNavController().navigate(R.id.action_splashFragment_to_editProfileFragment)
                else -> findNavController().navigate(R.id.action_splashFragment_to_profileFragment)
            }
        }
    }
}