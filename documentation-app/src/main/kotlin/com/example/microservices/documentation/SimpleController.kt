package com.example.microservices.documentation

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class SimpleController(private val discoveryClient: DiscoveryClient) {

    @Value("\${spring.application.name}")
    internal var appName: String? = null

    @GetMapping("/")
    fun homePage(model: Model): String {
        val urls = discoveryClient.services.map {
            val instanceInfo = discoveryClient.getInstances(it)[0] as EurekaDiscoveryClient.EurekaServiceInstance
            val host = instanceInfo.instanceInfo.homePageUrl
            val metadata = instanceInfo.metadata
            val docPath = metadata["documentationPath"] ?: ""
            val swaggerPath = metadata["swaggerPath"] ?: ""
            val appName = metadata["appDisplayName"] ?: it

            ApiInfo(
                displayName = appName,
                swaggerUrl = buildAbsoluteUrl(host, swaggerPath),
                documentationUrl = buildAbsoluteUrl(host, docPath)
            )
        }
        model.addAttribute("appName", appName)
        model.addAttribute("docLinks", urls)
        return "home"
    }

    private fun buildAbsoluteUrl(host: String, path: String?): String? =
        if (path.isNullOrEmpty()) null
        else host + path
}

data class ApiInfo(
    val displayName: String,
    val swaggerUrl: String?,
    val documentationUrl: String?
)