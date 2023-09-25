package com.example.phone_callapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.io.InputStream as InputStream1

class ClassStateReceiver : BroadcastReceiver() {
    private var isRecording = false
    private var mediaRecorder: MediaRecorder? = null
    private var recordingFilePath: String? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        val telephonyManager =
            context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if(intent?.action==TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                telephonyManager.registerTelephonyCallback(
                    context.mainExecutor,
                    object : TelephonyCallback(),
                        TelephonyCallback.CallStateListener {
                        override fun onCallStateChanged(state:Int) {
                            when (state) {
                                TelephonyManager.CALL_STATE_RINGING -> {
                                    startRecording()
                                }

                                TelephonyManager.CALL_STATE_IDLE -> {
                                    stopRecording()
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    private fun startRecording() {
        if (!isRecording) {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(Date())
            val fileName = "Recording_$timeStamp.mp4"
            recordingFilePath = Environment.getExternalStorageDirectory().path

            mediaRecorder?.apply {
                setAudioSource(MediaRecorder.AudioSource.VOICE_CALL)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(recordingFilePath)

                try {
                    prepare()
                    start()
                    isRecording = true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun stopRecording() {
        if (isRecording) {
            try {
                mediaRecorder?.stop()
                mediaRecorder?.reset()
                mediaRecorder?.release()
                isRecording = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }
    fun getRecordingFilePath(): String? {
        return recordingFilePath
    }

}
