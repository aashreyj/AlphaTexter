package com.example.alphatexter

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.widget.TextView
import android.widget.Toast
import java.text.DecimalFormat

class TextScreen : AppCompatActivity()
{

    var index: Int = 0
    var numberList: ArrayList<Double> = arrayListOf()
    val df = DecimalFormat("#")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_screen)

        numberList = (this.application as NumberLister).getList() //retrieve contact list from shared preferences

        val smsManager = SmsManager.getDefault()
        while (index < numberList.size)
        {
            smsManager.sendTextMessage(df.format(numberList[index++]), null, "This is a demo.", null, null)
        }
    }

    override fun onBackPressed()
    {
        //go to main screen if back button is pressed
        val goToMain = Intent(this@TextScreen, MainActivity::class.java)
        startActivity(goToMain)
        this.finish()
    }
}