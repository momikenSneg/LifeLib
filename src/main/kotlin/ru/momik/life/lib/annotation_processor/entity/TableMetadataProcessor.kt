package ru.momik.life.lib.annotation_processor.entity

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import ru.momik.life.lib.annotation_processor.addStringProperty
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.StandardLocation

class TableMetadataProcessor: AbstractProcessor() {

    private val map: MutableMap<String, MutableList<String>> = HashMap()
    private val POSTFIX = "_"

    override fun init(env: ProcessingEnvironment?) {
        super.init(env)
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> = mutableSetOf(Table::class.java.canonicalName)

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (annotations.isEmpty()) {
            return false
        }

        val elements = roundEnv.getElementsAnnotatedWith(Table::class.java)

        for (element in elements) {
            val fieldsClass = element.enclosingElement as TypeElement
            if (map.containsKey(fieldsClass.qualifiedName.toString())) {
                map[fieldsClass.qualifiedName.toString()]!!.add(getColumnName(element))
            } else {
                val list = ArrayList<String>()
                list.add(getColumnName(element))
                map[fieldsClass.qualifiedName.toString()] = list
            }
        }

        map.forEach { (k, v) ->
            generateMetadataClass(k, v)
        }

        return true
    }

    private fun generateMetadataClass(className: String, fields: MutableList<String>) {
        val simpleName = className.substring(className.lastIndexOf('.') + 1)
        val newName = simpleName + POSTFIX
        var packageName: String?
        val lastDot = className.lastIndexOf('.')
        if (lastDot > 0) {
            packageName = className.substring(0, lastDot)
        } else {
            return
        }

        val kotlinFile = FileSpec.builder(packageName, newName)
        val abstractClass = TypeSpec.classBuilder(newName).addModifiers(KModifier.PUBLIC, KModifier.ABSTRACT)

        val comp = TypeSpec.companionObjectBuilder()
        for (field in fields) {
            val name = generateFieldName(field)
            addStringProperty(comp, name, field)
        }
        abstractClass.addType(comp.build())
        kotlinFile.addType(abstractClass.build())

        val kotlinFileObject = processingEnv.filer.createResource(
            StandardLocation.SOURCE_OUTPUT,
            packageName, "$newName.kt"
        )
        kotlinFileObject.openWriter().use {
            kotlinFile.build().writeTo(it)
        }
    }

    private fun generateFieldName(field: String): String {
        var name = field
        val upperCaseLetters = field.filter { it.isUpperCase() }
        for (letter in upperCaseLetters) {
            val index = field.indexOf(letter)
            name = field.substring(0, index) + "_" + field.substring(index)
        }
        return name.uppercase()
    }

    private fun getColumnName(element: Element): String {
        val annot = element.getAnnotation(Column::class.java)
        if (annot != null) {
            return annot.value
        }
        return element.simpleName.toString()
    }
}