package hr.algebra.countryapp.api

import retrofit2.Call
import retrofit2.http.GET

const val API_URL = "https://raw.githubusercontent.com/SonjaVresk/oc/main/"

interface CountryApi {
    @GET("oc.json")
    fun fetchItems()
    : Call<List<CountryItem>>
}