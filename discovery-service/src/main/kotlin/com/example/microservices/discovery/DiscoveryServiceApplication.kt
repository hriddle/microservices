package com.example.microservices.discovery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@EnableEurekaServer
@SpringBootApplication
class DiscoveryServiceApplication

fun main(args: Array<String>) {
    runApplication<DiscoveryServiceApplication>(*args)
}

@RestController
internal class DiscoveryServiceController(val discoveryClient: DiscoveryClient) {

    @GetMapping("/service-instances")
    fun serviceInstances(): List<ServiceInstance> =
        discoveryClient.services.flatMap { discoveryClient.getInstances(it) }

    @GetMapping("/service-instances/{appName}")
    fun serviceInstanceByAppName(@PathVariable appName: String): List<ServiceInstance> =
        discoveryClient.getInstances(appName)
}