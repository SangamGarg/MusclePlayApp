package com.sangam.muscleplay.userRegistration.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.sangam.muscleplay.appUtils.HideKeyboard
import com.sangam.muscleplay.appUtils.HideStatusBarUtil
import com.sangam.muscleplay.appUtils.ToastUtil
import com.sangam.muscleplay.R
import com.sangam.muscleplay.databinding.ActivitySignUpBinding
import com.sangam.muscleplay.databinding.ErrorBottomDialogLayoutBinding
import com.sangam.muscleplay.userRegistration.model.UserDetailsPostRequestBody
import com.sangam.muscleplay.userRegistration.viewModel.UserDetailsViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var userDetailsViewModel: UserDetailsViewModel
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth

    private val emailPattern =
        "[a-zA-Z0-9._%+-]+@(gmail\\.com|yahoo\\.com|outlook\\.com|hotmail\\.com|icloud\\.com|aol\\.com|protonmail\\.com|zoho\\.com|mail\\.com|gmx\\.com|yandex\\.com)"

    private val namePattern = "^[a-zA-Z]+$"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDetailsViewModel = ViewModelProvider(this)[UserDetailsViewModel::class.java]
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        HideStatusBarUtil.hideStatusBar(this@SignUpActivity)
        auth = FirebaseAuth.getInstance()

        focusChangeListeners()
        initListener()
        observersForApiCalls()

    }

    private fun initListener() {

        binding.txtsignup.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.btnSignUp.setOnClickListener {
            email = binding.emailEt.text.toString()
            password = binding.passET.text.toString()
            val confirmPassword = binding.confirmPassEt.text.toString()
            val name = binding.nameET.text.toString()

            binding.passwordLayout.isPasswordVisibilityToggleEnabled = true
            binding.confirmPasswordLayout.isPasswordVisibilityToggleEnabled = true

            if (email.trim().isEmpty() or password.trim().isEmpty() || confirmPassword.trim()
                    .isEmpty() || name.trim().isEmpty()

//              ||  phoneno.trim().isEmpty()
            ) {


                if (email.trim().isEmpty()) binding.emailEt.error = "Empty Field"
                if (name.trim().isEmpty()) binding.nameET.error = "Empty Field"
//                if (phoneno.trim().isEmpty()) binding.phoneEt.error = "Empty Field"
                if (password.trim().isEmpty()) {
                    binding.passwordLayout.isPasswordVisibilityToggleEnabled = true
                    binding.passET.error = "Empty Field"
                }
                if (confirmPassword.isEmpty()) {
                    binding.confirmPasswordLayout.isPasswordVisibilityToggleEnabled = true
                    binding.confirmPassEt.error = "Empty Field"
                }
                Toast.makeText(this, "Enter Valid Details", Toast.LENGTH_SHORT).show()

            }
//            else if (!phoneno.all {
//                    it.isDigit()
//                }) {
//                binding.phoneEt.error = "Enter Valid Phone No (Only Digits)"
//                binding.progressBarSignUp.visibility = View.GONE
//
//            }
            else if (!email.matches(emailPattern.toRegex())) {
                binding.emailEt.error = "Enter Valid Email"

            } else if (name.length < 2) {
                binding.nameET.error = "Enter Minimum 2 Characters"

            } else if (name.length > 30) {
                binding.nameET.error = "Please Enter Less Than 30 Characters"
            } else if (!name.matches(namePattern.toRegex())) {
                binding.nameET.error = "Enter Valid Name (Only Alphabets No Space)"
            }
//            else if (phoneno.length != 10) {
//                binding.phoneEt.error = "Enter Valid Phone No"
//                binding.progressBarSignUp.visibility = View.GONE
//
//
//            }
            else if (password.length < 6) {
                binding.passET.error = "Enter Password Of More Than 6 Characters"
                binding.passwordLayout.isPasswordVisibilityToggleEnabled = true

            } else if (password != confirmPassword) {
                binding.confirmPassEt.error = "Password is Not Matching"
                binding.passwordLayout.isPasswordVisibilityToggleEnabled = true
                binding.confirmPasswordLayout.isPasswordVisibilityToggleEnabled = true
            } else {

                HideKeyboard.hideKeyboard(this, binding.emailEt.windowToken)
                HideKeyboard.hideKeyboard(this, binding.passET.windowToken)
                HideKeyboard.hideKeyboard(this, binding.confirmPassEt.windowToken)
                HideKeyboard.hideKeyboard(this, binding.nameET.windowToken)
                binding.progressBar.visibility = View.VISIBLE

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        callPostUserDetailsApi(
                            UserDetailsPostRequestBody(
                                auth.currentUser!!.uid, name, email, ""
                            )
                        )
                    } else {
                        binding.progressBar.visibility = View.GONE

                        Toast.makeText(
                            this, "Failed ${it.exception?.message}", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun callPostUserDetailsApi(userDetailsPostRequestBody: UserDetailsPostRequestBody) {
        userDetailsViewModel.callPostUserDetails(userDetailsPostRequestBody)
    }

    private fun observersForApiCalls() {

        userDetailsViewModel.errorMessage.observe(this, Observer {
            showErrorBottomDialog(
                it
            )
        })
        userDetailsViewModel.showProgress.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.nestedScrollView.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.nestedScrollView.visibility = View.VISIBLE
            }
        })

        userDetailsViewModel.userDetailsResponse.observe(this, Observer {
            userDetailsViewModel.setUserDetailsResponse(it)
            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
            intent.putExtra("Email", email)
            intent.putExtra("Password", password)
            intent.putExtra("isIntent", true)
            startActivity(intent)
            ToastUtil.makeToast(
                this@SignUpActivity, "Registered Successfully: Please SignIn To Continue"
            )
            finish()
        })
    }

    private fun showErrorBottomDialog(message: String) {
        val bottomDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val dialogBinding = ErrorBottomDialogLayoutBinding.inflate(layoutInflater)
        bottomDialog.setContentView(dialogBinding.root)
        bottomDialog.setCancelable(false)
        bottomDialog.show()
        dialogBinding.messageTv.text = message
        dialogBinding.continueButton.setOnClickListener {
            bottomDialog.dismiss()
            finish()
        }
    }


    private fun focusChangeListeners() {

        binding.nameET.setOnFocusChangeListener { view, b ->
            if (!b) {
                if (binding.nameET.text.toString().trim()
                        .isNotEmpty() && binding.nameET.text.toString().length < 2
                ) {
                    binding.nameET.error = "Please Enter Minimum 2 Characters"
                } else if (binding.nameET.text.toString().trim()
                        .isNotEmpty() && binding.nameET.text.toString().length > 30
                ) {
                    binding.nameET.error = "Please Enter Less Than 30 Characters"

                } else if (binding.nameET.text.toString().trim()
                        .isNotEmpty() && !binding.nameET.text!!.matches(namePattern.toRegex())
                ) {
                    binding.nameET.error = "Enter Valid Name (Only Alphabets No Space)"
                }
            }
        }
//
//        binding.phoneEt.setOnFocusChangeListener { view, b ->
//            if (!b) {
//                if (binding.phoneEt.text.toString().trim()
//                        .isNotEmpty() && binding.phoneEt.text.toString().length < 10
//                ) {
//                    binding.phoneEt.error = "Enter Valid Phone No"
//                }
//            }
//        }
        binding.emailEt.setOnFocusChangeListener { view, b ->
            if (!b) {
                if (binding.emailEt.text.toString().trim()
                        .isNotEmpty() && !binding.emailEt.text.toString()
                        .matches(emailPattern.toRegex())
                ) {
                    binding.emailEt.error = "Enter Valid Email"
                }
            }
        }
    }


    private fun clearData() {
        val sharedPreferences = getSharedPreferences("sharedpreference", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove("shared")
        editor.remove("Finished")
        editor.apply()
    }
}