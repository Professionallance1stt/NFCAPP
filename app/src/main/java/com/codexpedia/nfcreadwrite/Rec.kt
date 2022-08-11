package com.codexpedia.nfcreadwrite

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import java.io.File

class Rec : AppCompatActivity() {
    private var parentPath: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rec)
    }

    private fun handleContentUri(beamUri: Uri): File? =

        if (beamUri.authority != MediaStore.AUTHORITY) {
            val projection = arrayOf(MediaStore.MediaColumns.DATA)
            val pathCursor = contentResolver.query(beamUri, projection, null, null, null)

            if (pathCursor?.moveToFirst() == true) {
                pathCursor.getColumnIndex(MediaStore.MediaColumns.DATA).let { filenameIndex ->
                    pathCursor.getString(filenameIndex).let { fileName ->
                        File(fileName)
                    }.parentFile
                }
            } else {

                null
            }
        } else {
            null
        }


    fun handleFileUri(beamUri: Uri): File? =

        beamUri.path.let { fileName ->
            File(fileName)
                .parentFile
        }

    private fun handleViewIntent() {

        if (TextUtils.equals(intent.action, Intent.ACTION_VIEW)) {
            // Get the URI from the Intent
            intent.data?.also { beamUri ->
                parentPath = when (beamUri.scheme) {
                    "file" -> handleFileUri(beamUri)
                    "content" -> handleContentUri(beamUri)
                    else -> null
                }
            }
        }
    }
}
