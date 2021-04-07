package com.devhack.appdemofortests.runner

import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner
import com.devhack.appdemofortests.BuildConfig
import cucumber.api.android.CucumberInstrumentationCore

class CucumberTestRunner : AndroidJUnitRunner() {

    companion object {
        private const val CUCUMBER_TAGS_KEY = "tags"
    }

    private val instrumentationCore = CucumberInstrumentationCore(this)

    override fun onCreate(arguments: Bundle?) {

        val tags = BuildConfig.tags
        println("tags")
        if (tags.isNotEmpty()) {
            println(tags)
            arguments?.putString(CUCUMBER_TAGS_KEY, tags.replace("\\s", ""))
            arguments?.putString(CUCUMBER_TAGS_KEY, "@e2e")
        }

        instrumentationCore.create(arguments)
        super.onCreate(arguments)
    }

    override fun onStart() {
        waitForIdleSync()
        instrumentationCore.start()
    }
}
