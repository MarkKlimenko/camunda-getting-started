package com.camunda.academy.insurance;

import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Deployment(resources = "classpath*:/bpmn/*.bpmn")
public class BpmnLauncherApplication {

    public static void main(String[] args) {
        SpringApplication.run(BpmnLauncherApplication.class, args);
    }
}
