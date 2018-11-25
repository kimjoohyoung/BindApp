package com.jhkim.bindapp

import androidx.fragment.app.Fragment
import com.jhkim.bindannotation.Arg

open class MainFragment : Fragment(){
    @Arg
    lateinit var stringVar11 : String

    @Arg
    var intVar11 : Int = 0

    @Arg
    var intVar12 : Int = 0

//    companion object {
//
//    }
}