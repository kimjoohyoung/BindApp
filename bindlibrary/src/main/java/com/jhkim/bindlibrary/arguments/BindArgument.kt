package com.jhkim.bindlibrary.arguments

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.jhkim.bindlibrary.util.get
import com.jhkim.bindlibrary.util.set
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

//inline fun <reified T : Any> BindArgument(default: T?=null): ReadOnlyProperty<Lifecycle, T> = BindArgument(T::class, default)

@Suppress("UNCHECKED_CAST")
class BindArgument<T : Any>(private val default: T) : ReadWriteProperty<LifecycleOwner, T> {
    override fun setValue(thisRef: LifecycleOwner, property: KProperty<*>, value: T) {
        when(thisRef){
            is Fragment -> thisRef.arguments?.set(property.name, default)
            is AppCompatActivity -> thisRef.intent.extras?.set(property.name, default)
            else -> {}
        }
    }

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): T {
        return when(thisRef){
            is Fragment -> thisRef.arguments?.get(property.name, default) ?: default
            is AppCompatActivity -> thisRef.intent.extras?.get(property.name, default) ?: default
            else -> default
        }
    }
}