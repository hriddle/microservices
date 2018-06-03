package com.example.microservices.client

import com.example.microservices.rest.Product
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient("product-service")
interface ProductService {

    @GetMapping("/products")
    fun getAllProducts(): List<Product>

    @GetMapping("/products/{id}")
    fun getProductById(@PathVariable id: Long): Product
}