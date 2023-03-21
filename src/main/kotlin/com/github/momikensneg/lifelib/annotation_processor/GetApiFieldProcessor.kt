package com.github.momikensneg.lifelib.annotation_processor

import com.github.momikensneg.lifelib.annotation_processor.dto_annotations.GetApiField
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion

@SupportedSourceVersion(SourceVersion.RELEASE_16)
class GetApiFieldProcessor: AbstractFieldProcessor<GetApiField>() {
    override fun getPostfix(): String = "GetRequest"

    override fun getAnnotation(): Class<GetApiField> = GetApiField::class.java

    override fun getIsNullable(annotation: GetApiField): Boolean = annotation.isNullable
}