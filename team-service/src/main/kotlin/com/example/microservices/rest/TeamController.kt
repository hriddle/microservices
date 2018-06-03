package com.example.microservices.rest

import com.fasterxml.jackson.annotation.JsonFormat
import org.slf4j.LoggerFactory
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus.OK
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.time.LocalDate

private const val team = "/teams"

@RestController
@RequestMapping(team)
class TeamController(private val teamService: TeamService) {

    private val log = LoggerFactory.getLogger(TeamController::class.java)

    @GetMapping("")
    @ResponseStatus(OK)
    fun getTeams(): List<Team> = teamService.getAllTeams()
        .also { log.info("Getting teams...") }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    fun getTeam(@PathVariable id: Long): Team = teamService.getTeamById(id)
        .also { log.info("Getting team $id") }
}

@Service
class TeamService(private val discoveryClient: DiscoveryClient, private val restTemplate: RestTemplate) {

    private val log = LoggerFactory.getLogger(TeamService::class.java)

    private val peopleServiceUrl: String
        get() = discoveryClient.getInstances("person-service")[0].uri.toString()
    private val productServiceUrl: String
        get() = discoveryClient.getInstances("product-service")[0].uri.toString()

    fun getAllTeams(): List<Team> {
        val people = restTemplate.exchange("http://person-service/people/", HttpMethod.GET, null, object : ParameterizedTypeReference<List<Person>>() {}).body
            ?: emptyList()
        val products = restTemplate.exchange("http://product-service/products/", HttpMethod.GET, null, object : ParameterizedTypeReference<List<Product>>() {}).body
            ?: emptyList()
        val teamMap: Map<Long, MutableList<Person>> = products.map { it.id to mutableListOf<Person>() }.toMap()
        people.forEach { person ->
            person.productHistory.forEach { productId ->
                teamMap[productId]?.add(person)
            }
        }
        return teamMap.map { (productId, people) ->
            Team(
                id = productId,
                productName = products.find { it.id == productId }!!.name,
                team = people.map { it.name }
            )
        }.also { log.info("Compiling team from people on all products ... ")}
    }

    fun getTeamById(id: Long): Team {
        val people = restTemplate.exchange("http://person-service/people/", HttpMethod.GET, null, object : ParameterizedTypeReference<List<Person>>() {}).body
            ?: emptyList()
        val product = restTemplate.getForObject("http://product-service/products/$id", Product::class.java) ?: Product()
        return Team(
            id = product.id,
            productName = product.name,
            team = people.filter { it.productHistory.contains(product.id) }.map { it.name }
        ).also { log.info("Compiling team from people on product $id ... ")}
    }
}

data class Team(
    val id: Long,
    val productName: String,
    val team: List<String>
)

data class Person(
    val id: Long = -1,
    val name: String = "",
    val role: String = "UNKNOWN",
    val level: String = "UNKNOWN",
    val productHistory: List<Long> = emptyList()
)

data class Product(
    val id: Long = -1,
    val name: String = "",
    val description: String = "",
    val techStack: List<String> = emptyList(),
    @JsonFormat(pattern = "yyyy-MM-dd")
    val startDate: LocalDate = LocalDate.now()
)