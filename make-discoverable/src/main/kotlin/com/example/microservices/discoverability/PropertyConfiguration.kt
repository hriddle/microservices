package com.example.microservices.discoverability

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@PropertySource("classpath:discoverability.properties")
@Configuration
class PropertyConfiguration