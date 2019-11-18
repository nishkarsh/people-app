package com.intentfilter.people.views.profile.edit

import android.widget.Button
import android.widget.DatePicker
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions.setDate
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.intentfilter.people.R
import com.intentfilter.people.models.ViewableProfile
import com.intentfilter.people.views.HomeActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify

@LargeTest
@RunWith(AndroidJUnit4::class)
internal class CreateProfileTest {
    @get:Rule
    var activityRule: ActivityTestRule<HomeActivity> = ActivityTestRule(HomeActivity::class.java)

    private lateinit var viewableProfile: ViewableProfile

    @Before
    fun setUp() {
        InstrumentationRegistry.getInstrumentation().targetContext.deleteSharedPreferences("intentfilter")

        viewableProfile = ViewableProfile(
            "Nishkarsh", "Nishkarsh Sharma", null,
            "10 November, 2000", "Male", "Mixed", "Hindu", "125.0",
            "Athletic", "Never Married", "Applications Developer",
            "I am a cool person. I mean really. I like to try new things and would do anything at least once for the experience of it.",
            "Abidjan"
        )
    }

    @Test
    fun createProfileAndNavigateToProfileScreen() {
        val navController = Mockito.mock(NavController::class.java)
        val createProfileScenario = launchFragmentInContainer<EditProfileFragment>()

        createProfileScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.editDisplayName)).perform(typeText(viewableProfile.displayName))
        onView(withId(R.id.editRealName)).perform(typeText(viewableProfile.actualFullName))
        onView(withId(R.id.viewBirthday)).perform(click())

        onView(withClassName(equalTo(DatePicker::class.java.name))).perform(setDate(1992, 1, 30))
        onView(withText("OK")).perform(click())

        onView(withId(R.id.viewLocation)).perform(scrollTo(), typeText(viewableProfile.location), closeSoftKeyboard())

        onView(withId(R.id.viewGender)).perform(scrollTo(), click())
        onData(equalTo(viewableProfile.gender)).perform(click())

        onView(withId(R.id.viewEthnicity)).perform(scrollTo(), click())
        onData(equalTo(viewableProfile.ethnicity)).perform(click())

        onView(withId(R.id.viewReligion)).perform(scrollTo(), click())
        onData(equalTo(viewableProfile.religion)).perform(click())

        onView(withId(R.id.editHeight)).perform(scrollTo(), typeText(viewableProfile.height), closeSoftKeyboard())

        onView(withId(R.id.viewFigureType)).perform(scrollTo(), click())
        onData(equalTo(viewableProfile.figureType)).perform(click())

        onView(withId(R.id.editOccupation)).perform(scrollTo(), typeText(viewableProfile.occupation), closeSoftKeyboard())
        onView(withId(R.id.editAboutMe)).perform(scrollTo(), typeText(viewableProfile.aboutMe), closeSoftKeyboard())

        onView(allOf(withClassName(equalTo(Button::class.java.name)), withText("Create"))).perform(scrollTo(), click())

        onView(withText("This is a mandatory field"))
            .check(matches(isDisplayed()))
            .check(isCompletelyBelow(withId(R.id.viewMaritalStatus)))

        onView(withId(R.id.viewMaritalStatus)).perform(scrollTo(), click())
        onData(equalTo(viewableProfile.maritalStatus)).perform(click())

        onView(allOf(withClassName(equalTo(Button::class.java.name)), withText("Create"))).perform(scrollTo(), click())

        verify(navController).navigate(R.id.action_editProfileFragment_to_profileFragment)
    }
}