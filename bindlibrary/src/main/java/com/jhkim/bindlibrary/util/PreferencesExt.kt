package com.jhkim.bindlibrary.util

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


operator fun SharedPreferences.Editor.set(key: String, value: Boolean) = putBoolean(key, value)!!
operator fun SharedPreferences.Editor.set(key: String, value: Long) = putLong(key, value)!!
operator fun SharedPreferences.Editor.set(key: String, value: Int) = putInt(key, value)!!
operator fun SharedPreferences.Editor.set(key: String, value: String) = putString(key, value)!!
operator fun SharedPreferences.Editor.set(key: String, value: Float) = putFloat(key, value)!!
operator fun <T : Enum<T>> SharedPreferences.Editor.set(key: String, value: T ) = putInt(key, value.ordinal)!!
operator fun <T> SharedPreferences.Editor.set(key: String, value: T?) = putString(key, toJson(value))!!

operator fun SharedPreferences.get(key: String, value: Boolean) = getBoolean(key, value)
operator fun SharedPreferences.get(key: String, value: Long) = getLong(key, value)
operator fun SharedPreferences.get(key: String, value: Int) = getInt(key, value)
operator fun SharedPreferences.get(key: String, value: String) :String? = getString(key, value)
operator fun SharedPreferences.get(key: String, value: Float) = getFloat(key, value)
operator fun <T : Enum<T>> SharedPreferences.get(key: String, value: T ) :T {
    val array = value.declaringClass.enumConstants
    val index = getInt(key, -1)
    if(index in (0..(array.size-1)))
        return array[index]
    return value
}
operator fun <T> SharedPreferences.get(key: String, value: T) = fromJson(getString(key, null), value)

fun <T> SharedPreferences.Editor.toJson(value : T) :String? {
    return Gson().toJson(value)
}

fun <T> SharedPreferences.fromJson(value : String?, default : T) :T {
    return if(value !=null) Gson().fromJson<T>(value, object : TypeToken<T>() {}.type) else default
}
