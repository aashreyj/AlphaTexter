package com.example.alphatexter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.telephony.SmsManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.budiyev.android.circularprogressbar.CircularProgressBar
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.lang.Exception
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Make directory for storage of excel file
        this.getExternalFilesDir(null)?.mkdirs() //create the internal storage directory

        //Initialize all ButtonViews
        val startCallButton: Button = findViewById(R.id.startCallingButton)
        val clearDataButton: Button = findViewById(R.id.clearDatabaseButton)
        val importButton: Button = findViewById(R.id.importButton)

        refreshTextAndProgress() //Give all UI items their initial states

        if (clearDataButton.isClickable && clearDataButton.isEnabled)
        {
            //purge database and enable importing again
            clearDataButton.setOnClickListener {
                (this.application as NumberLister).setList(arrayListOf())
                refreshTextAndProgress()
            }

            importButton.setOnClickListener {
                readExcel(this, "test.xlsx")
                Toast.makeText(this@MainActivity, "Done Importing!", Toast.LENGTH_SHORT).show()
                refreshTextAndProgress()
            }

            startCallButton.setOnClickListener {
                refreshTextAndProgress()
                sendText()
            }
        }
        if (importButton.isClickable && importButton.isEnabled)
        {
            //import contacts from excel sheet
            importButton.setOnClickListener {
                readExcel(this, "testing.xlsx")
                Toast.makeText(this@MainActivity, "Done Importing!", Toast.LENGTH_SHORT).show()
                refreshTextAndProgress()
            }

            startCallButton.setOnClickListener {
                refreshTextAndProgress()
                sendText()
            }

            clearDataButton.setOnClickListener {
                (this.application as NumberLister).setList(arrayListOf())
                refreshTextAndProgress()
            }
        }
    }

    private fun refreshTextAndProgress() //Utility function to Update Progress bar and Text of all TextViews
    {
        val progressBar: CircularProgressBar = findViewById(R.id.progress_bar)
        val contactNumberLabel: TextView = findViewById(R.id.contactNumberLabel)
        val startCallButton: Button = findViewById(R.id.startCallingButton)
        val importButton: Button = findViewById(R.id.importButton)
        val clearDataButton: Button = findViewById(R.id.clearDatabaseButton)
        val numberList = (this.application as NumberLister).getList()
        val numbers: Int
        val message: String

        if(numberList.size == 0) {
            Toast.makeText(this@MainActivity, "Database is Empty! Load Data...", Toast.LENGTH_SHORT).show()
            numbers = 0
            progressBar.progress = 0f
            startCallButton.isClickable = false
            startCallButton.isEnabled = false
            clearDataButton.isEnabled = false
            clearDataButton.isClickable = false
            importButton.isClickable = true
            importButton.isEnabled = true

            message = String.format("Contacts Loaded: %1$2s", numbers)
            contactNumberLabel.text = message
        }
        else
        {
            numbers = numberList.size
            progressBar.progress = 65f
            progressBar.animate()
            startCallButton.isClickable = true
            startCallButton.isEnabled = true
            clearDataButton.isEnabled = true
            clearDataButton.isClickable = true
            importButton.isClickable = false
            importButton.isEnabled = false

            message = String.format("Contacts Loaded: %1$2s", numbers)
            contactNumberLabel.text = message
        }
    }

    private fun readExcel(context: Context, filename: String) //function to read Excel file and import data into SQLite Database
    {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly())
        {
            Toast.makeText(this, "Storage not available or is read only", Toast.LENGTH_SHORT).show()
            return
        }

        try
        {
            val file = File(context.getExternalFilesDir(null)?.toString(), filename)
            val pkg = OPCPackage.open(file)
            val myWorkBook = XSSFWorkbook(pkg)

            // Get the first sheet from workbook
            val mySheet = myWorkBook.getSheetAt(0)

            // We now need something to iterate through the cells.
            val rowIterator = mySheet.rowIterator()

            while(rowIterator.hasNext())
            {
                val myRow = rowIterator.next() as XSSFRow
                val cellIterator = myRow.cellIterator()
                while(cellIterator.hasNext())
                {
                    val myCell = cellIterator.next() as XSSFCell
                    //numberData?.storeNumber(myCell.toString()) //add data of current cell to the database
                    (this.application as NumberLister).numbers.add(myCell.numericCellValue)

                }
            }
            pkg.close()
        }
        catch(e: Exception)
        {
            e.printStackTrace()
        }
        return
    }

    private fun isExternalStorageReadOnly(): Boolean //function to check if External Storage is Read Only
    {
        val extStorageState = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState)
        {
            return true
        }
        return false
    }

    private fun isExternalStorageAvailable(): Boolean //function to check if External Storage is Available
    {
        val extStorageState = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == extStorageState)
        {
            return true
        }
        return false
    }

    fun sendText()
    {
        val df = DecimalFormat("#")
        var numberList: ArrayList<Double>
        var index: Int = 0

        numberList = (this.application as NumberLister).getList() //retrieve contact list from shared preferences

        val smsManager = SmsManager.getDefault()
        while (index < numberList.size)
        {
            smsManager.sendTextMessage(df.format(numberList[index++]), null, "This is a demo.", null, null)
        }
        Toast.makeText(this@MainActivity, "Texts have been sent!",Toast.LENGTH_SHORT).show()
    }
}
