package com.sangam.muscleplay.userRegistration.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class UserDetailsResponseModel(

    @SerializedName("userId") val userId: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("profileImageUrl") val profileImageUrl: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("measurements") val measurements: Measurements? = Measurements()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readParcelable(Measurements::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(profileImageUrl)
        parcel.writeString(gender)
        parcel.writeValue(age)
        parcel.writeParcelable(measurements, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserDetailsResponseModel> {
        override fun createFromParcel(parcel: Parcel): UserDetailsResponseModel {
            return UserDetailsResponseModel(parcel)
        }

        override fun newArray(size: Int): Array<UserDetailsResponseModel?> {
            return arrayOfNulls(size)
        }
    }
}

data class Measurements(

    @SerializedName("height") val height: Float? = null,
    @SerializedName("weight") val weight: Float? = null,
    @SerializedName("activityLevel") val activityLevel: String? = null,
    @SerializedName("goal") val goal: String? = null,
    @SerializedName("hip") val hip: Float? = null,
    @SerializedName("neck") val neck: Float? = null,
    @SerializedName("waist") val waist: Float? = null

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Float::class.java.classLoader) as? Float
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(height)
        parcel.writeValue(weight)
        parcel.writeString(activityLevel)
        parcel.writeString(goal)
        parcel.writeValue(hip)
        parcel.writeValue(neck)
        parcel.writeValue(waist)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Measurements> {
        override fun createFromParcel(parcel: Parcel): Measurements {
            return Measurements(parcel)
        }

        override fun newArray(size: Int): Array<Measurements?> {
            return arrayOfNulls(size)
        }
    }
}