package com.github.momikensneg.lifelib.annotation_processor

import com.github.momikensneg.lifelib.annotation_processor.dto_annotations.CreateApiField
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion

@SupportedSourceVersion(SourceVersion.RELEASE_16)
class CreateApiFieldProcessor: AbstractFieldProcessor<CreateApiField>() {
    override fun getPostfix(): String = "CreateRequest"

    override fun getAnnotation(): Class<CreateApiField> = CreateApiField::class.java

    override fun getIsNullable(annotation: CreateApiField): Boolean = annotation.isNullable
}