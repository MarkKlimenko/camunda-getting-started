package com.camunda.academy.insurance

import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@CucumberContextConfiguration
//@ZeebeSpringTest
class BpmnTest {

    val storage: MutableMap<String, String> = mutableMapOf()
}