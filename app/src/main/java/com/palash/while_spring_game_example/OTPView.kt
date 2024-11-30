package com.palash.while_spring_game_example

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.util.AttributeSet
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat

class OTPView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private lateinit var otpFields: Array<EditText>
    private var currentFocusIndex = 0

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        initViews(context)
    }

    private fun initViews(context: Context) {
        otpFields = Array(5) { createOTPField(context) }

        for (i in otpFields.indices) {
            val params = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
            otpFields[i].layoutParams = params
            addView(otpFields[i])
        }

        otpFields[0].requestFocus()
    }

    private fun createOTPField(context: Context): EditText {
        val editText = EditText(context).apply {
            setBackgroundResource(R.drawable.otp_field_border)
            gravity = Gravity.CENTER
            //setMaxLength(1)
            inputType = EditorInfo.TYPE_CLASS_NUMBER
            setTextColor(Color.BLACK)
            setHint("•")
        }

        editText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1 && currentFocusIndex < otpFields.size - 1) {
                    otpFields[currentFocusIndex + 1].requestFocus()
                    currentFocusIndex++
                } else if (s?.length == 0 && currentFocusIndex > 0) {
                    otpFields[currentFocusIndex - 1].requestFocus()
                    currentFocusIndex--
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        return editText
    }

    // Function to get the OTP input
    fun getOTP(): String {
        return otpFields.joinToString("") { it.text.toString() }
    }
}
