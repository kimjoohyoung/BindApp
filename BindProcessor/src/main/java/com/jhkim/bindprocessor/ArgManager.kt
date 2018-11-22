package com.jhkim.bindprocessor

import com.squareup.kotlinpoet.FileSpec
import java.io.File
import java.io.StringReader
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement

enum class ClassType {
    None,
    IsFragment,
    IsActivity
}

class ArgManager(private var processingEnv : ProcessingEnvironment) {
    private val activityType = processingEnv.elementUtils.getTypeElement("android.app.Activity").asType()
    private val typeFragments = listOf(
        processingEnv.elementUtils.getTypeElement("androidx.fragment.app.Fragment").asType(),
        processingEnv.elementUtils.getTypeElement("android.app.Fragment").asType(),
        processingEnv.elementUtils.getTypeElement("android.support.v4.app.Fragment").asType()
    )

    private val classes = mutableMapOf<ArgClass, MutableList<ArgField>>()

    fun getClass(element: TypeElement) : ArgClass {
        return classes.keys.find { it.classElement == element } ?: ArgClass(element, getPackageOf(element))
    }

    private fun getPackageOf(element: Element) : PackageElement {
        return processingEnv.elementUtils.getPackageOf(element)
    }

    fun add(field : ArgField){
        val clazz = getClass(field.getClazz())
        classes.putIfAbsent(clazz, mutableListOf())
        classes[clazz]!!.add(field)
    }
    fun getAllArgField(cls : ArgClass) : MutableList<ArgField> {
        return getAllArgField(cls.classElement)
    }

    private fun getAllArgField(item : TypeElement) : MutableList<ArgField> {
        val list = mutableListOf<ArgField>()
        for (cls in classes.keys) {
            if(processingEnv.typeUtils.isSubtype(item.asType(), cls.asType()))
                list.addAll(classes[cls]!!)
        }
        return list
    }

    fun checkBindArgument(field: ArgField) : Boolean{
        val element = field.element
        if(!element.simpleName.endsWith("\$annotations"))
            return false
        val tmp = "${element.simpleName.removeSuffix("\$annotations")}\$delegate"
        val tmp2 = element.enclosingElement.enclosedElements.find { it.simpleName.contentEquals(tmp) } ?: return false
        return tmp2.asType().toString().endsWith(".BindArgument")
    }

    fun bundleString(list : List<ArgField>):String {
        return list.joinToString(",\n", "bundleOf(\n", "\n)") { "\"${it.key}\" bundleTo ${it.key}" }
    }

    fun write(builder: FileSpec.Builder, cls : ArgClass) {
        val file = builder
            .addImport("com.jhkim.bindlibrary.util", "bundleTo")
            .addImport("com.jhkim.bindlibrary.util", "bundleOf")
            .build()

        val buffer = StringBuilder()
        file.writeTo(buffer)
        var str = buffer.toString().replace("`", "")
        str = removeKotlinImport(str)

        val kaptKotlinGeneratedDir = processingEnv.options[ArgProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME]
        File(kaptKotlinGeneratedDir, "${cls.fileName}.kt").writeText(str)
    }

    private fun removeKotlinImport(str:String) : String {
        val builder = StringBuilder()
        StringReader(str).forEachLine {
            if(it.startsWith("import kotlin"))
                return@forEachLine
            builder.appendln(it)
        }
        return builder.toString()
    }

    fun getType(cls : ArgClass) : ClassType {
        if(isFragment(cls))
            return ClassType.IsFragment
        else if(isActivity(cls))
            return ClassType.IsActivity
        return ClassType.None
    }

    private fun isActivity(cls : ArgClass) : Boolean{
        return processingEnv.typeUtils.isSubtype(cls.asType(), activityType)
    }

    private fun isFragment(cls : ArgClass) : Boolean{
        return typeFragments.any{processingEnv.typeUtils.isSubtype(cls.asType(), it)}
    }
}
