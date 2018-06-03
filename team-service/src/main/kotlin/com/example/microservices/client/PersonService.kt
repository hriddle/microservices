package com.example.microservices.client

import com.example.microservices.rest.Person
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient("person-service")
interface PersonService {

    @GetMapping("/people")
    fun getAllPeople(): List<Person>
}