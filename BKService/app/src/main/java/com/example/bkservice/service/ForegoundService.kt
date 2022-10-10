package com.example.bkservice.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.bkservice.*
import kotlin.random.Random

class ForegoundService : Service() {

    private var mediaPlayer : MediaPlayer? = null
    private var isPlaying = false

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("BBBBB", "on create Foreground Service")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("BBBBB", "on start Foreground Service")
        handlAction(intent?.getStringExtra(ACTION_INTENT))
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mediaPlayer!=null){
            mediaPlayer?.release()
            mediaPlayer=null
        }
        Log.d("BBBBB", "on destroy Foreground Service")
    }

    private fun handlAction(action: String?) {
        when(action){
            ACTION_PLAY -> handPlay()
            ACTION_CLEAR ->clearSong()
            ACTION_START -> startSong()
        }
    }

    private fun startSong() {
        if(mediaPlayer==null){
            mediaPlayer = MediaPlayer.create(applicationContext, R.raw.kclt)
        }
        mediaPlayer?.start()
        isPlaying = true
        sendNotifications()
        getPendingIntent("Không chỉ là thích","Tôn Ngữ Trại")?.send()
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
        sendNotifications()
        getPendingIntent("Không chỉ là thích","Tôn Ngữ Trại")?.send()
    }

    private fun playSong() {
        if(mediaPlayer==null){
            mediaPlayer = MediaPlayer.create(applicationContext, R.raw.kclt)
        }
        if(isPlaying==false){
            mediaPlayer?.start()
            isPlaying = true
            sendNotifications()
            getPendingIntent("Không chỉ là thích","Tôn Ngữ Trại")?.send()
        }
    }
    private fun sendNotifications() {

        createNotificationChannel()


        var mediaSession = MediaSessionCompat(this, "tag")
        var notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .apply {
                setSmallIcon(R.drawable.ic_play)
                setContentTitle(" Không chỉ là thích")
                setContentText("Tôn Ngữ Trại")
                setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1)
                        .setMediaSession(mediaSession.sessionToken)
                )
                if (isPlaying) addAction(
                    R.drawable.ic_pauses,
                    "play",
                    null
                )
                else addAction(R.drawable.ic_play, "pause", null)
                addAction(R.drawable.ic_clear, "next", null)
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1, notification.build())
        } else {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, notification.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, "Music", importance)
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun getPendingIntent(song: String, author : String): PendingIntent? {
        Log.d("BBBBB", "da gui broascast")
        var intent = Intent(ACTION_MUSIC_BROADCAST)
        intent.putExtra(STRING_NAME, song)
        intent.putExtra(STRING_AUTHOR, author)
        intent.putExtra(IS_PLAYING, isPlaying)
        return PendingIntent.getBroadcast(
            applicationContext,
            Random.nextInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}