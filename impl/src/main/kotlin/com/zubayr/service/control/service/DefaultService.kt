package com.zubayr.service.control.service

import com.zubayr.service.control.repository.DefaultRepository
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service
import java.util.*

/**
 * ProductService
 *
 * @author Denis_Kholmogorov
 */
@Service
class DefaultService(
        private val defaultRepository: DefaultRepository
) {

    fun getById(id: UUID) = defaultRepository.findAll()

}