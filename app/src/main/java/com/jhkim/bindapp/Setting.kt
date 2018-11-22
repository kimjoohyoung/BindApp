package com.jhkim.bindapp

import com.jhkim.bindlibrary.preference.ext.BindPreference
import com.jhkim.bindlibrary.preference.ext.SharedPreferenceExt

object Setting : SharedPreferenceExt() {
    var prefInt by BindPreference(10)
    var prefString by BindPreference("Sample")
}
