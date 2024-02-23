package hr.algebra.countryapp.api

import com.google.gson.annotations.SerializedName

data class CountryItem(
    @SerializedName("name") val name : String,
    @SerializedName("foundation") val foundation : String,
    @SerializedName("fall") val fall : String,
    @SerializedName("capital") val capital : String,
    @SerializedName("majorCities") val majorCities : List<String>,
    @SerializedName("location") val location : String,
    @SerializedName("officialLanguage") val officialLanguage : String,
    @SerializedName("rulers") val rulers : List<Rulers>,
    @SerializedName("religion") val religion : String,
    @SerializedName("government") val government : String,
    @SerializedName("map") val map : String,
    @SerializedName("preservedMonuments") val preservedMonuments : List<PreservedMonuments>
)
