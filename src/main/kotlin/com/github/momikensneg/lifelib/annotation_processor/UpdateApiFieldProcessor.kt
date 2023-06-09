package com.github.momikensneg.lifelib.annotation_processor

import com.github.momikensneg.lifelib.annotation_processor.dto_annotations.UpdateApiField
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion

@SupportedSourceVersion(SourceVersion.RELEASE_16)
class UpdateApiFieldProcessor: AbstractFieldProcessor<UpdateApiField>() {

    override fun getPostfix(): String = "UpdateRequest"

    override fun getAnnotation(): Class<UpdateApiField> = UpdateApiField::class.java

    override fun getIsNullable(annotation: UpdateApiField): Boolean = annotation.isNullable
}