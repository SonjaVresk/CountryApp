package hr.algebra.countryapp.api.model

data class Monument(
    var _id:Long?,
    val name : String,
    val location : String,
    val description : String,
    val CountryID : Long
)
