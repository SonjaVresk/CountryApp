package hr.algebra.countryapp.api

import com.google.gson.annotations.SerializedName

data class Rulers(

    @SerializedName("name") val name : String,
    @SerializedName("reign") val reign : String
)
