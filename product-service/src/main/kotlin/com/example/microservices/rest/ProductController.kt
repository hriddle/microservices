package com.example.microservices.rest

import com.fasterxml.jackson.annotation.JsonFormat
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.OK
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

private const val products = "/products"

@RestController
@RequestMapping(products)
class ProductController(private val productService: ProductService) {

    private val log = LoggerFactory.getLogger(ProductController::class.java)

    @GetMapping("")
    @ResponseStatus(OK)
    fun getProducts(): List<Product> =
        productService.getAllProducts()
            .also { log.info("Returning products from controller ... ") }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    fun getProduct(@PathVariable id: Long): Product =
        productService.getProduct(id)
            .also { log.info("Returning product $id from controller ... ") }
}

@Service
class ProductService(private val productRepository: FakeRepository) {

    private val log = LoggerFactory.getLogger(ProductService::class.java)

    fun getAllProducts(): List<Product> =
        productRepository.findAllProducts()
            .also { log.info("Returning products from service ... ") }

    fun getProduct(id: Long): Product =
        productRepository.findProductById(id)
            .also { log.info("Returning product $id from service ... ") }
}

@Component
class FakeRepository {

    private val log = LoggerFactory.getLogger(FakeRepository::class.java)

    fun findAllProducts(): List<Product> =
        listOf(
            Product(id = 1, name = "Product"),
            Product(id = 2, name = "Product")
        ).also { log.info("Returning products from fake repository ... ") }

    fun findProductById(id: Long): Product =
        Product(id = id, name = "Product")
            .also { log.info("Returning product $id from fake repository ...") }
}

data class Product(
    val id: Long,
    val name: String,
    val description: String = "",
    val techStack: List<String> = emptyList(),
    @JsonFormat(pattern = "yyyy-MM-dd")
    val startDate: LocalDate = LocalDate.now()
)