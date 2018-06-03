package com.example.microservices.client

import com.example.microservices.rest.Person
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod.GET
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class PersonService(private val restTemplate: RestTemplate) {

    fun getAllPeople(): List<Person> = restTemplate.exchange(
        "http://person-service/people/", GET, null, object : ParameterizedTypeReference<List<Person>>() {}
    ).body ?: emptyList()
}