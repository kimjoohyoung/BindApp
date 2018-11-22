package com.jhkim.bindlibrary.preference.ext

import com.jhkim.bindlibrary.util.get
import com.jhkim.bindlibrary.util.set
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class BindPreference<T : Any>(private val default: T) : ReadWriteProperty<SharedPreferenceExt, T> {
    override fun getValue(thisRef: SharedPreferenceExt, property: KProperty<*>): T {
        val pref = thisRef.preference
        return pref[property.name, default]
    }

    override fun setValue(thisRef: SharedPreferenceExt, property: KProperty<*>, value: T){
        val pref = thisRef.preference
        pref.edit().set(property.name, value).apply()
    }
}

