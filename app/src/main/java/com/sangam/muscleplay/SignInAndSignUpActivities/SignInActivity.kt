package com.sangam.muscleplay.SignInAndSignUpActivities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil
import com.sangam.muscleplay.UserExtraDetailsScreen.UserDetailsActivity
import com.sangam.muscleplay.R
import com.sangam.muscleplay.UserDataUtils.UserViewModel
import com.sangam.muscleplay.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private val userViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
    }
    private var dataFilled: Boolean? = null
    private val database by lazy {
        Firebase.firestore
    }
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val name = account?.displayName
                    val email = account?.email
                    val credentials = GoogleAuthProvider.getCredential(account?.idToken, null)
                    binding.progressBarSignIn.visibility = View.VISIBLE

                    firebaseAuth.signInWithCredential(credentials).addOnCompleteListener {
                        if (it.isSuccessful) {
                            binding.progressBarSignIn.visibility = View.GONE

                            val userId = firebaseAuth.currentUser!!.uid
                            // Check if the document exists
                            database.collection("users").document(userId).get()
                                .addOnCompleteListener { documentTask ->
                                    if (documentTask.isSuccessful) {
                                        val document = documentTask.result
                                        if (document != null && document.exists()) {
                                            intentToNext()
                                        } else {
                                            // Document does not exist, create a new one
                                            val user = hashMapOf(
                                                "name" to name,
                                                "email" to email,
                                                "phone" to "",
                                                "UserId" to userId
                                            )
                                            database.collection("users").document(userId).set(user)
                                                .addOnCompleteListener { createTask ->
                                                    if (createTask.isSuccessful) {
                                                        intentToNext()
                                                    } else {
                                                        Toast.makeText(
                                                            this,
                                                            "Error ${createTask.exception?.message}",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                        }
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Error ${documentTask.exception?.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {
                            Toast.makeText(
                                this, "Error ${it.exception?.message}", Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBarSignIn.visibility = View.GONE
                        }
                    }
                }
            } else {
                binding.progressBarSignIn.visibility = View.GONE
            }
        }

    lateinit var binding: ActivitySignInBinding
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
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
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        HideStatusBarUtil.hideStatusBar(this@SignInActivity)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        checkLogoutIntent = intent.getBooleanExtra("FromLogout", false)
        email = intent.getStringExtra("Email")
        password = intent.getStringExtra("Password")
        isIntent = intent.getBooleanExtra("isIntent", false)
//        callGetUserExtraData()
//        observeUserExtraData()
        focusChangeListeners()
        binding.TextForgotPass.setOnClickListener {
            val builder = Dialog(this)
            val view = layoutInflater.inflate(R.layout.forgot_password_dialog, null)
            builder.setContentView(view)
            builder.window?.setBackgroundDrawableResource(android.R.color.transparent)
            builder.show()
            builder.setCancelable(true)
            val UserEmail = view.findViewById<EditText>(R.id.ETResetEmail)
            val button = view.findViewById<TextView>(R.id.btnForgotPass)
            button.setOnClickListener {
                val email = UserEmail.text.toString()
                if (email.trim().isEmpty()) {
                    UserEmail.error = "Empty Field"
                } else if (!email.matches(emailpattern.toRegex())) {
                    UserEmail.error = "Enter Valid Email"
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
            binding.progressBarSignIn.visibility = View.VISIBLE

            if (email.trim().isEmpty() || password.trim().isEmpty()) {
                if (email.trim().isEmpty()) binding.emailEt.error = "Empty Field"
                if (password.trim().isEmpty()) binding.passET.error = "Empty Field"
                binding.progressBarSignIn.visibility = View.GONE
                binding.passwordLayout.isPasswordVisibilityToggleEnabled = true

            } else if (!email.matches(emailpattern.toRegex()) || password.length < 6) {
                if (!email.matches(emailpattern.toRegex())) binding.emailEt.error =
                    "Enter Valid Email"
                if (password.length < 6) binding.passET.error =
                    "Enter Password More than 6 characters"
                binding.progressBarSignIn.visibility = View.GONE
                binding.passwordLayout.isPasswordVisibilityToggleEnabled = true

            } else {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        intentToNext()
                    } else {
                        Toast.makeText(this, "Error ${it.exception?.message}", Toast.LENGTH_SHORT)
                            .show()
                        binding.progressBarSignIn.visibility = View.GONE
                    }
                }
            }
        }
        binding.btnGoogleSignIn.setOnClickListener {
            val signInClient = googleSignInClient.signInIntent
            launcher.launch(signInClient)
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
        val intent = Intent(this, UserDetailsActivity::class.java)
        startActivity(intent)
        Toast.makeText(this, "SignIn Successful", Toast.LENGTH_SHORT).show()
        onBoardingDone()
        finish()
    }

    private fun onBoardingDone() {
        val sharePref = getSharedPreferences("OnBoardScreenShow", Context.MODE_PRIVATE)
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