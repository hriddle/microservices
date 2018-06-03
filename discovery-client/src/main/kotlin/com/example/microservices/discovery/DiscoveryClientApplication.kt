package com.example.microservices.discovery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.cloud.client.ServiceInstance
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@EnableDiscoveryClient
@SpringBootApplication
class DiscoveryClientApplication

fun main(args: Array<String>) {
    runApplication<DiscoveryClientApplication>(*args)
}

@RestController
internal class ServiceInstanceRestController(private val discoveryClient: DiscoveryClient) {

    @GetMapping("/service-instances")
    fun serviceInstances(): List<ServiceInstance> =
        discoveryClient.services.flatMap {
            discoveryClient.getInstances(it)
        }

    @RequestMapping("/service-instances/{applicationName}")
    fun serviceInstancesByApplicationName(@PathVariable applicationName: String): List<ServiceInstance> =
        this.discoveryClient.getInstances(applicationName)
}