package com.jhkim.bindprocessor

import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement

class ArgClass(val classElement : TypeElement, val packElement : PackageElement){
//    val items = mutableListOf<ArgField>()
//
//    fun add(item : ArgField) {
//        items.add(item)
//    }

    val fileName = "${classElement.simpleName}Extension"

    val clsName = classElement.qualifiedName.removePrefix("$packElement.")

    val funNameTemplate = if(hasCompanion()) "$clsName.Companion."
                    else "${clsName}_"

    val type = classElement.asType().asTypeName().correctStringType()
    val simpleName = classElement.simpleName!!

    fun asType() = classElement.asType()!!

    fun hasCompanion() = classElement.enclosedElements.any { it.simpleName.contentEquals("Companion") }

//    fun isInnerClass(): Boolean {
//        return classElement.enclosingElement.kind === ElementKind.CLASS
//    }
}