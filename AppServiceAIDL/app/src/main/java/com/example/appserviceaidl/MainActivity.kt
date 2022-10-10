package com.example.appserviceaidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Toast
import com.example.appserviceaidl.databinding.ActivityMainBinding

const val SERVICE_ACTION = "service.add.number"

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding : ActivityMainBinding
    private  var mService : IAidlAddNumber? = null
    private var isConnection = false
    private var mConnection = object : ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
           mService = IAidlAddNumber.Stub.asInterface(iBinder)
            isConnection = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isConnection = false
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        startService()
        viewBinding.apply {
            btnSum.setOnClickListener(View.OnClickListener {
                var fistNumber = inFistNumber.text.toString()
                var secondNumber = inSecondNumber.text.toString()
                if(fistNumber=="" || secondNumber ==""){
                    Toast.makeText(applicationContext,"Vui lòng nhập đủ 2 số", Toast.LENGTH_SHORT).show()
                }else{
                    sumNumber(fistNumber.toInt(), secondNumber.toInt())
                }

            })
        }
    }

    private fun sumNumber(fistNumber: Int, secondNumber: Int) {
        var result =mService?.addNumbers(fistNumber,secondNumber)
        viewBinding.outResult.text ="Here is result : $result"
    }
    private fun startService(){
        if(isConnection==false){
            var intent = Intent(this, AddService::class.java)
            intent.action = SERVICE_ACTION
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isConnection){
            unbindService(mConnection)
            isConnection= false
        }
    }

}