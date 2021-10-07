package com.zubayr.service.control.resource

import com.zubayr.service.control.api.model.BaseDto
import com.zubayr.service.control.api.resource.DefaultResource
import com.zubayr.service.control.service.DefaultService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController
import java.util.*
@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
class DefaultResourceImpl: DefaultResource {

    override fun geById(id: UUID): ResponseEntity<BaseDto> =
        ResponseEntity.ok().body(BaseDto())

}