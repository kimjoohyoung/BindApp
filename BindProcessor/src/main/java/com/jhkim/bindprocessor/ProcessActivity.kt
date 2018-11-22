package com.jhkim.bindprocessor

import com.squareup.kotlinpoet.*

object ProcessActivity {

    private fun buildFunc(manager: ArgManager, cls: ArgClass, list: List<ArgField>, receiverName: ClassName, isCode: Boolean = true): FunSpec.Builder {
        val funcSpec = FunSpec.builder("launch${cls.simpleName}")
            .receiver(receiverName)

        list.forEach {
            funcSpec.addParameter(it.key, it.getType())
        }
        if (isCode)
            funcSpec.addParameter(ParameterSpec.builder("requestCode", Int::class.java).defaultValue("-1").build())

        val block = CodeBlock.builder()
        if (isCode)
            block.add("launchActivity<${cls.simpleName}>(requestCode)")
        else
            block.add("launchActivity<${cls.simpleName}>()")

        if (list.isEmpty()) {
            funcSpec.addCode(block.build())
        }else{
            funcSpec.beginControlFlow(block.build().toString())
                .addStatement("putExtras(${manager.bundleString(list)})")
            funcSpec.endControlFlow()
        }
        return funcSpec
    }

    fun generate(manager: ArgManager, cls: ArgClass, list: List<ArgField>) {
        val file = FileSpec.builder(cls.packElement.toString(), cls.fileName)
            .addImport("android.content", "Intent")
            .addImport("com.jhkim.bindlibrary.util", "launchActivity")
            .addImport("com.jhkim.bindlibrary.util", "set")

        file.addFunction(buildFunc(manager, cls, list, ClassName("android.app", "Activity")).build())
        file.addFunction(buildFunc(manager, cls, list, ClassName("androidx.fragment.app", "Fragment")).build())
        file.addFunction(buildFunc(manager, cls, list, ClassName("android.content", "Context"), false).build())

        //generate bindArgument method
        val listFilter = list.filter { !manager.checkBindArgument(it) }
        if (!listFilter.isNullOrEmpty()) {
            val funcSpec2 = FunSpec.builder("bindExtra")
                .receiver(cls.type)

            funcSpec2.beginControlFlow("intent.extras?.let")
            list.forEach {
                if (!manager.checkBindArgument(it))
                    funcSpec2.addStatement(
                        "${it.key} = it[%S] as %T",
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