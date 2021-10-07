package com.zubayr.service.control.resource

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner::class)
internal class DetailsResourceImplTest {

    @Autowired
    private val mvc: MockMvc? = null

    @Autowired
    private val objectMapper: ObjectMapper? = null

    @Test
    fun addAll() {

        mvc?.perform(
            MockMvcRequestBuilders.post("/news")
                .contentType(MediaType.TEXT_PLAIN))
            ?.andDo(MockMvcResultHandlers.print())
            ?.andExpect(MockMvcResultMatchers.status().isOk)

    }
}