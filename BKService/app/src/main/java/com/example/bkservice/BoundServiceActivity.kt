package com.example.bkservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.bkservice.databinding.ActivityBoundServiceBinding
import com.example.bkservice.model.Song
import com.example.bkservice.service.BoundService

class BoundServiceActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityBoundServiceBinding
    private var messenger: Messenger? = null
    private var isConnection = false
    private val mMessenger = Messenger(MyHandler())

    private var serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
            messenger = Messenger(iBinder)
            isConnection = true
            val msg = Message.obtain(null, MSG_MESSAGE_START, 0, 0)
            msg.replyTo = mMessenger
            messenger?.send(msg)
            messenger?.send(Message.obtain(null, MSG_MESSAGE_STATE, this.hashCode(), 0))
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isConnection = false
        }

    }

    inner class MyHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_MESSAGE_STATE -> {
                    if (msg.arg1 == 1) viewBinding.btnPlay.setImageResource(R.drawable.ic_pauses)
                    else viewBinding.btnPlay.setImageResource(R.drawable.ic_play)
                    if (msg.obj != null && msg.obj is Song) {
                        viewBinding.apply {
                            outAuthorName.text = (msg.obj as Song).author
                            outSongname.text = (msg.obj as Song).name
                        }
                    }
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityBoundServiceBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.apply {
            btnStart.setOnClickListener(View.OnClickListener {
                startBoundService()
            })
            btnend.setOnClickListener(View.OnClickListener {
                stopBoundService()
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
        val msg = Message.obtain(null, MSG_MESSAGE_CLEAR, 0, 0)
        msg.replyTo = mMessenger
        messenger?.send(msg)
        viewBinding.containerLayout.isVisible= false
    }

    private fun playSong() {
        val msg = Message.obtain(null, MSG_MESSAGE_PLAY, 0, 0)
        msg.replyTo = mMessenger
        messenger?.send(msg)
        messenger?.send(Message.obtain(null, MSG_MESSAGE_STATE, this.hashCode(), 0))
    }

    private fun stopBoundService() {
        viewBinding.containerLayout.isVisible = false
        unbindService(serviceConnection)
        isConnection = false
    }

    private fun startBoundService() {
        if (isConnection) {
            val msg = Message.obtain(null, MSG_MESSAGE_START, 0, 0)
            msg.replyTo = mMessenger
            messenger?.send(msg)
            messenger?.send(Message.obtain(null, MSG_MESSAGE_STATE, this.hashCode(), 0))
        } else {
            val intent = Intent(this, BoundService::class.java)
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            isConnection = true
        }
        viewBinding.apply {
            btnPlay.setImageResource(R.drawable.ic_pauses)
            containerLayout.isVisible = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
        isConnection = false
    }
}