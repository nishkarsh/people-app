package com.intentfilter.people

import com.intentfilter.people.providers.GatewayModule
import com.intentfilter.people.views.profile.EditProfileFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [GatewayModule::class])
@Singleton
interface PeopleComponent {
    fun inject(editProfileFragment: EditProfileFragment)
}