package com.example.microservices.rest

import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

private const val people = "/people"

@RestController
@RequestMapping(people)
class PersonController {

    @GetMapping("")
    @ResponseStatus(OK)
    fun getPeople(): List<Person> = listOf(
        Person(id = 1, name = "First Last", productHistory = listOf(1, 2)),
        Person(id = 2, name = "First Last", productHistory = listOf(1, 2)),
        Person(id = 3, name = "First Last", productHistory = listOf(1, 2)),
        Person(id = 4, name = "First Last", productHistory = listOf(1, 2)),
        Person(id = 5, name = "First Last", productHistory = listOf(1, 2))
    )

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    fun getPerson(@PathVariable id: Long): Person =
        Person(id = id, name = "First Last", productHistory = listOf(1, 2))

}

data class Person(
    val id: Long,
    val name: String,
    val role: String = "UNKNOWN",
    val level: String = "UNKNOWN",
    val productHistory: List<Long> = emptyList()
)