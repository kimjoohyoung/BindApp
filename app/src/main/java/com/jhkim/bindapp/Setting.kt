package com.jhkim.bindapp

import com.jhkim.bindlibrary.preference.BindPreference
import com.jhkim.bindlibrary.preference.SharedPreferenceExt

object Setting : SharedPreferenceExt() {
    var prefInt by BindPreference(10)
    var prefString by BindPreference("Sample")
}
