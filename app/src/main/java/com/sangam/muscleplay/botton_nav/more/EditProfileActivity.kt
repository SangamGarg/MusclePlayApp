package com.sangam.muscleplay.botton_nav.more

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sangam.muscleplay.AppUtils.ApiCallsConstant
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil
import com.sangam.muscleplay.AppUtils.ToastUtil
import com.sangam.muscleplay.R
import com.sangam.muscleplay.UserDataUtils.UserViewModel
import com.sangam.muscleplay.UserDataUtils.model.UserDataExtra
import com.sangam.muscleplay.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {
    private val database by lazy {
        Firebase.firestore
    }
    private var age: String? = null
    private var gender: String? = null
    private var weight: String? = null
    private var height: String? = null
    private var hip: String? = null
    private var waist: String? = null
    private var neck: String? = null
    private var activityLevel: String? = null
    private var goal: String? = null
    private val userViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
    }


    private val emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"


    private val binding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideStatusBarUtil.hideStatusBar(this@EditProfileActivity)
        callGetUserData()
        callGetUserExtraData()
        observeUserData()
        observeUserExtraData()
        observerProgressResponse()
        initListener2()
    }

    private fun initListener2() {

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
            else if (name.trim().length < 6) {
                binding.EtName.error = "Enter Minimum 6 Characters"

            } else if (name.trim().length > 20) {
                binding.EtName.error = "Please Enter Less Than 20 Characters"
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

                if (userId != null) {
                    val userDataExtra = UserDataExtra(
                        datafilled = true,
                        age,
                        gender,
                        height,
                        weight,
                        hip,
                        neck,
                        waist,
                        activityLevel,
                        goal
                    )
                    storeUserExtraDetails(userId, userDataExtra)

                    val userDocumentReference = database.collection("users").document(userId)

                    val userUpdates = hashMapOf<String, Any>(
                        "name" to name, "phone" to phone
                    )

                    // Update the document.
                    userDocumentReference.update(userUpdates).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Toast.makeText(
                                this@EditProfileActivity,
                                "Details updated successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                            ApiCallsConstant.apiCallsOnceHome = false
                        } else {
                            Toast.makeText(
                                this@EditProfileActivity,
                                "Failed to update details: ${updateTask.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }


                }

            }

        }
    }

    private fun storeUserExtraDetails(userId: String, userDataExtra: UserDataExtra) {
        database.collection("users").document(userId).update(userDataExtra.toMap())
            .addOnSuccessListener {
                Toast.makeText(this, "Details updated successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(
                    this, "Failed to update details: ${it.message}", Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun UserDataExtra.toMap(): Map<String, Any?> {
        return mapOf(
            "age" to age,
            "gender" to gender,
            "height" to height,
            "weight" to weight,
            "hip" to hip,
            "neck" to neck,
            "waist" to waist,
            "activity_level" to activity_level,
            "goal" to goal
        )
    }

    fun callGetUserData() {
        userViewModel.getUserData()
    }

    fun callGetUserExtraData() {
        userViewModel.getUserDataExtra()
    }

    fun observeUserData() {
        userViewModel.userDataResponse.observe(this, Observer {

            binding.EtName.setText(it?.name ?: "")
            binding.EtPhone.setText(it?.phone ?: "")
            if (it?.phone.isNullOrEmpty()) {
                binding.EtLayoutPhone.isHelperTextEnabled = true
                binding.EtLayoutPhone.helperText = "Required*"
            }
        })
    }

    fun observeUserExtraData() {
        userViewModel.userDataExtraResponse.observe(this, Observer {
            Log.d("USEREXTRADETAIL", it.toString())
            age = it.age.toString()
            gender = it.gender.toString()
            height = it.height.toString()
            weight = it.weight.toString()
            waist = it.waist.toString()
            neck = it.neck.toString()
            hip = it.hip.toString()
            activityLevel = it.activity_level.toString()
            goal = it.goal.toString()
            initListener()

        })
    }

    private fun observerProgressResponse() {
        userViewModel.showProgress.observe(this, Observer {
            if (it) {
                binding.mainView.visibility = View.GONE
                binding.progreessBar.visibility = View.VISIBLE
            } else {
                binding.mainView.visibility = View.VISIBLE
                binding.progreessBar.visibility = View.GONE
            }
        })

        userViewModel.showProgressExtra.observe(this, Observer {
            if (it) {
                binding.mainView.visibility = View.GONE
                binding.progreessBar.visibility = View.VISIBLE
            } else {
                binding.mainView.visibility = View.VISIBLE
                binding.progreessBar.visibility = View.GONE
            }
        })
    }

    private fun initListener() {

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
                val selectedItem = parent?.getItemAtPosition(position).toString()
                when (selectedItem) {
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
                val selectedItem = parent?.getItemAtPosition(position).toString()
                when (selectedItem) {
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
}