package com.sangam.muscleplay.SignInAndSignUpActivities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil
import com.sangam.muscleplay.SignInAndSignUpActivities.model.UserDataClass
import com.sangam.muscleplay.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private val database by lazy {
        Firebase.firestore
    }
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }
    private val emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        HideStatusBarUtil.hideStatusBar(this@SignUpActivity)

        binding.txtsignup.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignUp.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passET.text.toString()
            val confirmpassword = binding.confirmPassEt.text.toString()
            val name = binding.nameET.text.toString()
            val phoneno = binding.phoneEt.text.toString()

//            binding.progreessbarSignUp.visibility = View.VISIBLE
            binding.passwordLayout.isPasswordVisibilityToggleEnabled = true
            binding.confirmPasswordLayout.isPasswordVisibilityToggleEnabled = true


            if (email.trim().isEmpty() or password.trim().isEmpty() || confirmpassword.trim()
                    .isEmpty() || name.trim().isEmpty() || phoneno.trim().isEmpty()
            ) {
                if (email.isEmpty()) binding.emailEt.error = "Empty Field"
                if (name.isEmpty()) binding.nameET.error = "Empty Field"
                if (phoneno.isEmpty()) binding.phoneEt.error = "Empty Field"
                if (password.isEmpty()) {
                    binding.passwordLayout.isPasswordVisibilityToggleEnabled = false
                    binding.passET.error = "Empty Field"
                }
                if (confirmpassword.isEmpty()) {
                    binding.confirmPasswordLayout.isPasswordVisibilityToggleEnabled = false
                    binding.confirmPassEt.error = "Empty Field"
                }
                Toast.makeText(this, "Enter Valid Details", Toast.LENGTH_SHORT).show()
//                binding.progreessbarSignUp.visibility = View.GONE

            } else if (!email.matches(emailpattern.toRegex())) {
                binding.emailEt.error = "Enter Valid Email"
//                binding.progreessbarSignUp.visibility = View.GONE

            } else if (phoneno.length != 10) {
                binding.phoneEt.error = "Enter Valid Phone No"
//                binding.progreessbarSignUp.visibility = View.GONE


            } else if (password.length < 6) {
                binding.passET.error = "Enter Password Of More Than 6 Characters"
//                binding.progreessbarSignUp.visibility = View.GONE
                binding.passwordLayout.isPasswordVisibilityToggleEnabled = false

            } else if (password != confirmpassword) {
                binding.confirmPassEt.error = "Password is Not Matching"
//                binding.progreessbarSignUp.visibility = View.GONE
                binding.passwordLayout.isPasswordVisibilityToggleEnabled = false
                binding.confirmPasswordLayout.isPasswordVisibilityToggleEnabled = false
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val userId = auth.currentUser!!.uid
                        val user = hashMapOf(
                            "name" to name, "email" to email, "phone" to phoneno, "UserId" to userId
                        )

                        database.collection("users").document(userId).set(user)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val intent = Intent(this, SignInActivity::class.java)
                                    startActivity(intent)
                                    Toast.makeText(
                                        this, "Registered Successfully", Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this, "Error ${it.exception?.message}", Toast.LENGTH_SHORT
                                    ).show()
//                                binding.progreessbarSignUp.visibility = View.GONE

                                }
                            }
                    } else {
                        Toast.makeText(
                            this, "Failed ${it.exception?.message}", Toast.LENGTH_SHORT
                        ).show()
//                        binding.progreessbarSignUp.visibility = View.GONE

                    }
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