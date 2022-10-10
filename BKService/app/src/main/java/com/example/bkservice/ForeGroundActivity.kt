package com.example.bkservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import com.example.bkservice.databinding.ActivityForeGroundBinding
import com.example.bkservice.service.ForegoundService

class ForeGroundActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityForeGroundBinding
    private var localReciver = object : BroadcastReceiver() {

        override fun onReceive(p0: Context?, intent: Intent?) {
            var songName = intent?.getStringExtra(STRING_NAME)
            var author = intent?.getStringExtra(STRING_AUTHOR)
            var isPlaying = intent?.getBooleanExtra(IS_PLAYING, true)
            setView(songName, author, isPlaying)
        }

    }

    private fun setView(songName: String?, author: String?, playing: Boolean?) {
        viewBinding.apply {
            outAuthorName.text = author
            outSongname.text = songName
            if (playing == true) btnPlay.setImageResource(R.drawable.ic_pauses)
            else btnPlay.setImageResource(R.drawable.ic_play)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityForeGroundBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        registerBroadcast()
        viewBinding.apply {
            btnStart.setOnClickListener(View.OnClickListener {
                startForeGroundService()
            })
            btnend.setOnClickListener(View.OnClickListener {
                endForegroundService()
            })
            btnPlay.setOnClickListener(View.OnClickListener {
                playSong()
            })
            btnClear.setOnClickListener(View.OnClickListener {
                clearSong()
            })
        }
    }

    private fun clearSong() {
        var intent = Intent(this, ForegoundService::class.java)
        intent.putExtra(ACTION_INTENT, ACTION_CLEAR)
        startService(intent)
        viewBinding.containerLayout.isVisible = false
    }

    private fun playSong() {
        var intent = Intent(this, ForegoundService::class.java)
        intent.putExtra(ACTION_INTENT, ACTION_PLAY)
    //    Log.d("BBBBB", "on play or pause")
        startService(intent)

    }


    private fun endForegroundService() {
        var intent = Intent(this, ForegoundService::class.java)
        stopService(intent)
        viewBinding.containerLayout.isVisible = false
    }

    private fun startForeGroundService() {
        var intent = Intent(this, ForegoundService::class.java)
        intent.putExtra(ACTION_INTENT, ACTION_START)
        startService(intent)
        viewBinding.containerLayout.isVisible = true
    }

    private fun registerBroadcast() {
        var filter = IntentFilter(ACTION_MUSIC_BROADCAST)
        registerReceiver(localReciver, filter)
    }
}