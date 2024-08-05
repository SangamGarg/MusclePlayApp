package com.sangam.muscleplay.botton_nav.more

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.sangam.muscleplay.appUtils.IntentUtil
import com.sangam.muscleplay.appUtils.ToastUtil
import com.sangam.muscleplay.R
import com.sangam.muscleplay.userRegistration.ui.SignInActivity
import com.sangam.muscleplay.botton_nav.more.MyProgress.MyProgressActivity
import com.sangam.muscleplay.databinding.FragmentNotificationsBinding
import com.sangam.muscleplay.databinding.PasswordUpdateDialogBinding
import com.sangam.muscleplay.startActivtyCalculateBurnedCalories.StartActivityBurnedCaloriesActivity
import com.sangam.muscleplay.userRegistration.model.UserDetailsResponseModel
import com.sangam.muscleplay.userRegistration.viewModel.UserDetailData

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private lateinit var userData: UserDetailsResponseModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(layoutInflater)
        initListener()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getUserData()
    }

    private fun initListener() {

        binding.includeAge.apply {
            ivImage.setBackgroundResource(R.drawable.age)
            tvName.text = "Age"
        }
        binding.includeAvailableCoins.apply {
            ivImage.setBackgroundResource(R.drawable.coin)
            tvName.text = "Coins"
            tvValue.text = "0"
        }
        binding.includeHeight.apply {
            ivImage.setBackgroundResource(R.drawable.height)
            tvName.text = "Height"
        }
        binding.includeWeight.apply {
            ivImage.setBackgroundResource(R.drawable.weight)
            tvName.text = "Weight"
        }

        binding.tvChangePassword.setOnClickListener {
            changePassword()
        }
        binding.tvDeleteAccount.setOnClickListener {
            deleteAlertDialog()
        }
        binding.tvWithdraw.setOnClickListener {
            ToastUtil.makeToast(requireContext(), "Soon....")
        }

        binding.ivEditProfileCircle.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            intent.putExtra("UserData", userData)
            startActivity(intent)
        }
        binding.tvMyProgress.setOnClickListener {
            IntentUtil.startIntent(requireContext(), MyProgressActivity())
        }
        binding.tvStartActivity.setOnClickListener {
            IntentUtil.startIntent(requireContext(), StartActivityBurnedCaloriesActivity())
        }
        binding.tvLogout.setOnClickListener {
            logoutAlertDialog()
        }
    }


    private fun getUserData() {
        userData = UserDetailData.userDetailsResponseModel!!

        binding.tvName.text = userData.name
        binding.tvEmail.text = userData.email
        binding.includeHeight.tvValue.text = "${userData.measurements?.height} cm"
        binding.includeAge.tvValue.text = userData.age.toString()
        binding.includeWeight.tvValue.text = "${userData.measurements?.weight} kg"


        if (userData.profileImageUrl != null) {
            Glide.with(requireContext()).load(userData.profileImageUrl)
                .placeholder(R.drawable.profile).into(binding.ivProfile)
            Glide.with(requireContext()).load(userData.profileImageUrl)
                .placeholder(R.drawable.profile).into(binding.ivProfileBig)


            binding.ivProfile.setOnClickListener {
                binding.ivProfileZoom.visibility = View.VISIBLE
            }
            binding.ivProfileZoom.setOnClickListener {
                binding.ivProfileZoom.visibility = View.GONE
            }
        }
    }

    private fun logoutAlertDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setCancelable(true)
        alertDialog.setTitle("Logout")
        alertDialog.setMessage("Are You Sure?")


        alertDialog.setPositiveButton("yes") { _, _ ->
            logoutFunction()
        }
        alertDialog.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.create().show()
    }

    private fun logoutFunction() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("339910902548-8pjamj4qhoc5ogfrpptd42uqsnkvt1b4.apps.googleusercontent.com")
            .requestEmail().build()
        GoogleSignIn.getClient(requireContext(), gso).signOut()
        auth.signOut()
        Toast.makeText(requireContext(), "Logged Out Successful", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), SignInActivity::class.java)
        intent.putExtra("FromLogout", true)
        startActivity(intent)
    }


    private fun deleteAlertDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setCancelable(true)
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
        val user = auth.currentUser
        user?.delete()?.addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    requireContext(), "Account Deleted Successfully", Toast.LENGTH_SHORT
                ).show()
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("339910902548-8pjamj4qhoc5ogfrpptd42uqsnkvt1b4.apps.googleusercontent.com")
                    .requestEmail().build()
                GoogleSignIn.getClient(requireContext(), gso).signOut()
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
        val bottomDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val binding = PasswordUpdateDialogBinding.inflate(layoutInflater)
        bottomDialog.setContentView(binding.root)
        bottomDialog.setCancelable(true)
        bottomDialog.show()
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
                val user = auth.currentUser
                user?.updatePassword(userPass)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            requireContext(), "Password Updated Successfully", Toast.LENGTH_SHORT
                        ).show()
                        bottomDialog.show()
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken("339910902548-8pjamj4qhoc5ogfrpptd42uqsnkvt1b4.apps.googleusercontent.com")
                            .requestEmail().build()
                        GoogleSignIn.getClient(requireContext(), gso).signOut()
                        auth.signOut()
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
                        bottomDialog.dismiss()

                    }
                }
            }
        }
    }
}