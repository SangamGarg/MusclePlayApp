package com.sangam.muscleplay.appUtils

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import retrofit2.Call

class CallTypeAdapter<T> : TypeAdapter<Call<T>>() {
    override fun write(out: JsonWriter?, value: Call<T>?) {
        throw UnsupportedOperationException("Serialization not supported")
    }

    override fun read(`in`: JsonReader?): Call<T>? {
        throw UnsupportedOperationException("Deserialization not supported")
    }
}
