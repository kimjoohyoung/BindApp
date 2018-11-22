package com.jhkim.bindprocessor

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec

object ProcessFragment {
    fun generate(manager: ArgManager, cls: ArgClass, list: List<ArgField>) {

        //generate newInstance method
        val funcSpec = if (cls.hasCompanion()) {
            FunSpec.builder("Companion.newInstance").receiver(cls.type)
        } else {
            FunSpec.builder("${cls.simpleName}_newInstance")
        }
            .returns(cls.type)

        list.forEach {
            funcSpec.addParameter(it.key, it.getType())
        }

        funcSpec.addStatement("val f = ${cls.clsName}()")
            .addStatement("f.arguments = ${manager.bundleString(list)}")
            .addStatement("return f")

        val file = FileSpec.builder(cls.packElement.toString(), cls.fileName)
            .addFunction(funcSpec.build())

        //generate bindArgument method
        val listFilter = list.filter { !manager.checkBindArgument(it) }
        if (!listFilter.isNullOrEmpty()) {
            val funcSpec2 = FunSpec.builder("bindArgument")
                .receiver(cls.type)
            //.addParameter("fragment", cls.asType().asTypeName().correctStringType())


            funcSpec2.beginControlFlow("arguments?.let")
            list.forEach {
                if (!manager.checkBindArgument(it))
                    funcSpec2.addStatement(
                        "${it.key} = it.get(%S) as %T",
                        it.key,
                        it.getType()
                    )
            }
            funcSpec2.endControlFlow()

            file.addFunction(funcSpec2.build())
        }

        manager.write(file, cls)
    }

}