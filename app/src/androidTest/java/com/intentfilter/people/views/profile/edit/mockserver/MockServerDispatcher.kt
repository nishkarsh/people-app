package com.intentfilter.people.views.profile.edit.mockserver

import com.intentfilter.people.views.profile.edit.utils.ClasspathUtil
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

internal class RequestDispatcher : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        if (request.path == "/en/single_choice_attributes.json") {
            return MockResponse().setResponseCode(200).setBody(readFromFile("sample-choice-attributes.json"))
        } else if (request.path == "/en/locations/cities.json") {
            return MockResponse().setResponseCode(200).setBody(readFromFile("cities-slice.json"))
        } else if (request.path == "/profile") {
            return MockResponse().setResponseCode(201).setBody(readFromFile("sample-profile-response.json"))
        }

        return MockResponse().setResponseCode(404)
    }

    private fun readFromFile(path: String): String {
        return ClasspathUtil.fromClassPath(path)
    }
}