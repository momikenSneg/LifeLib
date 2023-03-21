package ru.momik.life.lib.annotation_processor.dto_annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class DtoField(val isNullable: Boolean)
