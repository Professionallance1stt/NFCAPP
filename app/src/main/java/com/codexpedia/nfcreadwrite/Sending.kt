package com.codexpedia.nfcreadwrite

import android.net.Uri
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import java.io.File

class Sending : AppCompatActivity()
{
    private lateinit var nfcAdapter: NfcAdapter
    private var androidBeamAvailable = false
    private val fileUris = mutableListOf<Uri>()

    private inner class FileUriCallback : NfcAdapter.CreateBeamUrisCallback {

        override fun createBeamUris(event: NfcEvent): Array<Uri> {
            return fileUris.toTypedArray()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sending)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this).apply {
            val fileUris = mutableListOf<Uri>()
            val transferFile = null
            val extDir = getExternalFilesDir(null)
            val requestFile = File(extDir, transferFile).apply {
                setReadable(true, false)
            }
            Uri.fromFile(requestFile)?.also { fileUri ->
                fileUris += fileUri
            } ?: Log.e("My Activity", "No File URI available for file.")
            FileUriCallback();
            var fileUriCallback = FileUriCallback()
            nfcAdapter.setBeamPushUrisCallback(fileUriCallback, this@Sending)


        }
    }
    fun sendFile(view: View?) {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        val fileName = null
        val fileDirectory = Environment
            .getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            )

        val fileToTransfer = File(fileDirectory, fileName)
        fileToTransfer.setReadable(true, false)

    }


}