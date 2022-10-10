package com.example.bkservice.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import com.example.bkservice.R

class BackgroundService : Service() {

    private var mediaPlayer : MediaPlayer? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("AAAAA", "BK Service on create")
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("AAAAA", "BK Service on start")
        startMusic()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startMusic() {
        if(mediaPlayer==null){
            mediaPlayer = MediaPlayer.create(applicationContext, R.raw.kclt)
        }
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        Log.d("AAAAA", "BK Service on destroy")
        super.onDestroy()
        if(mediaPlayer!= null){
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
}