package com.example.microservices

import com.example.microservices.rest.Team
import com.example.microservices.rest.TeamController
import com.example.microservices.rest.TeamService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@WebMvcTest(TeamController::class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
class GenerateDocumentation {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var teamService: TeamService

    private val mockTeam = Team(id = 1, productName = "Product", team = emptyList())

    @Before
    fun setUp() {
        `when`(teamService.getTeamById(mockTeam.id)).thenReturn(mockTeam)
    }

    @Test
    fun `get team by id should return team object`() {
        mockMvc.perform(get("/teams/${mockTeam.id}"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(mockTeam)))
            .andDo(document("teams"))
    }
}