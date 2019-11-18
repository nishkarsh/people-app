# people-app
This is a sample android application with a purpose of demonstrating a sample profile page that is editable.

## Building the Project

You would need an android emulator to test this application. The DEV build variant points to 10.0.2.2 which would point to the localhost where ProfileService should be running.

- Build: `./gradlew build`
- Unit Test: `./gradlew test`
- Android Instrumentation Test: `./gradlew connectedAndroidTest` [Emulator needs to be up]

***Please make sure to use QA build variant while running instrumentation tests. The URLs in QA product flavour points to a stub MockWebServer.***

***Please also make sure that the animations are disabled on the emulator while running the tests***

Note: This app needs `Profile Service`[https://github.com/nishkarsh/profile-service] to be up and running on local. When this service is up, it listens for connections on port 9740.

### Functionalities
- First time user lands on the EditProfileFragment in Create mode.
- Profile picture could be selected along with entry of information in other fields.
- The dynamic storage permission is handled for selecting picture.
- The validation for required fields is handled. It kicks in when the user taps on Create/Save profile. The state is cleared on changing the values.
- The information is retained across orientation change.
- When user taps on Create/Save, the POST /profile call is made and a profile is created on the server. The profile ID is returned and kept in SharedPreferences.
- The user is taken to ProfileFragment that displays the details and a Edit Floating Action Button.
- On clicking of Floating Action Button, the EditProfileFragment opens up in Edit mode.
- The snackbar displays the status, whether loading or an error.
- Display of Snackbar with retry when the network calls fail to fetch the profile.

***All these functionalities have been tested on Android emulator running API 29 - Pixel 3a***

### Technologies Used / Configuration
- Min SDK version: 21
- Target SDK version: 29
- Proguard is not enabled
- Testing: Junit 5 with Mockito for Unit and Espresso for UI testing
- Language & Components : Kotlin with Coroutines
- Patterns: View Models, Data Binding, Dagger 2 for DI, Butterknife for View Injection
- Networking: Retrofit with OkHttp

Note: The app also has a dependency on `com.intentfilter:android-permissions:2.0.49` which is an open source library written by me. This library helps in easily dealing with dynamic permissions.


### Things kept in consideration
- Theme colors match SN brand colors.
- Handled configuration change (use ViewModel)
- Used retrofit with kotlin coroutines and lifecycle extension
- Number of Unit Tests: 40
- Number of UI Test: 1 (Checks the most important Create Flow)

#### Known Issues / Scope for Improvements
- The state when network calls fail is not handled very appropriately. The FAB and the Create/Save button is not disabled when the network calls fail.
- The loading state could be handled better with an in place loader.
- The location autocomplete is not validated for the input value other than the possible choice from within the list. If an invalid value is kept, the generic error shows up when user attempts to create/edit profile.
- The code could be refactored at places. If I have to, the first thing that I would do is split the ProfileViewModel into two.
- The configuration change retains the values for all the fields except for when the `SingleAttributeChooserFragment` is open and then the screen is rotated. There is a possiblity to handle this scenario, right now it was compromised because of the possiblity to reuse code.
