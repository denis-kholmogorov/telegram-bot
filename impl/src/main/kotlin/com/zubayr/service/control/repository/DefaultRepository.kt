package com.zubayr.service.control.repository

import com.zubayr.service.control.domain.model.BaseEntity
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DefaultRepository : PagingAndSortingRepository<BaseEntity, UUID>