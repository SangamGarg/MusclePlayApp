package com.sangam.muscleplay.botton_nav.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sangam.muscleplay.AppUtils.IntentUtil
import com.sangam.muscleplay.UserDataUtils.UserViewModel
import com.sangam.muscleplay.EditProfileActivity
import com.sangam.muscleplay.R
import com.sangam.muscleplay.SignInAndSignUpActivities.SignInActivity
import com.sangam.muscleplay.databinding.FragmentNotificationsBinding
import com.sangam.muscleplay.databinding.PasswordUpdateDialogBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val database by lazy {
        Firebase.firestore
    }
    private lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        observeUserData()
        initListener()

        return root
    }

    private fun initListener() {

        binding.tvChangePassword.setOnClickListener {
            changePassword()
        }
        binding.tvDeleteAccount.setOnClickListener {
            deleteAlertDialog()
        }


        binding.tvEditProfile.setOnClickListener {
            IntentUtil.startIntent(requireContext(), EditProfileActivity())
        }
        binding.tvLogout.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("339910902548-8pjamj4qhoc5ogfrpptd42uqsnkvt1b4.apps.googleusercontent.com")
                .requestEmail().build()
            GoogleSignIn.getClient(requireContext(), gso).signOut()
            firebaseAuth.signOut()
            Toast.makeText(requireContext(), "Logged Out Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), SignInActivity::class.java)
            intent.putExtra("FromLogout", true)
            startActivity(intent)
        }
    }

    private fun deleteAlertDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setCancelable(false)
        alertDialog.setTitle("Delete Account")
        alertDialog.setMessage("Are You Sure?")


        alertDialog.setPositiveButton("yes") { _, _ ->
            deleteAccount()
        }
        alertDialog.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.create().show()

    }

    //This is the function to delete the user account
    private fun deleteAccount() {
        val user = firebaseAuth.currentUser
        user?.delete()?.addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    requireContext(), "Account Deleted Successfully", Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(requireContext(), SignInActivity::class.java)
                startActivity(intent)
            } else {
                Log.d("DeleteAccount", it.exception.toString())
                Toast.makeText(
                    requireContext(),
                    "Error: ${it.exception?.message.toString()} ",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    private fun changePassword() {
        val alertDialog = AlertDialog.Builder(requireContext())
        val binding = PasswordUpdateDialogBinding.inflate(layoutInflater)
        alertDialog.setView(binding.root)
        alertDialog.setCancelable(true)
        alertDialog.create().show()
        binding.btnPassUpdate.setOnClickListener {
            val userPass = binding.ETUpdatePassword.text.toString()
            val confirmpassword = binding.ETConfirmUpdatePassword.text.toString()
            binding.passwordLayout.isPasswordVisibilityToggleEnabled = true
            binding.confirmPasswordLayout.isPasswordVisibilityToggleEnabled = true
            if (userPass.trim().isEmpty() || confirmpassword.trim().isEmpty()) {
                if (userPass.isEmpty()) {
                    binding.passwordLayout.isPasswordVisibilityToggleEnabled = true
                    binding.ETUpdatePassword.error = "Empty Field"
                }
                if (confirmpassword.isEmpty()) {
                    binding.confirmPasswordLayout.isPasswordVisibilityToggleEnabled = true
                    binding.ETConfirmUpdatePassword.error = "Empty Field"
                }
            } else if (userPass.length < 6) {
                binding.ETUpdatePassword.error = "Enter Password Of More Than 6 Characters"

                binding.passwordLayout.isPasswordVisibilityToggleEnabled = true

            } else if (userPass != confirmpassword) {
                binding.ETConfirmUpdatePassword.error = "Password is Not Matching"

                binding.passwordLayout.isPasswordVisibilityToggleEnabled = true
                binding.confirmPasswordLayout.isPasswordVisibilityToggleEnabled = true
            } else {
                val user = firebaseAuth.currentUser
                user?.updatePassword(userPass)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            requireContext(), "Password Updated Successfully", Toast.LENGTH_SHORT
                        ).show()
                        alertDialog.create().dismiss()
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken("339910902548-8pjamj4qhoc5ogfrpptd42uqsnkvt1b4.apps.googleusercontent.com")
                            .requestEmail().build()
                        GoogleSignIn.getClient(requireContext(), gso).signOut()
                        firebaseAuth.signOut()
                        Toast.makeText(
                            requireContext(), "Please Login Again", Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(requireContext(), SignInActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${it.exception?.message.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        alertDialog.create().dismiss()

                    }
                }
            }
        }
    }


    fun observeUserData() {
        userViewModel.userDataResponse.observe(viewLifecycleOwner, Observer {
            binding.tvName.text = it?.name
            binding.tvEmail.text = it?.email

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}