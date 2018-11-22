package com.jhkim.bindapp

import android.app.Application

class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Setting.init(this, "Setting")
    }
}