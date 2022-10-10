package com.example.bkservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.bkservice.databinding.ActivityBoundAndUnboundServiceBinding
import com.example.bkservice.service.BoundAnUnboundService

class BoundAndUnboundActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityBoundAndUnboundServiceBinding

    private lateinit var mService : BoundAnUnboundService
    private var isConnection = false
    private val serviceConnection = object : ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            val binder = service as BoundAnUnboundService.LocalBinder
            mService = binder.getService()
            isConnection = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isConnection=false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityBoundAndUnboundServiceBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.apply {
            btnStart.setOnClickListener(View.OnClickListener {
                startServices()
            })
            btnStopBound.setOnClickListener(View.OnClickListener {
                stopBundService()
            })
            btnStopUnbound.setOnClickListener(View.OnClickListener {
                stopUnboundService()
            })
            btnRebind.setOnClickListener(View.OnClickListener {
                reBindService()
            })
        }
    }

    private fun reBindService() {
        val intent = Intent(this, BoundAnUnboundService::class.java)
        bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE)
    }

    private fun stopUnboundService() {
        val intent = Intent(this, BoundAnUnboundService::class.java)
        stopService(intent)
    }

    private fun stopBundService() {
        if(isConnection){
            unbindService(serviceConnection)
            isConnection=false
        }
    }

    private fun startServices() {
        val intent = Intent(this, BoundAnUnboundService::class.java)
        startService(intent)
        bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}