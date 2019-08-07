package com.example.alphatexter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.text.DecimalFormat

class Texting : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_texting)

        val sendText: Button = findViewById(R.id.sendText)

        sendText.setOnClickListener {
            sendText()
        }
    }

    fun sendText()
    {
        val df = DecimalFormat("#")
        val numberList: ArrayList<Double>
        var index = 0
        val textEdit: EditText = findViewById(R.id.textMessage)

        numberList = (this.application as NumberLister).getList() //retrieve contact list from shared preferences

        val smsManager = SmsManager.getDefault()
        while (index < numberList.size)
        {
            smsManager.sendTextMessage(df.format(numberList[index++]), null, textEdit.text.toString(), null, null)
        }
        Toast.makeText(this@Texting, "Texts have been sent!", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        val startMain = Intent(this@Texting, MainActivity::class.java)
        startActivity(startMain)
        this.finish()
    }
}