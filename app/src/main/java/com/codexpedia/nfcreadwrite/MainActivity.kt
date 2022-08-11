package com.codexpedia.nfcreadwrite

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.nfc.*
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import java.io.File


class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pm = this.packageManager
        /*
        if (!pm.hasSystemFeature(PackageManager.FEATURE_NFC)) {
            Toast.makeText(
                this, "The device does not has NFC hardware.",
                Toast.LENGTH_SHORT
            ).show()
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Toast.makeText(
                this, "Android Beam is not supported.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this, "Android Beam is supported on your device.",
                Toast.LENGTH_SHORT
            ).show()
        }

         */


    }
}

