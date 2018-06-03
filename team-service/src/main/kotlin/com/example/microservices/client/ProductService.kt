package com.example.microservices.client

import com.example.microservices.rest.Product
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod.GET
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.client.RestTemplate

@Service
class ProductService(private val restTemplate: RestTemplate) {

    fun getAllProducts(): List<Product> = restTemplate.exchange(
        "http://product-service/products/", GET, null, object : ParameterizedTypeReference<List<Product>>() {}
    ).body ?: emptyList()

    fun getProductById(@PathVariable id: Long): Product =
        restTemplate.getForObject("http://product-service/products/$id", Product::class.java) ?: Product()
}