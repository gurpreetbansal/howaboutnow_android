package com.how_about_now.app.data.profile_data

import android.os.Parcel
import android.os.Parcelable

data class GetUserInfoMsg(
    val about_me: String = "",
    val age: Int = 0,
    val birthday: String = "",
    val company: String = "",
    val first_name: String = "",
    val gender: String = "",
    val image1: String = "",
    val image2: String = "",
    val image3: String = "",
    val image4: String = "",
    val image5: String = "",
    val image6: String = "",
    val job_title: String = "",
    val last_name: String = "",
    val school: String = "",
    val user_answer: ArrayList<GetUserAnswer>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        TODO("user_answer")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(about_me)
        parcel.writeInt(age)
        parcel.writeString(birthday)
        parcel.writeString(company)
        parcel.writeString(first_name)
        parcel.writeString(gender)
        parcel.writeString(image1)
        parcel.writeString(image2)
        parcel.writeString(image3)
        parcel.writeString(image4)
        parcel.writeString(image5)
        parcel.writeString(image6)
        parcel.writeString(job_title)
        parcel.writeString(last_name)
        parcel.writeString(school)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetUserInfoMsg> {
        override fun createFromParcel(parcel: Parcel): GetUserInfoMsg {
            return GetUserInfoMsg(parcel)
        }

        override fun newArray(size: Int): Array<GetUserInfoMsg?> {
            return arrayOfNulls(size)
        }
    }
}