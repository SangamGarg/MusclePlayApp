package com.sangam.muscleplay.botton_nav.more

import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.sangam.muscleplay.appUtils.HideStatusBarUtil
import com.sangam.muscleplay.appUtils.ToastUtil
import com.sangam.muscleplay.R
import com.sangam.muscleplay.appUtils.ApiCallsConstant
import com.sangam.muscleplay.appUtils.CropImage
import com.sangam.muscleplay.databinding.ActivityEditProfileBinding
import com.sangam.muscleplay.databinding.ErrorBottomDialogLayoutBinding
import com.sangam.muscleplay.userRegistration.model.Measurements
import com.sangam.muscleplay.userRegistration.model.UserDetailsResponseModel
import com.sangam.muscleplay.userRegistration.viewModel.UserDetailData
import com.sangam.muscleplay.userRegistration.viewModel.UserDetailsViewModel
import com.yalantis.ucrop.UCrop
import java.io.File

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    private var age: String? = null
    private var gender: String? = null
    private var weight: String? = null
    private var height: String? = null
    private var hip: String? = null
    private var waist: String? = null
    private var neck: String? = null
    private var photoUrl: String? = null
    private var activityLevel: String? = null
    private var goal: String? = null
    private lateinit var userDetailsViewModel: UserDetailsViewModel
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }
    private lateinit var userData: UserDetailsResponseModel

    private val emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    private var uri: Uri? = null
    private lateinit var imageUri: Uri
    private var storageRef = Firebase.storage

    private val contract =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                binding.tvDone.visibility = View.VISIBLE
                CropImage.cropImage(cropResultLauncher, this, imageUri)

            } else {
                binding.tvDone.visibility = View.VISIBLE
                binding.ivProfile.setImageURI(uri)
                uri = null
            }
        }

    private val requestForPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                contract.launch(imageUri)
            } else {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                    showRationaleDialog()
                } else {
                    val message =
                        "You've denied camera permission twice. To enable it, open app settings."
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            }
        }

    private val cropResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == UCrop.RESULT_ERROR) {
                val cropError = result.data?.let { UCrop.getError(it) }
                Toast.makeText(
                    this, "Crop error: ${cropError?.localizedMessage}", Toast.LENGTH_SHORT
                ).show()
            } else if (result.data != null) {
                val croppedUri = UCrop.getOutput(result.data!!)
                if (croppedUri != null) {
                    binding.ivProfile.setImageDrawable(null)
                    binding.ivProfile.visibility = View.VISIBLE
                    binding.ivProfile.setImageURI(croppedUri)
                    uri = croppedUri
                } else {
                    Toast.makeText(this, "Crop failed", Toast.LENGTH_SHORT).show()
                }
            } else {
            }
        }

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDetailsViewModel = ViewModelProvider(this)[UserDetailsViewModel::class.java]

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        HideStatusBarUtil.hideStatusBar(this@EditProfileActivity)
        window.statusBarColor = Color.WHITE


        storageRef = FirebaseStorage.getInstance()
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.tvDone.visibility = View.VISIBLE
                CropImage.cropImage(cropResultLauncher, this, uri)

            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
        imageUri = createImageUri()!!

        val receivedIntent = intent

        if (receivedIntent.hasExtra("UserData")) {
            val userDetails: UserDetailsResponseModel? =
                receivedIntent.getParcelableExtra("UserData")
            userDetails?.let {
                userData = it
                initListener()
                1
            }
        } else {
            ToastUtil.makeToast(this, "User details not found")
            finish()
        }
        observersForApiCalls()
        initListener2()
    }

    private fun initListener2() {
        binding.tvDone.setOnClickListener {
            uploadImage()
        }
        binding.ivProfile.setOnClickListener {
            chooseImage()
        }


        binding.tvEditProfileConfirm.setOnClickListener {
            val name = binding.EtName.text.toString()
            val phone = binding.EtPhone.text.toString()
            val age = binding.includeAge.EtValue.text.toString()
            val weight = binding.includeWeight.EtValue.text.toString()
            val height = binding.includeHeight.EtValue.text.toString()
            val waist = binding.includeWaist.EtValue.text.toString()
            val hip = binding.includeHip.EtValue.text.toString()
            val neck = binding.includeNeck.EtValue.text.toString()
            if (age.trim().isEmpty() || name.trim().isEmpty() || weight.trim()
                    .isEmpty() || height.trim().isEmpty() || waist.trim().isEmpty() || hip.trim()
                    .isEmpty() || neck.trim().isEmpty()
            ) {
                ToastUtil.makeToast(this@EditProfileActivity, "Empty Field")
                if (name.trim().isEmpty()) binding.EtName.error = "Empty Field"
                if (phone.isEmpty()) binding.EtPhone.error = "Empty Field"
                if (age.isEmpty()) binding.includeAge.EtValue.error = "Empty Field"
                if (weight.isEmpty()) binding.includeWeight.EtValue.error = "Empty Field"
                if (height.isEmpty()) binding.includeHeight.EtValue.error = "Empty Field"
                if (waist.isEmpty()) binding.includeWaist.EtValue.error = "Empty Field"
                if (hip.isEmpty()) binding.includeHip.EtValue.error = "Empty Field"
                if (neck.isEmpty()) binding.includeNeck.EtValue.error = "Empty Field"

            } else if (phone.isNotEmpty() && phone.length != 10) {
                binding.EtPhone.error = "Enter Valid Phone No"

            } else if (!phone.all {
                    it.isDigit()
                }) {
                binding.EtPhone.error = "Enter Valid Phone No (Only Digits)"

            }
//            else if (!name.matches(namePattern.toRegex())) {
//                binding.EtName.error = "Enter Valid Name (Only Alphabets)"
//            }
            else if (name.trim().length < 2) {
                binding.EtName.error = "Enter Minimum 2 Characters"

            } else if (name.trim().length > 30) {
                binding.EtName.error = "Please Enter Less Than 30 Characters"
            } else if (age.toInt() < 1 || age.toInt() > 80) {
                binding.includeAge.EtValue.error = "Please Enter In Given Range"
            } else if (height.toInt() < 130 || height.toInt() > 230) {
                binding.includeHeight.EtValue.error = "Please Enter In Given Range"

            } else if (weight.toInt() < 40 || weight.toInt() > 160) {
                binding.includeWeight.EtValue.error = "Please Enter In Given Range"

            } else if (waist.toInt() < 40 || waist.toInt() > 130) {
                binding.includeWaist.EtValue.error = "Please Enter In Given Range"

            } else if (hip.toInt() < 40 || hip.toInt() > 130) {
                binding.includeHip.EtValue.error = "Please Enter In Given Range"

            } else if (neck.toInt() < 20 || neck.toInt() > 80) {
                binding.includeNeck.EtValue.error = "Please Enter In Given Range"

            } else {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                callPutUserDetailsApi(
                    userId, UserDetailsResponseModel(
                        userId,
                        name,
                        UserDetailData.userDetailsResponseModel?.email,
                        phone,
                        photoUrl ?: UserDetailData.userDetailsResponseModel?.profileImageUrl,
                        gender,
                        age.toInt(),
                        Measurements(
                            height.toFloatOrNull(),
                            weight.toFloatOrNull(),
                            activityLevel,
                            goal,
                            hip.toFloatOrNull(),
                            neck.toFloatOrNull(),
                            waist.toFloatOrNull()
                        )
                    )
                )

            }
        }
    }

    private fun initListener() {

        if (!userData.profileImageUrl.isNullOrEmpty()) {
            Glide.with(this).load(userData.profileImageUrl).placeholder(R.drawable.profile)
                .into(binding.ivProfile)
        }
        age = userData.age.toString()
        gender = userData.gender.toString()
        height = userData.measurements?.height.toString()
        weight = userData.measurements?.weight?.toInt().toString()
        waist = userData.measurements?.waist.toString()
        neck = userData.measurements?.neck.toString()
        hip = userData.measurements?.hip.toString()
        activityLevel = userData.measurements?.activityLevel.toString()
        goal = userData.measurements?.goal.toString()

        binding.EtName.setText(userData.name ?: "")
        binding.EtPhone.setText(userData.phone ?: "")
        if (userData.phone.isNullOrEmpty()) {
            binding.EtLayoutPhone.isHelperTextEnabled = true
            binding.EtLayoutPhone.helperText = "Required*"
        }
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonMale -> gender = "male"
                R.id.radioButtonFemale -> gender = "female"
            }
        }


        val spinner = binding.spinnerActivityLevel
        ArrayAdapter.createFromResource(
            this, R.array.spinner_array_activity_level, R.layout.custom_spinner_layout
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                when (parent?.getItemAtPosition(position).toString()) {
                    "Sedentary: little or no exercise" -> activityLevel = "level_1"
                    "Exercise 1–3 times/week" -> activityLevel = "level_2"
                    "Exercise 4–5 times/week" -> activityLevel = "level_3"
                    "Daily exercise or intense exercise 3–4 times/week" -> activityLevel = "level_4"
                    "Intense exercise 6–7 times/week" -> activityLevel = "level_5"
                    "Very intense exercise daily, or physical job" -> activityLevel = "level_6"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@EditProfileActivity, "Nothing Selected", Toast.LENGTH_SHORT)
                    .show()

            }

        }
        when (activityLevel) {
            "level_1" -> spinner.setSelection(0) // Assuming "Sedentary: little or no exercise" is the first item in the array
            "level_2" -> spinner.setSelection(1)
            "level_3" -> spinner.setSelection(2)
            "level_4" -> spinner.setSelection(3)
            "level_5" -> spinner.setSelection(4)
            "level_6" -> spinner.setSelection(5)
            else -> spinner.setSelection(0) // Default to the first item if the activityLevel doesn't match any of the predefined values
        }

        val spinner2 = binding.spinnerGoal
        ArrayAdapter.createFromResource(
            this, R.array.spinner_array_goal, R.layout.custom_spinner_layout
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
            spinner2.adapter = adapter
        }
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                when (parent?.getItemAtPosition(position).toString()) {
                    "Extreme Weight Gain (1 kg)" -> goal = "Extreme WG"
                    "Normal Weight Gain (0.50 kg)" -> goal = "Normal WG"
                    "Mild Weight Gain (0.25 kg)" -> goal = "Mild WG"
                    "Extreme Weight Loss (1 kg)" -> goal = "Extreme WL"
                    "Normal Weight Loss (0.50 kg)" -> goal = "Normal WL"
                    "Mild Weight Loss (0.25 kg)" -> goal = "Mild WL"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@EditProfileActivity, "Nothing Selected", Toast.LENGTH_SHORT)
                    .show()

            }

        }
        when (goal) {
            "Extreme WG" -> spinner2.setSelection(0) // Assuming "Sedentary: little or no exercise" is the first item in the array
            "Normal WG" -> spinner2.setSelection(1)
            "Mild WG" -> spinner2.setSelection(2)
            "Extreme WL" -> spinner2.setSelection(3)
            "Normal WL" -> spinner2.setSelection(4)
            "Mild WL" -> spinner2.setSelection(5)
            else -> spinner2.setSelection(0) // Default to the first item if the activityLevel doesn't match any of the predefined values
        }

        binding.includeAge.apply {
            tvTopName.text = "Age"
            tvMinValue.text = "1"
            tvMaxValue.text = "80"
            EtValue.setText(age ?: "")
        }
        binding.includeWeight.apply {
            tvTopName.text = "Weight (in Kg)"
            tvMinValue.text = "40 Kg"
            tvMaxValue.text = "160 Kg"
            EtValue.setText(weight ?: "")
        }
        binding.includeHeight.apply {
            tvTopName.text = "Height Size (in cm)"
            tvMinValue.text = "130 cm"
            tvMaxValue.text = "230 cm"
            EtValue.setText(height ?: "")
        }
        binding.includeWaist.apply {
            tvTopName.text = "Waist Size (in cm)"
            tvMinValue.text = "40 cm"
            tvMaxValue.text = "130 cm"
            EtValue.setText(waist ?: "")
        }
        binding.includeHip.apply {
            tvTopName.text = "Hip Size (in cm)"
            tvMinValue.text = "40 cm"
            tvMaxValue.text = "130 cm"
            EtValue.setText(hip ?: "")
        }
        binding.includeNeck.apply {
            tvTopName.text = "Neck Size (in cm)"
            tvMinValue.text = "20 cm"
            tvMaxValue.text = "80 cm"
            EtValue.setText(neck ?: "")
        }
        if (gender == "male") {
            binding.radioButtonMale.isChecked = true
        } else if (gender == "female") {
            binding.radioButtonFemale.isChecked = true
        }
    }


    private fun uploadImage() {
        if (uri == null) {
            Toast.makeText(this, "Please choose an image first", Toast.LENGTH_SHORT).show()
            return
        }

        val progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Uploading")
        progressDialog.show()

        val imageRef = storageRef.reference.child("profile_images")
            .child("${System.currentTimeMillis()} ${auth.currentUser!!.uid}")
        imageRef.putFile(uri!!).addOnSuccessListener { task ->
            task.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                photoUrl = uri.toString()
                progressDialog.dismiss()
                ToastUtil.makeToast(this, "Image Updated")
            }?.addOnFailureListener {
                Toast.makeText(
                    this, "Error: ${it.message}", Toast.LENGTH_SHORT
                ).show()
                progressDialog.dismiss()
            }
        }.addOnFailureListener {
            Toast.makeText(
                this, "Error: ${it.message}", Toast.LENGTH_SHORT
            ).show()
            progressDialog.dismiss()
        }
    }


    private fun chooseImage() {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
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


    private fun callPutUserDetailsApi(
        userId: String?, userDetailsResponseModel: UserDetailsResponseModel?
    ) {
        Log.d("ImageUploadError", userDetailsResponseModel.toString())

        userDetailsViewModel.callPutUserDetails(userId, userDetailsResponseModel)
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
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        userDetailsViewModel.userDetailsResponse.observe(this, Observer {
            userDetailsViewModel.setUserDetailsResponse(it)
            ApiCallsConstant.apiCallsOnceHome = false

            ToastUtil.makeToast(this, "Details Updated")
            setResult(RESULT_OK)
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

    private fun showRationaleDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
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
            this, permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun createImageUri(): Uri? {
        val image = File(this.filesDir, "profile_photos.png")
        return FileProvider.getUriForFile(
            this, "com.sangam.muscleplay.fileProvider", image
        )
    }

}