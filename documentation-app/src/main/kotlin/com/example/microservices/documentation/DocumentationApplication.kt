package com.example.microservices.documentation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@SpringBootApplication
class DocumentationApplication

fun main(args: Array<String>) {
    runApplication<DocumentationApplication>(*args)
}