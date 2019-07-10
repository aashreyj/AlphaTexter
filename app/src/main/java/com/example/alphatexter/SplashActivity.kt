package com.example.alphatexter

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.app.ActivityCompat


class SplashActivity : AppCompatActivity() {

    private val permissionArray = arrayOf(
        Manifest.permission.SEND_SMS,
        Manifest.permission.READ_EXTERNAL_STORAGE)

    val requestPermission = 131 //test code to request all permissions from the system

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if(!hasPermissions(this@SplashActivity, *permissionArray))
        {
            //request for permissions
            ActivityCompat.requestPermissions(this@SplashActivity, permissionArray, requestPermission)
        }
        else
        {
            //show Splash Screen for 2.5 sec
            Handler().postDelayed({
                val startMain = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(startMain)
                this.finish()
            }, 2500)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            //check if all permissions have been granted or not
            requestPermission -> {
                if(grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                {
                    //show Splash Screen for 2.5 sec
                    Handler().postDelayed({
                        val startMain = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(startMain)
                        this.finish()
                    }, 2500)
                }
                else
                {
                    //kill activity if permissions are not granted while showing a prompt
                    Toast.makeText(this@SplashActivity, "Please grant Necessary Permissions to Continue...", Toast.LENGTH_SHORT).show()
                    this.finish()
                }
                return
            }
            else -> {
                //error case
                Toast.makeText(this@SplashActivity, "Something went wrong :/", Toast.LENGTH_SHORT).show()
                this.finish()
            }
        }
    }

    //checks if permissions have been granted
    fun hasPermissions(context: Context, vararg permissions: String): Boolean
    {
        var hasAllPermissions = true
        for(permission in permissions)
        {
            val res = context.checkCallingOrSelfPermission(permission)
            if(res != PackageManager.PERMISSION_GRANTED)
                hasAllPermissions = false
        }
        return hasAllPermissions
    }
}
