package com.sangam.muscleplay.UserDataUtils.model

data class UserDataNameAndEmail(val name: String?, val email: String?, val phone: String?)
data class UserDataExtra(
    val datafilled: Boolean?,
    val age: String?,
    val gender: String?,
    val height: String?,
    val weight: String?,
    val hip: String?,
    val neck: String?,
    val waist: String?,
    val activity_level: String?,
    val goal: String?
)
