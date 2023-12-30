package com.servicee.servicee.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp

@SuppressLint("ParcelCreator")
data class Randevu(val from : String,
                   val to : String,
                   val todo : ArrayList<String>,
                   val time : Timestamp,
                   val documentId: String ) : Parcelable {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        TODO("Not yet implemented")
    }

}