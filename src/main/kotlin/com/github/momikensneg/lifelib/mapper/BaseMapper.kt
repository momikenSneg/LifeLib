package com.github.momikensneg.lifelib.mapper

import org.mapstruct.BeanMapping
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy

interface BaseMapper<E,D,R,U> {
    abstract fun convertDtoToEntity(user: D): E

    abstract fun convertEntityToDto(user: E): D

    abstract fun convertDtoToResponse(user: D): R

    abstract fun convertUpdateToDto(user: U): D

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    abstract fun convertToEntityFromDto(userDto: D, @MappingTarget user: E): E
}