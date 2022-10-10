package com.example.bkservice.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class BoundAnUnboundService : Service() {

    private val mBinder : LocalBinder =LocalBinder()

    inner class LocalBinder : Binder(){
        fun getService() : BoundAnUnboundService = this@BoundAnUnboundService
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d("CCCCC", "On Bind")
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("CCCCC", "On Create")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("CCCCC", "On start command")
        return START_NOT_STICKY

    }

    override fun onDestroy() {
        Log.d("CCCCC", "On Destroy")
        super.onDestroy()
    }

    override fun onRebind(intent: Intent?) {
        Log.d("CCCCC", "On Rebind")
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("CCCCC", "On Unnbind")
        return true

    }
}