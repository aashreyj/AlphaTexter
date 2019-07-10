package com.example.alphatexter

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.widget.TextView
import android.widget.Toast
import java.text.DecimalFormat

class TextScreen : AppCompatActivity()
{

    var index: Int = 0
    var numberList: ArrayList<Double> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_screen)

        numberList = (this.application as NumberLister).getList() //retrieve contact list from shared preferences
    }

    override fun onBackPressed()
    {
        //go to main screen if back button is pressed
        val goToMain = Intent(this@TextScreen, MainActivity::class.java)
        startActivity(goToMain)
        this.finish()
    }

    //when activity resumes from Android system calling UI
    override fun onResume()
    {
        val phoneNumber: TextView = findViewById(R.id.phoneNumber)
        val df = DecimalFormat("#")
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val callStateListener = object : PhoneStateListener()
        {
            override fun onCallStateChanged(state: Int, incomingNumber: String)
            {
                if (state == TelephonyManager.CALL_STATE_OFFHOOK)
                {
                    //increment the index to load next contact from the list
                    //when the phone state is off-hook i.e a call is active or is dialling
                    index++
                }

                if (state == TelephonyManager.CALL_STATE_IDLE)
                {
                    //if call state is idle, current contact from the list should be read and called
                    if (index < numberList.size)
                    {
                        phoneNumber.text = df.format(numberList[index])

                        val makeCall = Intent(Intent.ACTION_CALL)
                        makeCall.data = Uri.parse("tel:" + phoneNumber.text) //intent to make the call
                        startActivity(makeCall)
                    }
                    else
                        Toast.makeText(applicationContext, "Finished calls...", Toast.LENGTH_SHORT).show() //when the last call is dialled
                }
            }
        }
        //listen when the calling state of the phone is changed
        //to control the loading and updating of the phone numbers in the list
        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE)
        super.onResume()
    }
}