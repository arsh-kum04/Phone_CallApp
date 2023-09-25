package com.example.phone_callapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes

class MainActivity : AppCompatActivity() {
    private lateinit var phoneNumberEditText: EditText
    private lateinit var call_btn: Button
    private val CALL_MAKE_PERMISSION=Manifest.permission.CALL_PHONE
    private val RECORD_AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO
    private val WRITE_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val REQUEST_PERMISSIONS_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        phoneNumberEditText = findViewById(R.id.edittext_button)
        call_btn = findViewById(R.id.makeCall_button)

        // Request permissions
        if (!checkPermissions()) {
            requestPermissions()
        }

        val classStateReceiver = ClassStateReceiver()
        call_btn.setOnClickListener {
            val phoneNumber = phoneNumberEditText.text.toString()
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNumber")
            classStateReceiver.onReceive(this,intent)
            startActivity(intent)

        }

        Toast.makeText(this,"Recording store at: ${classStateReceiver.getRecordingFilePath()}",Toast.LENGTH_LONG)
    }
    private fun checkPermissions(): Boolean {
        val callPermission =
            ContextCompat.checkSelfPermission(this, CALL_MAKE_PERMISSION) ==
                    PackageManager.PERMISSION_GRANTED
        val recordAudioPermission =
            ContextCompat.checkSelfPermission(this, RECORD_AUDIO_PERMISSION) ==
                    PackageManager.PERMISSION_GRANTED
        val writeExternalStoragePermission =
            ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE_PERMISSION) ==
                    PackageManager.PERMISSION_GRANTED
        return recordAudioPermission && writeExternalStoragePermission && callPermission
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(CALL_MAKE_PERMISSION,RECORD_AUDIO_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION),
            REQUEST_PERMISSIONS_CODE
        )
    }

}
