package com.sangam.muscleplay.botton_nav.profile

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.sangam.muscleplay.AppUtils.IntentUtil
import com.sangam.muscleplay.AppUtils.ToastUtil
import com.sangam.muscleplay.UserDataUtils.UserViewModel
import com.sangam.muscleplay.R
import com.sangam.muscleplay.SignInAndSignUpActivities.SignInActivity
import com.sangam.muscleplay.botton_nav.profile.MyProgress.MyProgressActivity
import com.sangam.muscleplay.botton_nav.profile.MyReport.ui.MyReportActivity
import com.sangam.muscleplay.databinding.FragmentNotificationsBinding
import com.sangam.muscleplay.databinding.PasswordUpdateDialogBinding
import com.sangam.muscleplay.startActivtyCalculateBurnedCalories.StartActivityBurnedCaloriesActivity
import java.io.File

class ProfileFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private var uri: Uri? = null
    lateinit var imageUri: Uri
    private var storageRef = Firebase.storage
    lateinit var databaseReference: DatabaseReference
    var userName: String? = null

    private val contract =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                binding.tvDone.visibility = View.VISIBLE
                binding.ivProfile.setImageURI(imageUri)
                uri = imageUri

            } else {
                binding.tvDone.visibility = View.VISIBLE
                binding.ivProfile.setImageURI(uri)
                uri = null
            }
        }

    private val requestForPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                contract.launch(imageUri)
            } else {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                    showRationaleDialog()
                } else {
                    val message =
                        "You've denied camera permission twice. To enable it, open app settings."
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                }
            }
        }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val database by lazy {
        Firebase.firestore
    }
    lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        storageRef = FirebaseStorage.getInstance()
        val notificationsViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.tvDone.visibility = View.VISIBLE
                binding.ivProfile.setImageURI(uri)
                this.uri = uri
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
        imageUri = createImageUri()!!
        val root: View = binding.root
        observeUserData()
        observeUserExtraData()
        initListener()

        return root
    }

    private fun showRationaleDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Camera Permission")
            .setMessage("This app requires camera permission to take profile photos. If you deny this time you have to manually go to app setting to allow permission.")
            .setPositiveButton("Ok") { _, _ ->
                requestForPermission.launch(android.Manifest.permission.CAMERA)
            }
        builder.create().show()
    }

    private fun checkPermission(): Boolean {
        val permission = android.Manifest.permission.CAMERA
        return ContextCompat.checkSelfPermission(
            requireContext(), permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun createImageUri(): Uri? {
        val image = File(requireContext().filesDir, "profile_photos.png")
        return FileProvider.getUriForFile(
            requireContext(), "com.sangam.muscleplay.fileProvider", image
        )
    }

    private fun initListener() {

        binding.tvDone.setOnClickListener {
            uploadImage()
        }
        binding.ivProfileEdit.setOnClickListener {
            chooseImage()
        }

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
            IntentUtil.startIntent(requireContext(), EditProfileActivity())
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

    private fun uploadImage() {
        if (uri == null) {
            Toast.makeText(requireContext(), "Please choose an image first", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Uploading")
        progressDialog.show()

        val imageRef = storageRef.reference.child("images")
            .child("${System.currentTimeMillis()} ${firebaseAuth.currentUser!!.uid}")
        imageRef.putFile(uri!!).addOnSuccessListener { task ->
            task.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                val mapImage = mapOf(
                    "profileImageUrl" to uri.toString()
                )
                val userId = firebaseAuth.currentUser?.uid ?: ""

                database.collection("users").document(userId).update(mapImage)
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(), "Profile uploaded successfully", Toast.LENGTH_SHORT
                        ).show()
                        userViewModel.getUserData()
                        binding.tvDone.visibility = View.GONE
                        progressDialog.dismiss()

                    }.addOnFailureListener {
                        Toast.makeText(
                            requireContext(), "Failed to upload profile", Toast.LENGTH_SHORT
                        ).show()
                        binding.tvDone.visibility = View.GONE
                        progressDialog.dismiss()

                    }
            }?.addOnFailureListener {
                Toast.makeText(
                    requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT
                ).show()
                binding.tvDone.visibility = View.GONE
                progressDialog.dismiss()

            }
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT
            ).show()
            binding.tvDone.visibility = View.GONE
            progressDialog.dismiss()
        }
    }


    private fun chooseImage() {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.dialog_choose_profile_image, null)
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
        val gallery = view.findViewById<TextView>(R.id.chooseGallery)
        gallery.setOnClickListener {
            dialog.dismiss()
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }
        val camera = view.findViewById<TextView>(R.id.chooseCamera)
        camera.setOnClickListener {
            dialog.dismiss()
            if (checkPermission()) {
                contract.launch(imageUri)
            } else {
                requestForPermission.launch(android.Manifest.permission.CAMERA)
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
        firebaseAuth.signOut()
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
        val user = firebaseAuth.currentUser
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
                val user = firebaseAuth.currentUser
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
                        bottomDialog.dismiss()

                    }
                }
            }
        }
    }


    fun observeUserData() {
        userViewModel.userDataResponse.observe(viewLifecycleOwner, Observer {
            binding.tvName.text = it?.name
            binding.tvEmail.text = it?.email
            userName = it?.name
            if (it?.profileImageUrl != null) {
                Glide.with(requireContext()).load(it.profileImageUrl)
                    .placeholder(R.drawable.baseline_person_24).into(binding.ivProfile)
            }
        })
    }

    fun observeUserExtraData() {
        userViewModel.userDataExtraResponse.observe(viewLifecycleOwner, Observer {
            binding.includeHeight.tvValue.text = "${it?.height} cm"
            binding.includeAge.tvValue.text = it?.age
            binding.includeWeight.tvValue.text = "${it?.weight} kg"
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}