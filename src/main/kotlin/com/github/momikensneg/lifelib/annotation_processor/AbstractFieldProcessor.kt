package com.github.momikensneg.lifelib.annotation_processor

import com.squareup.kotlinpoet.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.StandardLocation

abstract class AbstractFieldProcessor<A>: AbstractProcessor() where A: Annotation {

    companion object {
        private const val OLD_PACKAGE_NAME = "dao"
        private const val NEW_PACKAGE_NAME = "dto"
    }

    private val map: MutableMap<String, MutableList<FieldInfo>> = HashMap()

    override fun init(env: ProcessingEnvironment?) {
        super.init(env)
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> = mutableSetOf(getAnnotation().canonicalName)

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (annotations.isEmpty()) {
            return false
        }

        val elements = roundEnv.getElementsAnnotatedWith(getAnnotation())
        for (element in elements) {
            val fieldsClass = element.enclosingElement as TypeElement
            if (map.containsKey(fieldsClass.qualifiedName.toString())) {
                map[fieldsClass.qualifiedName.toString()]!!.add(FieldInfo(element))
            } else {
                val list = ArrayList<FieldInfo>()
                list.add(FieldInfo(element))
                map[fieldsClass.qualifiedName.toString()] = list
            }
        }

        map.forEach { (k, v) ->
            generateDtoClass(k, v)
        }

        return true
    }

    private fun generateDtoClass(className: String, fields: MutableList<FieldInfo>) {
        val simpleName = className.substring(className.lastIndexOf('.') + 1)
        val newName = simpleName + getPostfix()
        val packageName = definePackageName(className, simpleName)

        val kotlinFile = FileSpec.builder(packageName, newName)
        val classBuilder = TypeSpec.classBuilder(newName).addModifiers(KModifier.DATA)

        addConstructor(classBuilder, fields)

        for (field in fields) {
            addProperty(classBuilder, field)
        }
        kotlinFile.addType(classBuilder.build())

        writeFile(kotlinFile.build(), packageName, newName)
    }

    abstract fun getPostfix(): String

    abstract fun getAnnotation(): Class<A>

    abstract fun getIsNullable(annotation: A): Boolean

    private fun writeFile(kotlinFile: FileSpec, packageName: String, name: String) {
        val kotlinFileObject = processingEnv.filer.createResource(
            StandardLocation.SOURCE_OUTPUT,
            packageName, "$name.kt"
        )
        kotlinFileObject.openWriter().use {
            kotlinFile.writeTo(it)
        }
    }
    private fun definePackageName(className: String, simpleName: String): String {
        val lastDot = className.lastIndexOf('.')
        return if (lastDot > 0) {
            val oldPackageName = className.substring(0, lastDot)
            "${oldPackageName.replace(OLD_PACKAGE_NAME, NEW_PACKAGE_NAME)}.${simpleName.lowercase()}"
        } else {
            ""
        }
    }

    inner class FieldInfo(
        val name: String,
        val type: TypeName,
        val isNullable: Boolean
    ) {

        @OptIn(DelicateKotlinPoetApi::class)
        constructor(element: Element) : this(
            element.simpleName.toString(),
            element.asType().asTypeName(),
            getIsNullable(element.getAnnotation(getAnnotation()))
        )
    }
}