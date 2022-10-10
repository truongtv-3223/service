package com.example.appclientaidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.appclientaidl.databinding.ActivityMainBinding
import com.example.appserviceaidl.IAidlAddNumber

const val SERVICE_ACTION = "service.add.number"
const val serverAppUri = "com.example.appserviceaidl";
class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding : ActivityMainBinding

    private  var mService : IAidlAddNumber? = null
    private var isConnection = false
    private var mConnection = object : ServiceConnection {
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
        startService()
    }
    private fun sumNumber(fistNumber: Int, secondNumber: Int) {
        if(isAppInstall()){
            var result =mService?.addNumbers(fistNumber,secondNumber)
            viewBinding.outResult.text ="Here is result : $result"
        }else {
            Toast.makeText(applicationContext,"Service App Not Installed", Toast.LENGTH_SHORT).show()

        }
    }
    private fun startService(){
        //intent implicit
        var intent = Intent()
        intent.action = SERVICE_ACTION
        intent.`package` = serverAppUri
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isConnection){
            unbindService(mConnection)
            isConnection= false
        }
    }
    private fun isAppInstall() : Boolean{
        val pm = packageManager
        var isInstall =false
        try {
            pm.getPackageInfo(serverAppUri, PackageManager.GET_ACTIVITIES)
            isInstall=true
            Log.d("AAAAA", "app is install")
        }catch (e : Exception){
            Log.d("AAAAA", "app is NOT install")
            isInstall =false
        }
        return isInstall
    }
}