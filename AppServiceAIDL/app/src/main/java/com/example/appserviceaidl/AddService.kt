package com.example.appserviceaidl

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class AddService : Service() {
    // The generated interface includes a subclass named Stub
// that is an abstract implementation of its parent interface
    private val mBinder: IAidlAddNumber.Stub = object : IAidlAddNumber.Stub() {
        override fun addNumbers(num1: Int, num2: Int): Int {
            return num1 + num2
        }
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d("AAAAA", "service running")
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("AAAAA", "Service is unbind")
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("AAAAA", "Service is destroy")
    }
}