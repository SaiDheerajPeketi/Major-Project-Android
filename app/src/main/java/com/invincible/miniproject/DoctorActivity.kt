package com.invincible.miniproject

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.invincible.miniproject.ui.theme.MiniProjectTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class DoctorActivity : ComponentActivity() {

    private val CAMERA_REQUEST_CODE = 101
    private val PDF_FILE_NAME = "captured_image.pdf"

    var fileName:String? = null
//    var dataL:
//            ActivityResultLauncher<String>?

//    LaunchedEffect(key1 = true) {
        val data = registerForActivityResult(ActivityResultContracts.GetContent()){item ->
            fileName = "Selected File: " + getFileNameFromUri(this@DoctorActivity, item)!!
        }
//        data = data
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiniProjectTheme {
//                var bool = true
                var bool by remember {
                    mutableStateOf(true)
                }
                if(bool) {
                    val items = (0 until 50).toList() // Generate a list of 10 items

                    LazyColumn(modifier = Modifier
                        .fillMaxSize()
//                        .indication(indication = null)
//                        .clickable(
//                            indication = null,
//                            interactionSource = remember { MutableInteractionSource() } // This is mandatory
//                        ) {
//                            bool = false
//                        }
                        .clickable {bool = false})
                        {
                        items(items) { index ->
                            MyItem(index)
                        }
                    }
                }
                else {
                    var fileName by remember {
                    mutableStateOf("Selected File: ")
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = "Add Prescription")

                    Button(
                        onClick = {
                            // Launch camera to capture image
                            dispatchTakePictureIntent()
                        }
                    ) {
                        Text(text = "Take a picture")
                    }

                    Button(
                        onClick = {

                            data.launch("*/*")
                        }
                    ) {
                        Text(text = "Select from Device")
                    }
                    var pdfFile: File? = fileName?.let { File(it) }
                    LaunchedEffect(key1 = true) {
                        while(true) {
                            pdfFile = fileName?.let { File(it) }
                            delay(100)
                        }
                    }
//                    PdfViewer()
//                    pdfFile?.let { PdfViewer(pdfFile = it) }


                    fileName?.let { Text(text = it) }
                }
                }

//
            }
        }
    }

    @Composable
    fun MyItem(index: Int) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
//                .clickable { bool = false}
        ) {
            Image(imageVector = Icons.Default.AccountCircle, contentDescription = "NULL", modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = "Name $index", fontSize = 18.sp)
                Text(text = "Description $index", fontSize = 14.sp)
            }
        }
    }

    @Composable
    fun PdfViewer() {
//        Toast(this, pdfFile.toString(), Toast.LENGTH_SHORT).show()
        var tmp = "file:///storage/emulated/0/Android/data/com.invincible.miniproject/files/Documents/captured_image.pdf"
        var pdfFile: File? = tmp?.let { File(it) }
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            request?.url?.let {
                                loadUrl(it.toString())
                            }
                            return true
                        }
                    }
                    if (pdfFile != null) {
                        loadUrl("https://docs.google.com/gview?embedded=true&url=${pdfFile.absolutePath}")
                    }
                }
            }
        )
    }
    private fun convertImageToPdf(imageBitmap: Bitmap, context: Context): Uri? {
        return runCatching {
            val pdfDocument = PdfDocument()
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "captured_image.pdf")
            val fileOutputStream = FileOutputStream(file)

            val pageInfo = PdfDocument.PageInfo.Builder(imageBitmap.width, imageBitmap.height, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            canvas.drawBitmap(imageBitmap, 0f, 0f, null)
            pdfDocument.finishPage(page)

            pdfDocument.writeTo(fileOutputStream)
            pdfDocument.close()
            fileOutputStream.close()

            // Return URI of the created PDF file
            Uri.fromFile(file)

        }.getOrElse {
            it.printStackTrace()
            null
        }
    }
    fun getFileNameFromUri(context: Context, uri: Uri?): String? {
        var fileName: String? = null
        var cursor: Cursor? = null
        try {
            // Query the content resolver to get the file name
            cursor = context.contentResolver.query(uri!!, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                // Get the column index of the file name
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = cursor.getString(nameIndex)
                }
            }
        } finally {
            cursor?.close()
        }
        return fileName
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Camera not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            // Convert captured image to PDF
            Log.e("myDebugTAG", convertImageToPdf(imageBitmap, this).toString())
        }
    }

    private fun convertImageToPdf(imageBitmap: Bitmap) {
        GlobalScope.launch(Dispatchers.IO) {
            val pdfDocument = PdfDocument()
            val filePath = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "captured_image.pdf")
            try {
                val fileOutputStream = FileOutputStream(filePath)

                val pageInfo = PdfDocument.PageInfo.Builder(imageBitmap.width, imageBitmap.height, 1).create()
                val page = pdfDocument.startPage(pageInfo)
                val canvas = page.canvas
                canvas.drawBitmap(imageBitmap, 0f, 0f, null)
                pdfDocument.finishPage(page)

                pdfDocument.writeTo(fileOutputStream)
                pdfDocument.close()

                fileOutputStream.close()

                runOnUiThread {
                    Toast.makeText(this@DoctorActivity, "PDF saved at: ${filePath.absolutePath}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}

