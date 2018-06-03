package com.example.microservices.discovery

import org.assertj.core.api.BDDAssertions.then
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.LocalServerPort
import org.junit.AfterClass
import org.springframework.boot.SpringApplication
import org.junit.BeforeClass
import org.junit.Test
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [DiscoveryClientApplication::class], webEnvironment = RANDOM_PORT)
class DiscoveryClientApplicationTests {

    @LocalServerPort
    private val port: Int = 0

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Test
    @Throws(InterruptedException::class)
    fun `should register client in eureka server`() {
        // registration has to take place...
        Thread.sleep(3000)

        val response = this.testRestTemplate.getForEntity("http://localhost:" + this.port + "/service-instances/a-bootiful-client", String::class.java)

        then(response.statusCode).isEqualTo(HttpStatus.OK)
        then(response.body).contains("a-bootiful-client")
    }

    @Configuration
    @EnableAutoConfiguration
    @EnableEurekaServer
    internal class EurekaServer

    companion object {
        lateinit var eurekaServer: ConfigurableApplicationContext

        @BeforeClass
        @JvmStatic
        fun startEureka() {
            eurekaServer = SpringApplication.run(EurekaServer::class.java,
                "--server.port=8761",
                "--eureka.instance.leaseRenewalIntervalInSeconds=1")
        }

        @AfterClass
        @JvmStatic
        fun closeEureka() {
            eurekaServer.close()
        }
    }
}