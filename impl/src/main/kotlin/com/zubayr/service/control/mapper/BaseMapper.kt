package com.zubayr.service.control.mapper

interface BaseMapper<Entity, Dto> {
    fun convertToEntity(dto: Dto): Entity
    fun convertToDto(entity: Entity): Dto
}