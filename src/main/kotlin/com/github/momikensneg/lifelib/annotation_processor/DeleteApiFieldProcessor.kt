package com.github.momikensneg.lifelib.annotation_processor

import com.github.momikensneg.lifelib.annotation_processor.dto_annotations.DeleteApiField
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion

@SupportedSourceVersion(SourceVersion.RELEASE_16)
class DeleteApiFieldProcessor: AbstractFieldProcessor<DeleteApiField>() {
    override fun getPostfix(): String = "DeleteRequest"

    override fun getAnnotation(): Class<DeleteApiField> = DeleteApiField::class.java

    override fun getIsNullable(annotation: DeleteApiField): Boolean = annotation.isNullable
}