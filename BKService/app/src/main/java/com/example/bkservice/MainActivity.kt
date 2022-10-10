package com.example.bkservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.bkservice.databinding.ActivityMainBinding
import com.example.bkservice.service.BackgroundService

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btnStart.setOnClickListener(View.OnClickListener {
                startBKService()
            })
            btnend.setOnClickListener(View.OnClickListener {
                endBKService()
            })
        }
    }
    private fun startBKService(){
        val intent = Intent(this, BackgroundService::class.java)
        startService(intent)
    }
    private fun endBKService(){
        val intent = Intent(this, BackgroundService::class.java)
        stopService(intent)
    }
}