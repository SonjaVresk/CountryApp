package hr.algebra.countryapp.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.algebra.countryapp.COUNTRY_PROVIDER_CONTENT_URI
import hr.algebra.countryapp.CountryReceiver
import hr.algebra.countryapp.MONUMENT_PROVIDER_CONTENT_URI
import hr.algebra.countryapp.RULER_PROVIDER_CONTENT_URI
import hr.algebra.countryapp.api.model.Country
import hr.algebra.countryapp.api.model.Monument
import hr.algebra.countryapp.api.model.Ruler
import hr.algebra.countryapp.framework.sendBroadcast
import hr.algebra.countryapp.handler.downloadImageAndStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CountryFetcher(private val context: Context) {

    private val countryApi: CountryApi
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        countryApi = retrofit.create(CountryApi::class.java)
    }

    fun fetchItems(count: Int) {

        val request = countryApi.fetchItems()

        request.enqueue(object: Callback<List<CountryItem>>{
            override fun onResponse(
                call: Call<List<CountryItem>>,
                response: Response<List<CountryItem>>
            ) {
                response.body()?.let { populateItems(it) }
            }

            override fun onFailure(call: Call<List<CountryItem>>, t: Throwable) {
                Log.e(javaClass.name, t.toString(), t)
            }
        })
    }

    private fun populateItems(countryItems: List<CountryItem>) {

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            countryItems.forEach {
                val map = downloadImageAndStore(context, it.map)

                val values = ContentValues().apply {
                    put(Country::name.name, it.name)
                    put(Country::foundation.name, it.foundation)
                    put(Country::fall.name, it.fall)
                    put(Country::capital.name, it.capital)
                    put(Country::location.name, it.location)
                    put(Country::officialLanguage.name, it.officialLanguage)
                    put(Country::religion.name, it.religion)
                    put(Country::government.name, it.government)
                    put(Country::map.name, map ?: "")
                    put(Country::read.name, false)
                }


                // Inserting values into the CountryContentProvider
                val countryUri = context.contentResolver.insert(
                    COUNTRY_PROVIDER_CONTENT_URI,
                    values
                )

                // Extract the ID from the URI after insertion
                val countryID = countryUri?.lastPathSegment?.toIntOrNull() ?: -1

                val rulersValues = mutableListOf<ContentValues>()
                it.rulers.forEach { ruler ->
                    val rulerValues = ContentValues().apply {
                        put(Ruler::name.name, ruler.name)
                        put(Ruler::reign.name, ruler.reign)
                        put(Ruler::CountryID.name, countryID)
                    }
                    rulersValues.add(rulerValues)
                }


                context.contentResolver.bulkInsert(
                    RULER_PROVIDER_CONTENT_URI,
                    rulersValues.toTypedArray()
                )

                val monumentsValues = mutableListOf<ContentValues>()
                it.preservedMonuments.forEach { monument ->
                    val monumentValues = ContentValues().apply {
                        put(Monument::name.name, monument.name)
                        put(Monument::location.name, monument.location)
                        put(Monument::description.name, monument.description)
                        put(Monument::CountryID.name, countryID)
                    }
                    monumentsValues.add(monumentValues)
                }

                context.contentResolver.bulkInsert(
                    MONUMENT_PROVIDER_CONTENT_URI,
                    monumentsValues.toTypedArray()
                )

            }

            context.sendBroadcast<CountryReceiver>()
        }
    }
}