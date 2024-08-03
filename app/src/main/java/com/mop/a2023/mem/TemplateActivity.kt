import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.mop.a2023.mem.R
import java.io.IOException

class TemplateActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_SELECT = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val orientation = intent.getIntExtra("ORIENTATION", ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        val templateNumber = intent.getIntExtra("TEMPLATE_NUMBER", 1)

        requestedOrientation = orientation

        when (templateNumber) {
            1 -> setContentView(R.layout.template1)
            2 -> setContentView(R.layout.template2)
            3 -> setContentView(R.layout.template3)
            4 -> setContentView(R.layout.template4)
            5 -> setContentView(R.layout.template5)
            6 -> setContentView(R.layout.template6)
            else -> setContentView(R.layout.template1)
        }

        imageView = findViewById(R.id.imageView)
        val selectImageButton: Button = findViewById(R.id.selectImageButton)

        selectImageButton.setOnClickListener {
            showImageSourceDialog()
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Image Source")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> takePhotoFromCamera()
                1 -> choosePhotoFromGallery()
            }
        }
        builder.show()
    }

    private fun takePhotoFromCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun choosePhotoFromGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_SELECT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    imageView.setImageBitmap(imageBitmap)
                }
                REQUEST_IMAGE_SELECT -> {
                    val selectedImageUri: Uri? = data?.data
                    if (selectedImageUri != null) {
                        try {
                            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImageUri)
                            imageView.setImageBitmap(bitmap)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }
}
