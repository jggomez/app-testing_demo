package com.devhack.appdemofortests.test

import cucumber.api.CucumberOptions

@CucumberOptions(
    features = ["features"],
    glue = ["com.devhack.appdemofortests.steps"],
    tags = ["@e2e", "@smoke"]
)
@SuppressWarnings("unused")
class CucumberTestCase