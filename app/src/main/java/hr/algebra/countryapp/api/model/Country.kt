package hr.algebra.countryapp.api.model


import hr.algebra.countryapp.api.PreservedMonuments
import hr.algebra.countryapp.api.Rulers


data class Country(
    var _id:Long?,
    val name : String,
    val foundation : String,
    val fall : String,
    val capital : String,
    val location : String,
    val officialLanguage : String,
    val religion : String,
    val government : String,
    val map : String,
    var read: Boolean
)