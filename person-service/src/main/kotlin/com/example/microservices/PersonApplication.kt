package com.example.microservices

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@SpringBootApplication
class PersonApplication

fun main(args: Array<String>) {
    runApplication<PersonApplication>(*args)
}