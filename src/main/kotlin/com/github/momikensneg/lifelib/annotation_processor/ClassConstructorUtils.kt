package com.github.momikensneg.lifelib.annotation_processor

import com.squareup.kotlinpoet.*

const val JAVA_STRING = "java.lang.String"

fun <A> addConstructor(
    builder: TypeSpec.Builder, fields: MutableList<AbstractFieldProcessor<A>.FieldInfo>
) where A : Annotation {
    val ctor = FunSpec.constructorBuilder()
    for (field in fields) {
        addParameter(ctor, field)
    }
    builder.primaryConstructor(ctor.build())
}

fun <A> addParameter(ctor: FunSpec.Builder, field: AbstractFieldProcessor<A>.FieldInfo) where A : Annotation {
    val type = defineType(field)
    ctor.addParameter(field.name, type)
}

fun <A> addProperty(builder: TypeSpec.Builder, field: AbstractFieldProcessor<A>.FieldInfo) where A : Annotation {
    val type = defineType(field)
    val property = PropertySpec.builder(field.name, type).mutable(true).initializer(field.name).build()
    builder.addProperty(property)
}

fun addStringProperty(builder: TypeSpec.Builder, fieldName: String, value: String) {
    builder.addProperty(
        PropertySpec.builder(fieldName, String::class)
            .addModifiers(KModifier.CONST, KModifier.PUBLIC)
            .initializer("%S", value)
            .build()
    )
}

private fun <A> defineType(field: AbstractFieldProcessor<A>.FieldInfo): TypeName where A : Annotation {
    val type = if (field.type.toString() == JAVA_STRING) String::class.asTypeName() else field.type
    return if (field.isNullable) type.copy(true) else type
}
