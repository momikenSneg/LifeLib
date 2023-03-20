package io.github.momikensneg.annotation_processor

import io.github.momikensneg.annotation_processor.dto_annotations.DtoField
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion

@SupportedSourceVersion(SourceVersion.RELEASE_16)
class DtoFieldProcessor: AbstractFieldProcessor<DtoField>() {

    override fun getPostfix(): String = "Dto"

    override fun getPackageName(): String = "cy.pet.life.dto"

    override fun getAnnotation(): Class<DtoField> = DtoField::class.java

    override fun getIsNullable(annotation: DtoField): Boolean = annotation.isNullable
}