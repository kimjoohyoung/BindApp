package com.jhkim.bindlibrary.preference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.preference.PreferenceManager
import kotlin.reflect.KClass

open class SharedPreferenceExt {

    fun init(context: Context) {
        map[this::class] = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun init(context: Context, name : String, mode : Int= MODE_PRIVATE) {
        map[this::class] = context.getSharedPreferences(name, mode)
    }

    val preference : SharedPreferences
        get() = map[this::class]!!
//
//    fun find(instance : SharedPreferenceExt) : SharedPreferences {
//        return map[instance::class]!!
//    }

    companion object {
        /*
        fun add(instance : SharedPreferenceExt, context: Context){
            map[instance::class] = PreferenceManager.getDefaultSharedPreferences(context)
        }
        fun add(instance : SharedPreferenceExt, context: Context, name : String, mode : Int= MODE_PRIVATE){
            map[instance::class] = context.getSharedPreferences(name, mode)
        }

*/
        private  val map = mutableMapOf<KClass<*>, SharedPreferences?>()
    }
}
