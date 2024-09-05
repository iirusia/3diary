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

class TemplateActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 전달된 인텐트에서 레이아웃 리소스를 가져옴
        val layoutResource = intent.getIntExtra("LAYOUT_RESOURCE", R.layout.template1)
        val orientation = intent.getIntExtra("ORIENTATION", ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)

        // 화면 방향 설정
        requestedOrientation = orientation

        // 선택된 레이아웃 리소스를 설정
        setContentView(layoutResource)

        val selectImageButton: Button = findViewById(R.id.selectImageButton)
        val imageView: ImageView = findViewById(R.id.imageView)

        selectImageButton.setOnClickListener {
            val options = arrayOf("Camera", "Gallery")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select Image From")
            builder.setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            builder.show()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 100)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val imageView: ImageView = findViewById(R.id.imageView)
            when (requestCode) {
                100 -> { // Camera
                    val photo = data?.extras?.get("data") as? Bitmap
                    imageView.setImageBitmap(photo)
                }
                200 -> { // Gallery
                    val imageUri: Uri? = data?.data
                    imageView.setImageURI(imageUri)
                }
            }
        }
    }
}
