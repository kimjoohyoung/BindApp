package com.jhkim.bindprocessor

import com.google.auto.service.AutoService
import com.jhkim.bindannotation.Arg
import com.jhkim.bindannotation.ArgBuilder
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement


fun TypeName.correctStringType() =
    if (this.toString() == "java.lang.String") ClassName("kotlin", "String") else this

@AutoService(Processor::class) // For registering the service
@SupportedSourceVersion(SourceVersion.RELEASE_8) // to support Java 8
@SupportedOptions(ArgProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class ArgProcessor: AbstractProcessor(){

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        println("getSupportedAnnotationTypes")
        return mutableSetOf(Arg::class.java.canonicalName, ArgBuilder::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun process(p0: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        val manager = ArgManager(processingEnv)
        for(item in roundEnv.getElementsAnnotatedWith(Arg::class.java)){
            manager.add(ArgField(item))
        }

        for(cls in roundEnv.getElementsAnnotatedWith(ArgBuilder::class.java).map { manager.getClass(it as TypeElement) }){
            val list = manager.getAllArgField(cls)
            when(manager.getType(cls)){
                ClassType.IsFragment -> ProcessFragment.generate(manager, cls, list)
                ClassType.IsActivity -> ProcessActivity.generate(manager, cls, list)
                else -> {}
            }
        }
        return true
    }
}