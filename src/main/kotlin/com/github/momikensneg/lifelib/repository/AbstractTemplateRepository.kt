package com.github.momikensneg.lifelib.repository

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

abstract class AbstractTemplateRepository<E,I>(protected var template: R2dbcEntityTemplate) {
    open fun findAll(): Flux<E> =
        template.select(getEntityClass()).all()

    open fun delete(entity: E): Mono<Void> =
        template.delete(entity!!).then()

    open fun deleteById(id: I): Mono<Void> =
        template.delete(Query.query(Criteria.where(getIdName()).`is`(id!!)), getEntityClass()).then()

    open fun save(entity: E): Mono<E> =
        template.insert(entity!!)

    open fun update(entity: E): Mono<E> =
        template.update(entity!!)

    open fun findById(id: I): Mono<E> =
        template.selectOne(Query.query(Criteria.where(getIdName()).`is`(id!!)), getEntityClass())

    abstract fun getEntityClass(): Class<E>
    abstract fun getIdName(): String
}