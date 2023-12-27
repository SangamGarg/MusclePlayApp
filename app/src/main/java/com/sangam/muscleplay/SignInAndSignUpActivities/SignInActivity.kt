package com.sangam.muscleplay.SignInAndSignUpActivities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil
import com.sangam.muscleplay.MainActivity
import com.sangam.muscleplay.R
import com.sangam.muscleplay.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
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
                    firebaseAuth.signInWithCredential(credentials).addOnCompleteListener {
                        if (it.isSuccessful) {

                            val userId = firebaseAuth.currentUser!!.uid
                            val user = hashMapOf(
                                "name" to name, "email" to email, "phone" to "", "UserId" to userId
                            )

                            database.collection("users").document(userId).set(user)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {

                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        Toast.makeText(
                                            this, "SignIn Successful", Toast.LENGTH_SHORT
                                        ).show()
                                        onBoardingFinished()
                                        finish()

                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Error ${it.exception?.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }
                                }


                        } else {
                            Toast.makeText(
                                this, "Error ${it.exception?.message}", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this@SignInActivity, "Failed", Toast.LENGTH_SHORT).show()
            }
        }

    lateinit var binding: ActivitySignInBinding
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    private var checkLogoutIntent: Boolean? = null
    private lateinit var googleSignInClient: GoogleSignInClient

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
        binding.TextForgotPass.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.forgot_password_dialog, null)
            builder.setView(view)
            builder.create().show()
            builder.setCancelable(true)
            val UserEmail = view.findViewById<EditText>(R.id.ETResetEmail)
            val button = view.findViewById<Button>(R.id.btnForgotPass)
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
                            builder.create().dismiss()
                        } else {
                            Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT)
                                .show()
                            builder.create().dismiss()

                        }
                    }
                }
            }
        }
        binding.txtsignin.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignIn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passET.text.toString()
            // binding.progreessbarSignIn.visibility = View.VISIBLE

            if (email.trim().isEmpty() || password.trim().isEmpty()) {
                if (email.trim().isEmpty()) binding.emailEt.error = "Empty Field"
                if (password.trim().isEmpty()) binding.passET.error = "Empty Field"
                // binding.progreessbarSignIn.visibility = View.GONE
                binding.passwordLayout.isPasswordVisibilityToggleEnabled = false

            } else if (!email.matches(emailpattern.toRegex()) || password.length < 6) {
                if (!email.matches(emailpattern.toRegex())) binding.emailEt.error =
                    "Enter Valid Email"
                if (password.length < 6) binding.passET.error =
                    "Enter Password More than 6 characters"
                //     binding.progreessbarSignIn.visibility = View.GONE
                binding.passwordLayout.isPasswordVisibilityToggleEnabled = false

            } else {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "SignIn Successful", Toast.LENGTH_SHORT).show()
                        onBoardingFinished()
                        finish()
                    } else {
                        Toast.makeText(this, "Error ${it.exception?.message}", Toast.LENGTH_SHORT)
                            .show()
                        //       binding.progreessbarSignIn.visibility = View.GONE
                    }
                }
            }
        }
        binding.btnGoogleSignIn.setOnClickListener {
            val signInClient = googleSignInClient.signInIntent
            launcher.launch(signInClient)
        }
    }

    private fun onBoardingFinished() {
        val sharedPref = getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (checkLogoutIntent == true) {
            finishAffinity()
        }
    }


}