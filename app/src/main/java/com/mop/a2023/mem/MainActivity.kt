package com.mop.a2023.mem

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val writeDiaryButton: Button = findViewById(R.id.writeDiaryButton)
        writeDiaryButton.setOnClickListener{
            showDiarySettingsDialog()
        }

    }
    private fun showDiarySettingsDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_settings, null)
        val orientationRadioGroup: RadioGroup = dialogView.findViewById(R.id.orientationRadioGroup)
        val templateRadioGroup: RadioGroup = dialogView.findViewById(R.id.templateRadioGroup)

        orientationRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            templateRadioGroup.removeAllViews()
            when (checkedId) {
                R.id.radioLandscape -> {
                    addTemplateRadioButton(templateRadioGroup, R.drawable.landscape_1, 1)
                    addTemplateRadioButton(templateRadioGroup, R.drawable.landscape_2, 2)
                    addTemplateRadioButton(templateRadioGroup, R.drawable.total, 3)
                }
                R.id.radioPortrait -> {
                    addTemplateRadioButton(templateRadioGroup, R.drawable.portrait_1, 4)
                    addTemplateRadioButton(templateRadioGroup, R.drawable.portrait_2, 5)
                    addTemplateRadioButton(templateRadioGroup, R.drawable.total, 6)
                }
            }
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("일기 설정")
        builder.setView(dialogView)

        builder.setPositiveButton("확인") { _, _ ->
            val selectedOrientation = when (orientationRadioGroup.checkedRadioButtonId) {
                R.id.radioLandscape -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                R.id.radioPortrait -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                else -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }

            val selectedTemplate = templateRadioGroup.findViewById<RadioButton>(templateRadioGroup.checkedRadioButtonId)?.tag as Int

            startTemplateActivity(selectedOrientation, selectedTemplate)
        }

        builder.setNegativeButton("취소", null)

        val dialog = builder.create()
        dialog.show()
    }

    private fun addTemplateRadioButton(group: RadioGroup, drawableRes: Int, id: Int) {
        val imageView = ImageView(this).apply {
            this.setImageResource(drawableRes)
            this.tag = id
            this.id = View.generateViewId()
            this.layoutParams = RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8.dpToPx(), 8.dpToPx(), 8.dpToPx(), 8.dpToPx())
            }
            this.setOnClickListener {
                group.check(this.id)
            }
        }
        group.addView(imageView)
    }

    private fun startTemplateActivity(orientation: Int, templateNumber: Int) {
        val intent = Intent(this, TemplateActivity::class.java).apply {
            putExtra("ORIENTATION", orientation)
            putExtra("TEMPLATE_NUMBER", templateNumber)
        }
        startActivity(intent)
    }
}
fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

class TemplateActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val orientation = intent.getIntExtra("ORIENTATION", ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        val templateNumber = intent.getIntExtra("TEMPLATE_NUMBER", 1)

        requestedOrientation = orientation

        // 템플릿 번호에 따라 다른 레이아웃 파일을 설정
        when (templateNumber) {
            1 -> setContentView(R.layout.template1)
            2 -> setContentView(R.layout.template2)
            3 -> setContentView(R.layout.template3)
            4 -> setContentView(R.layout.template4)
            5 -> setContentView(R.layout.template5)
            6 -> setContentView(R.layout.template6)
            else -> setContentView(R.layout.template1)
        }
    }

}
