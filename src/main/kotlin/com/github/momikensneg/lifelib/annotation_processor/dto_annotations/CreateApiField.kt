package com.github.momikensneg.lifelib.annotation_processor.dto_annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class CreateApiField(val isNullable: Boolean)
