package com.github.momikensneg.lifelib.annotation_processor

import com.github.momikensneg.lifelib.annotation_processor.dto_annotations.ResponseApiField
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion

@SupportedSourceVersion(SourceVersion.RELEASE_16)
class ResponseApiFieldProcessor: AbstractFieldProcessor<ResponseApiField>() {

    override fun getPostfix(): String = "ResponseDto"

    override fun getPackageName(): String = "cy.pet.life.dto"

    override fun getAnnotation(): Class<ResponseApiField> = ResponseApiField::class.java

    override fun getIsNullable(annotation: ResponseApiField): Boolean = annotation.isNullable
}