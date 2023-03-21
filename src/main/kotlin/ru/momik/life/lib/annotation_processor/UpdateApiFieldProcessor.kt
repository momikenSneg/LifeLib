package ru.momik.life.lib.annotation_processor

import ru.momik.life.lib.annotation_processor.dto_annotations.UpdateApiField
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion

@SupportedSourceVersion(SourceVersion.RELEASE_16)
class UpdateApiFieldProcessor: AbstractFieldProcessor<UpdateApiField>() {

    override fun getPostfix(): String = "UpdateDto"

    override fun getPackageName(): String = "cy.pet.life.dto"

    override fun getAnnotation(): Class<UpdateApiField> = UpdateApiField::class.java

    override fun getIsNullable(annotation: UpdateApiField): Boolean = annotation.isNullable
}