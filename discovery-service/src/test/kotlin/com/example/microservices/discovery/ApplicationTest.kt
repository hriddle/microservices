package com.example.microservices.discovery

import org.assertj.core.api.BDDAssertions.then
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DiscoveryServiceApplicationTests {

    @LocalServerPort
    private val port: Int = 0

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun `should start discovery server`() {
        val entity = this.testRestTemplate.getForEntity(
            "http://localhost:" + this.port + "/eureka/apps", String::class.java)

        then(entity.statusCode).isEqualTo(HttpStatus.OK)
    }
}