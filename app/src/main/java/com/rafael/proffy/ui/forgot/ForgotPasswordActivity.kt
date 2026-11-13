package com.rafael.proffy.ui.forgot

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.rafael.proffy.R
import com.rafael.proffy.databinding.ActivityForgotPasswordBinding
import com.rafael.proffy.ui.finallyComponent.FinallyActivity
import com.rafael.proffy.utils.ValidationUtils

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root

        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                maxOf(systemBars.bottom, ime.bottom)
            )
            insets
        }

        val buttonGoBack = binding.buttonGoBack
        val textInputEmail = binding.textInputEditEmail
        val buttonSend = binding.buttonSend

        val enabledButtonColor = ContextCompat.getColor(this, R.color.green)
        val disabledButtonColor = ContextCompat.getColor(this, R.color.shape_disable)

        setButtonState(false, enabledButtonColor, disabledButtonColor)

        buttonGoBack.setOnClickListener {
            goBack()
        }

        textInputEmail.addTextChangedListener {
            val email = textInputEmail.text?.toString()?.trim() ?: ""
            val validEmail = ValidationUtils.isValidEmail(email)
            showEmailError(email.isNotEmpty() && !validEmail)
            setButtonState(validEmail, enabledButtonColor, disabledButtonColor)
        }

        textInputEmail.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.scrollViewForgot.post {
                    binding.scrollViewForgot.smoothScrollTo(0, buttonSend.bottom)
                }
            }
        }

        buttonSend.setOnClickListener {
            handleSend()
        }
    }

    private fun goBack() {
        finish()
    }

    private fun showEmailError(hasError: Boolean) {
        binding.textInputLayoutEmail.error =
            if (hasError) getString(R.string.forgot_password_error_invalid_email) else null
    }

    private fun setButtonState(enabled: Boolean, enabledColor: Int, disabledColor: Int) {
        binding.buttonSend.isEnabled = enabled
        val color = if (enabled) enabledColor else disabledColor

        binding.buttonSend.backgroundTintList = ColorStateList.valueOf(color)
        val textColor = if (enabled)
            ContextCompat.getColor(this, R.color.shape_01_white)
        else
            ContextCompat.getColor(this, R.color.text_complement)
        binding.buttonSend.setTextColor(textColor)
        binding.buttonSend.text = getString(
            if (enabled) R.string.forgot_password_button_send else R.string.forgot_password_button_disabled
        )
    }

    private fun handleSend() {
        val email = binding.textInputEditEmail.text?.toString()?.trim() ?: ""

        if (!ValidationUtils.isValidEmail(email)) {
            showEmailError(true)
            return
        }

        val intent = Intent(this, FinallyActivity::class.java)
        intent.putExtra("textViewTitleFinally", getString(R.string.forgot_password_finally_title))
        intent.putExtra("textViewSubtitleFinally", getString(R.string.forgot_password_finally_subtitle))
        intent.putExtra("titleButton", getString(R.string.forgot_password_finally_button))

        startActivity(intent)
        finish()
    }
}
