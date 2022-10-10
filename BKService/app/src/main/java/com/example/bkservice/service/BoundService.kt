package com.example.bkservice.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import com.example.bkservice.*
import com.example.bkservice.model.Song

class BoundService : Service() {

    private var mediaPlayer : MediaPlayer? =null
    private var isPlaying = false
    private lateinit var messenger : Messenger
    private val listMessage = mutableListOf<Messenger>()
    inner class MyHandler() : Handler(){
        override fun handleMessage(msg: Message) {
            when(msg.what){
                MSG_MESSAGE_START ->{
                    startSong()
                    listMessage.add(msg.replyTo)
                }
                MSG_MESSAGE_PLAY -> {
                    handPlay()
                    listMessage.add(msg.replyTo)
                }
                MSG_MESSAGE_CLEAR -> {
                    clearSong()
                }
                MSG_MESSAGE_STATE ->{
                    for(x in listMessage){
                        var state=if(isPlaying) 1 else 0
                        val msgs =Message.obtain(null, MSG_MESSAGE_STATE,state,0)
                        msgs.obj = Song("Không chỉ là thích", "Tôn  Ngữ Trại")
                        x.send(msgs)
                    }
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        messenger = Messenger(MyHandler())
        return messenger.binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        if(mediaPlayer!=null){
            mediaPlayer?.release()
            mediaPlayer=null
        }
        return true
    }

    private fun startSong() {
        if(mediaPlayer==null){
            mediaPlayer = MediaPlayer.create(applicationContext, R.raw.kclt)
        }
        mediaPlayer?.start()
        isPlaying = true
    }

    private fun handPlay() {
        if(isPlaying) pauseSong()
        else playSong()
    }

    private fun clearSong() {
        if(mediaPlayer!=null){
            mediaPlayer?.release()
            mediaPlayer=null
            isPlaying = false
        }
        stopSelf()
    }

    private fun pauseSong() {
        if(mediaPlayer!=null){
            mediaPlayer?.pause()
        }
        isPlaying = false
    }

    private fun playSong() {
        if(mediaPlayer==null){
            mediaPlayer = MediaPlayer.create(applicationContext, R.raw.kclt)
        }
        if(isPlaying==false){
            mediaPlayer?.start()
            isPlaying = true
        }
    }
}