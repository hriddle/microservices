package com.example.microservices.rest

import com.example.microservices.client.PersonService
import com.example.microservices.client.ProductService
import com.fasterxml.jackson.annotation.JsonFormat
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.OK
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/teams")
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
class TeamService(private val personService: PersonService, private val productService: ProductService) {

    private val log = LoggerFactory.getLogger(TeamService::class.java)

    fun getAllTeams(): List<Team> {
        val people = personService.getAllPeople()
        val products = productService.getAllProducts()

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
        }.also { log.info("Compiling teams from people on all products ... ")}
    }

    fun getTeamById(id: Long): Team {
        val people = personService.getAllPeople()
        val product = productService.getProductById(id)
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