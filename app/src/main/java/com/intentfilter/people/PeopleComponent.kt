package com.intentfilter.people

import android.content.Context
import com.intentfilter.people.providers.GatewayModule
import com.intentfilter.people.providers.SharedPreferencesModule
import com.intentfilter.people.views.profile.detail.ProfileFragment
import com.intentfilter.people.views.profile.edit.EditProfileFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [GatewayModule::class, SharedPreferencesModule::class])
@Singleton
interface PeopleComponent {
    fun inject(editProfileFragment: EditProfileFragment)

    fun inject(profileFragment: ProfileFragment)

    fun inject(splashFragment: SplashFragment)

    @Component.Factory
    interface Factory {
        fun newInstance(@BindsInstance context: Context): PeopleComponent
    }
}