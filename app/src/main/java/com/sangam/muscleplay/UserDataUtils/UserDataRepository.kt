package com.sangam.muscleplay.UserDataUtils

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sangam.muscleplay.UserDataUtils.model.UserDataExtra
import com.sangam.muscleplay.UserDataUtils.model.UserDataNameAndEmail

class UserDataRepository {
    val showProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var userDataResponse = MutableLiveData<UserDataNameAndEmail>()
    val showProgressExtra = MutableLiveData<Boolean>()
    var userDataExtraResponse = MutableLiveData<UserDataExtra>()

    fun getUserData() {
        showProgress.postValue(true)
        val firebaseAuth = FirebaseAuth.getInstance()
        val database = Firebase.firestore
        database.collection("users").document(firebaseAuth.currentUser!!.uid).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showProgress.postValue(false)
                    val document = task.result
                    if (document.exists()) {
                        val name = document.getString("name")
                        val email = document.getString("email")
                        val phone = document.getString("phone")
                        val data = UserDataNameAndEmail(name, email, phone )
                        userDataResponse.postValue(data)
                    }
                } else {
                    showProgress.postValue(false)
                    errorMessage.postValue("Error getting user data: ${task.exception?.message}")
                }
            }
    }

    fun getUserDataExtra() {
        showProgressExtra.postValue(true)
        val firebaseAuth = FirebaseAuth.getInstance()
        val database = Firebase.firestore
        database.collection("users").document(firebaseAuth.currentUser!!.uid).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showProgressExtra.postValue(false)
                    val document = task.result
                    if (document.exists()) {
                        val dataFilled = document.getBoolean("datafilled")
                        val age = document.getString("age")
                        val gender = document.getString("gender")
                        val height = document.getString("height")
                        val weight = document.getString("weight")
                        val hip = document.getString("hip")
                        val neck = document.getString("neck")
                        val waist = document.getString("waist")
                        val activity_level = document.getString("activity_level")
                        val data = UserDataExtra(
                            dataFilled,
                            age, gender, height, weight, hip, neck, waist, activity_level
                        )
                        userDataExtraResponse.postValue(data)
                    }
                } else {
                    showProgressExtra.postValue(false)
                    errorMessage.postValue("Error getting user data: ${task.exception?.message}")
                }
            }
    }
}