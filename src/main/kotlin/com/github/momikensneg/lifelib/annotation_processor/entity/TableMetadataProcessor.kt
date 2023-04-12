package com.github.momikensneg.lifelib.annotation_processor.entity

import com.github.momikensneg.lifelib.annotation_processor.addStringProperty
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.tools.StandardLocation

@SupportedSourceVersion(SourceVersion.RELEASE_16)
class TableMetadataProcessor: AbstractProcessor() {

    private val map: MutableMap<String, MutableList<String>> = HashMap()
    companion object {
        private const val POSTFIX = "_"
    }

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
            val table = element as TypeElement
            if (!map.containsKey(table.qualifiedName.toString())){
                map[table.qualifiedName.toString()] = ArrayList()
            }
            val tableElements = table.enclosedElements
            for (tableElement in tableElements){
                if (tableElement is VariableElement){
                    map[table.qualifiedName.toString()]!!.add(getColumnName(tableElement))
                }
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
        val packageName = definePackageName(className)

        val kotlinFile = FileSpec.builder(packageName, newName)
        val abstractClass = TypeSpec.classBuilder(newName).addModifiers(KModifier.PUBLIC, KModifier.ABSTRACT)

        val comp = TypeSpec.companionObjectBuilder()
        for (field in fields) {
            val name = generateFieldName(field)
            addStringProperty(comp, name, field)
        }
        abstractClass.addType(comp.build())
        kotlinFile.addType(abstractClass.build())

        writeFile(kotlinFile.build(), packageName, newName)
    }

    private fun writeFile(kotlinFile: FileSpec, packageName: String, name: String) {
        val kotlinFileObject = processingEnv.filer.createResource(
            StandardLocation.SOURCE_OUTPUT,
            packageName, "$name.kt"
        )
        kotlinFileObject.openWriter().use {
            kotlinFile.writeTo(it)
        }
    }

    private fun definePackageName(className: String): String {
        val lastDot = className.lastIndexOf('.')
        return if (lastDot > 0) {
            className.substring(0, lastDot)
        } else {
            ""
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