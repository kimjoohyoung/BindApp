package com.jhkim.bindprocessor

import com.jhkim.bindannotation.Arg
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

class ArgField(val element: Element) {
    private val clazz = element.enclosingElement as TypeElement
    val key : String
        get() {
        val annotationKey = element.getAnnotation(Arg::class.java).key
            return if(!annotationKey.isEmpty()){
                annotationKey
            }else{
                when (element) {
                    is ExecutableElement -> regex.replace(element.simpleName.toString(), "$1")
                    else -> element.simpleName.toString()
                }
            }
    }

    fun getType() = asType().asTypeName().correctStringType()

    private fun asType() = when (element) {
        is ExecutableElement -> {
            val tmp = regex.replace(element.simpleName.toString().capitalize(), "get$1")
            val item2 =
                element.enclosingElement.enclosedElements.find { it.simpleName.contentEquals(tmp) } as ExecutableElement
            item2.returnType
        }
        else -> element.asType()
    }!!

    fun getClazz() = clazz

    companion object {
        private val regex = "([^$]+).+".toRegex()
    }
}
