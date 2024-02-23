package hr.algebra.countryapp.api

import com.google.gson.annotations.SerializedName

data class PreservedMonuments(

    @SerializedName("name") val name : String,
    @SerializedName("location") val location : String,
    @SerializedName("description") val description : String
)
