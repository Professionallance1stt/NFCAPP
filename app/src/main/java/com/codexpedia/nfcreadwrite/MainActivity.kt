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
    private lateinit var nfcAdapter: NfcAdapter
    private var androidBeamAvailable = false
    private var parentPath: File? = null
    private val fileUris = mutableListOf<Uri>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pm = this.packageManager
        // Check whether NFC is available on device
        if (!pm.hasSystemFeature(PackageManager.FEATURE_NFC)) {
            // NFC is not available on the device.
            Toast.makeText(
                this, "The device does not has NFC hardware.",
                Toast.LENGTH_SHORT
            ).show()
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            // Android Beam feature is not supported.
            Toast.makeText(
                this, "Android Beam is not supported.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // NFC and Android Beam file transfer is supported.
            Toast.makeText(
                this, "Android Beam is supported on your device.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun sendFile(view: View?) {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        val fileName = null
        // Retrieve the path to the user's public pictures directory
        val fileDirectory = Environment
            .getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            )

        // Create a new file using the specified directory and name
        val fileToTransfer = File(fileDirectory, fileName)
        fileToTransfer.setReadable(true, false)

    }

    fun handleFileUri(beamUri: Uri): File? =
        // Get the path part of the URI
        beamUri.path.let { fileName ->
            // Create a File object for this filename
            File(fileName)
                // Get the file's parent directory
                .parentFile
        }

    private fun handleContentUri(beamUri: Uri): File? =
        // Test the authority of the URI
        if (beamUri.authority == MediaStore.AUTHORITY) {

        } else {
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
        }

    private fun handleViewIntent() {

        if (TextUtils.equals(intent.action, Intent.ACTION_VIEW)) {
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
   /*
    private lateinit var nfcAdapter: NfcAdapter
    // Flag to indicate that Android Beam is available
    private var androidBeamAvailable = false
    private var parentPath: File? = null
    private val fileUris = mutableListOf<Uri>()
    private inner class FileUriCallback : NfcAdapter.CreateBeamUrisCallback {
        override fun createBeamUris(event: NfcEvent): Array<Uri> {
            return fileUris.toTypedArray()
        }
    }

        public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        androidBeamAvailable = if (!packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)) {
            false
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            androidBeamAvailable = false
            false
        } else {
            nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            true
        }
            nfcAdapter = NfcAdapter.getDefaultAdapter(this).apply {

                var fileUriCallback = FileUriCallback()
                nfcAdapter.setBeamPushUrisCallback(fileUriCallback, this@MainActivity)
            }



    }
    fun handleFileUri(beamUri: Uri): File? =
        // Get the path part of the URI
        beamUri.path.let { fileName ->
            // Create a File object for this filename
            File(fileName)
                // Get the file's parent directory
                .parentFile
        }
    private fun handleContentUri(beamUri: Uri): File? =
        // Test the authority of the URI
        if (beamUri.authority == MediaStore.AUTHORITY) {

        } else {
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
        }
    private fun handleViewIntent() {

        if (TextUtils.equals(intent.action, Intent.ACTION_VIEW)) {
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
*/
/*
  lateinit var writeTagFilters: Array<IntentFilter>
    private lateinit var tvNFCContent: TextView
    private lateinit var message: TextView
    private lateinit var btnWrite: Button
    private lateinit var binding: ActivityMainBinding
    var nfcAdapter: NfcAdapter? = null
    var pendingIntent: PendingIntent? = null
    var writeMode = false
    var myTag: Tag? = null
//
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvNFCContent = binding.nfcContents
        message = binding.editMessage
        btnWrite = binding.button

        btnWrite.setOnClickListener {
            try {
                if (myTag == null) {
                    Toast.makeText(this, ERROR_DETECTED, Toast.LENGTH_LONG).show()
                } else {
                    write(message.text.toString(), myTag)
                    Toast.makeText(this, WRITE_SUCCESS, Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                Toast.makeText(this, WRITE_ERROR, Toast.LENGTH_LONG).show()
                e.printStackTrace()
            } catch (e: FormatException) {
                Toast.makeText(this, WRITE_ERROR, Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show()
            finish()
        }

        //For when the activity is launched by the intent-filter for android.nfc.action.NDEF_DISCOVERE
        readFromIntent(intent)
        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            0
        )
        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT)
        writeTagFilters = arrayOf(tagDetected)
    }

 //Read NFC tag
    private fun readFromIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action || NfcAdapter.ACTION_TECH_DISCOVERED == action || NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            myTag = intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag?
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            var msgs = mutableListOf<NdefMessage>()
            if (rawMsgs != null) {
                for (i in rawMsgs.indices) {
                    msgs.add(i, rawMsgs[i] as NdefMessage)
                }
                buildTagViews(msgs.toTypedArray())
            }
        }
    }

    private fun buildTagViews(msgs: Array<NdefMessage>) {
        if (msgs == null || msgs.isEmpty()) return
        var text = ""
        val payload = msgs[0].records[0].payload
        val textEncoding: Charset = if ((payload[0] and 128.toByte()).toInt() == 0) Charsets.UTF_8 else Charsets.UTF_16 // Get the Text Encoding
        val languageCodeLength: Int = (payload[0] and 51).toInt() // Get the Language Code, e.g. "en"
        try {
            // Get the Text
            text = String(
                payload,
                languageCodeLength + 1,
                payload.size - languageCodeLength - 1,
                textEncoding
            )
        } catch (e: UnsupportedEncodingException) {
            Log.e("UnsupportedEncoding", e.toString())
        }
        tvNFCContent.text = "Message read from NFC Tag:\n $text"
    }

 //Write the NFC Tag
    @Throws(IOException::class, FormatException::class)
    private fun write(text: String, tag: Tag?) {
        val records = arrayOf(createRecord(text))
        val message = NdefMessage(records)
        // Get an instance of Ndef for the tag.
        val ndef = Ndef.get(tag)
        // Enable I/O
        ndef.connect()
        // Write the message
        ndef.writeNdefMessage(message)
        // Close the connection
        ndef.close()
    }

    @Throws(UnsupportedEncodingException::class)
    private fun createRecord(text: String): NdefRecord {
        val lang = "en"
        val textBytes = text.toByteArray()
        val langBytes = lang.toByteArray(charset("US-ASCII"))
        val langLength = langBytes.size
        val textLength = textBytes.size
        val payload = ByteArray(1 + langLength + textLength)

        // set status byte (see NDEF spec for actual bits)
        payload[0] = langLength.toByte()
        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength)
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength)
        return NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), payload)
    }

//Reading the NFC Tag while the Apps is Launched
    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        readFromIntent(intent)
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        }
    }

    public override fun onPause() {
        super.onPause()
        WriteModeOff()
    }

    public override fun onResume() {
        super.onResume()
        WriteModeOn()
    }


      //Enable Write and foreground dispatch to prevent intent-filter to launch the app again

    private fun WriteModeOn() {
        writeMode = true
        nfcAdapter!!.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null)
    }


      //Disable Write and foreground dispatch to allow intent-filter to launch the app

    private fun WriteModeOff() {
        writeMode = false
        nfcAdapter!!.disableForegroundDispatch(this)
    }

    companion object {
        const val ERROR_DETECTED = "NFC Tag not Detected"
        const val WRITE_SUCCESS = "Text was sent to tag!"
        const val WRITE_ERROR = "Error during the write"
    }
 */