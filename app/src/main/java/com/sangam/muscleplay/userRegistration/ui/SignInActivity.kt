package com.sangam.muscleplay.userRegistration.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.sangam.muscleplay.AppUtils.HideKeyboard
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil
import com.sangam.muscleplay.AppUtils.IntentUtil
import com.sangam.muscleplay.MainActivity
import com.sangam.muscleplay.UserExtraDetailsFill.UserDetailsActivity
import com.sangam.muscleplay.R
import com.sangam.muscleplay.databinding.ActivitySignInBinding
import com.sangam.muscleplay.databinding.ErrorBottomDialogLayoutBinding
import com.sangam.muscleplay.userRegistration.model.UserDetailsPostRequestBody
import com.sangam.muscleplay.userRegistration.viewModel.UserDetailsViewModel
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var userDetailsViewModel: UserDetailsViewModel


    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val name = account?.displayName
                    val email = account?.email
                    val credentials = GoogleAuthProvider.getCredential(account?.idToken, null)
                    binding.progressBar.visibility = View.VISIBLE

                    firebaseAuth.signInWithCredential(credentials).addOnCompleteListener {
                        if (it.isSuccessful) {
                            binding.progressBar.visibility = View.GONE
                            callPostUserDetailsApi(
                                UserDetailsPostRequestBody(
                                    firebaseAuth.currentUser!!.uid, name, email, ""

                                )
                            )
                        } else {
                            Toast.makeText(
                                this, "Error ${it.exception?.message}", Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

    private lateinit var firebaseAuth: FirebaseAuth
    private val emailpattern =
        "[a-zA-Z0-9._%+-]+@(gmail\\.com|yahoo\\.com|outlook\\.com|hotmail\\.com|icloud\\.com|aol\\.com|protonmail\\.com|zoho\\.com|mail\\.com|gmx\\.com|yandex\\.com)"


    private var checkLogoutIntent: Boolean? = null
    private lateinit var googleSignInClient: GoogleSignInClient
    private var email: String? = null
    private var password: String? = null
    private var isIntent: Boolean? = false

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        userDetailsViewModel = ViewModelProvider(this)[UserDetailsViewModel::class.java]
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        HideStatusBarUtil.hideStatusBar(this@SignInActivity)
        firebaseAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        checkLogoutIntent = intent.getBooleanExtra("FromLogout", false)
        email = intent.getStringExtra("Email")
        password = intent.getStringExtra("Password")
        isIntent = intent.getBooleanExtra("isIntent", false)
//        callGetUserExtraData()
//        observeUserExtraData()
        observersForApiCalls()
        focusChangeListeners()

        initListener()


    }

    private fun initListener() {


        binding.TextForgotPass.setOnClickListener {
            val builder = Dialog(this)
            val view = layoutInflater.inflate(R.layout.forgot_password_dialog, null)
            builder.setContentView(view)
            builder.window?.setBackgroundDrawableResource(android.R.color.transparent)
            builder.show()
            builder.setCancelable(true)
            val userEmail = view.findViewById<EditText>(R.id.ETResetEmail)
            val button = view.findViewById<TextView>(R.id.btnForgotPass)
            button.setOnClickListener {
                val email = userEmail.text.toString()
                if (email.trim().isEmpty()) {
                    userEmail.error = "Empty Field"
                } else if (!email.matches(emailpattern.toRegex())) {
                    userEmail.error = "Enter Valid Email"
                } else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Email Sent", Toast.LENGTH_SHORT).show()
                            builder.dismiss()
                        } else {
                            Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT)
                                .show()
                            builder.dismiss()

                        }
                    }
                }
            }
        }
        binding.txtsignin.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
        if (isIntent == true) {
            binding.emailEt.setText(email)
            binding.passET.setText(password)
        }
        binding.btnSignIn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passET.text.toString()
            binding.progressBar.visibility = View.VISIBLE

            if (email.trim().isEmpty() || password.trim().isEmpty()) {
                if (email.trim().isEmpty()) binding.emailEt.error = "Empty Field"
                if (password.trim().isEmpty()) binding.passET.error = "Empty Field"
                binding.progressBar.visibility = View.GONE
                binding.passwordLayout.isPasswordVisibilityToggleEnabled = true

            } else if (!email.matches(emailpattern.toRegex()) || password.length < 6) {
                if (!email.matches(emailpattern.toRegex())) binding.emailEt.error =
                    "Enter Valid Email"
                if (password.length < 6) binding.passET.error =
                    "Enter Password More than 6 characters"
                binding.progressBar.visibility = View.GONE
                binding.passwordLayout.isPasswordVisibilityToggleEnabled = true

            } else {
                HideKeyboard.hideKeyboard(this, binding.emailEt.windowToken)
                HideKeyboard.hideKeyboard(this, binding.passET.windowToken)
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        callGetUserDetailsApi(firebaseAuth.currentUser!!.uid)
                    } else {
                        Toast.makeText(this, "Error ${it.exception?.message}", Toast.LENGTH_SHORT)
                            .show()
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
        binding.btnGoogleSignIn.setOnClickListener {
            val signInClient = googleSignInClient.signInIntent
            launcher.launch(signInClient)
        }
    }

    private fun callPostUserDetailsApi(userDetailsPostRequestBody: UserDetailsPostRequestBody) {
        userDetailsViewModel.callPostUserDetails(userDetailsPostRequestBody)
    }

    private fun callGetUserDetailsApi(userId: String?) {
        userDetailsViewModel.callGetUserDetails(userId)
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

            if (it.gender.isNullOrEmpty()) {
                IntentUtil.startIntent(this@SignInActivity, UserDetailsActivity())
                Toast.makeText(this, "SignIn Successful", Toast.LENGTH_SHORT).show()
                onBoardingDone()
                finish()
            } else {
                intentToNext()
            }
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

        binding.emailEt.setOnFocusChangeListener { view, b ->
            if (!b) {
                if (!binding.emailEt.text.toString().trim()
                        .isEmpty() && !binding.emailEt.text.toString()
                        .matches(emailpattern.toRegex())
                ) {
                    binding.emailEt.error = "Enter Valid Email"
                }
            }
        }
    }

    private fun intentToNext() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        Toast.makeText(this, "SignIn Successful", Toast.LENGTH_SHORT).show()
        onBoardingDone()
        finish()
    }

    private fun onBoardingDone() {
        val sharePref = getSharedPreferences("OnBoardShow", Context.MODE_PRIVATE)
        sharePref.edit().apply {
            putBoolean("Done", true)
            apply()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (checkLogoutIntent == true) {
            finishAffinity()
        }
    }

}