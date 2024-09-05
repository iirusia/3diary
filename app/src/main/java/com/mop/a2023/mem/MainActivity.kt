package com.mop.a2023.mem

import TemplateActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mop.a2023.mem.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val writeDiaryButton: Button = findViewById(R.id.writeDiaryButton)
        writeDiaryButton.setOnClickListener {
            showDiarySettingsDialog()
        }
    }

    private fun showDiarySettingsDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_settings, null)
        val orientationRadioGroup: RadioGroup = dialogView.findViewById(R.id.orientationRadioGroup)
        val landscapeTemplateRadioGroup: RadioGroup = dialogView.findViewById(R.id.landscapeTemplateRadioGroup)
        val portraitTemplateRadioGroup: RadioGroup = dialogView.findViewById(R.id.portraitTemplateRadioGroup)

        orientationRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            // Hide both groups initially
            landscapeTemplateRadioGroup.visibility = View.GONE
            portraitTemplateRadioGroup.visibility = View.GONE

            // Show appropriate template group based on selected orientation
            when (checkedId) {
                R.id.radioLandscape -> {
                    landscapeTemplateRadioGroup.visibility = View.VISIBLE
                }
                R.id.radioPortrait -> {
                    portraitTemplateRadioGroup.visibility = View.VISIBLE
                }
            }
        }

        landscapeTemplateRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioTemplateLandscape1 -> startTemplateActivity(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, 1)
                R.id.radioTemplateLandscape2 -> startTemplateActivity(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, 2)
                R.id.radioTemplateLandscape3 -> startTemplateActivity(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, 3)
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

            val selectedTemplate = when {
                landscapeTemplateRadioGroup.checkedRadioButtonId != -1 ->
                    landscapeTemplateRadioGroup.findViewById<RadioButton>(landscapeTemplateRadioGroup.checkedRadioButtonId).tag as Int
                portraitTemplateRadioGroup.checkedRadioButtonId != -1 ->
                    portraitTemplateRadioGroup.findViewById<RadioButton>(portraitTemplateRadioGroup.checkedRadioButtonId).tag as Int
                else -> null
            }

            if (selectedTemplate != null) {
                startTemplateActivity(selectedOrientation, selectedTemplate)
            }
        }

        builder.setNegativeButton("취소", null)

        val dialog = builder.create()
        dialog.show()
    }

    private fun addTemplateImageView(group: RadioGroup, drawableRes: Int, id: Int) {
        val imageView = ImageView(this).apply {
            setImageResource(drawableRes)
            tag = id
            this.id = View.generateViewId()
            layoutParams = RadioGroup.LayoutParams(
                50.dpToPx(), // 너비를 조정하여 이미지 크기를 조정
                50.dpToPx()  // 높이를 조정하여 이미지 크기를 조정
            ).apply {
                setMargins(8.dpToPx(), 8.dpToPx(), 8.dpToPx(), 8.dpToPx())
            }

            // 이미지를 클릭하면 RadioGroup이 체크되도록 설정
            setOnClickListener {
                group.check(this.id)
            }
        }

        // `RadioButton`처럼 이미지를 동작하게 설정
        group.addView(imageView)

        // `RadioGroup`에 이미지의 선택 상태를 반영
        group.setOnCheckedChangeListener { _, checkedId ->
            // 선택된 이미지의 배경 색상을 변경하여 강조 표시
            for (i in 0 until group.childCount) {
                val child = group.getChildAt(i)
                if (child is ImageView) {
                    child.setBackgroundColor(
                        if (child.id == checkedId) getColor(android.R.color.holo_blue_light) // 선택된 경우
                        else getColor(android.R.color.transparent) // 선택되지 않은 경우
                    )
                }
            }
        }
    }

    private fun showTemplatesForLandscape(group: RadioGroup) {
        addTemplateImageView(group, R.drawable.landscape_1, 1)
        addTemplateImageView(group, R.drawable.landscape_2, 2)
        addTemplateImageView(group, R.drawable.total, 3)
    }

    private fun showTemplatesForPortrait(group: RadioGroup) {
        addTemplateImageView(group, R.drawable.portrait_1, 4)
        addTemplateImageView(group, R.drawable.portrait_2, 5)
        addTemplateImageView(group, R.drawable.total, 6)
    }

    private fun startTemplateActivity(orientation: Int, templateNumber: Int) {
        val layoutResource = when (templateNumber) {
            1 -> R.layout.template6  // 가로 모드에서 landscape_1 템플릿을 선택한 경우 template1.xml 로 전환
            2 -> R.layout.template6  // 가로 모드에서 landscape_2 템플릿을 선택한 경우 template2.xml 로 전환
            3 -> R.layout.template6  // 가로 모드에서 landscape_3 템플릿을 선택한 경우 template3.xml 로 전환
            4 -> R.layout.template6  // 세로 모드에서 portrait_1 템플릿을 선택한 경우 template1.xml 로 전환
            5 -> R.layout.template6  // 세로 모드에서 portrait_2 템플릿을 선택한 경우 template2.xml 로 전환
            else -> R.layout.template6  // 기본 템플릿
        }

        val intent = Intent(this, TemplateActivity::class.java).apply {
            putExtra("ORIENTATION", orientation)
            putExtra("LAYOUT_RESOURCE", layoutResource)
        }
        startActivity(intent)
    }
}

fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}
