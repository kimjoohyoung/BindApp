package com.jhkim.bindapp

import com.jhkim.bindannotation.Arg
import com.jhkim.bindannotation.ArgBuilder
import com.jhkim.bindlibrary.arguments.BindArgument

@ArgBuilder
class MainFragment2 : MainFragment(){
    @Arg
    var intVar21  by BindArgument(0)

    @Arg
    var stringVar21  by BindArgument("Test")

}
