package com.zubayr.service.control.api.resource

import com.zubayr.service.control.api.model.BaseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping("/product")
interface DefaultResource {

    @GetMapping("/{id}")
    fun geById(@PathVariable id: UUID): ResponseEntity<BaseDto>
}