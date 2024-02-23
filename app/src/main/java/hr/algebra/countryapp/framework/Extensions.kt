package hr.algebra.countryapp.framework

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import hr.algebra.countryapp.COUNTRY_PROVIDER_CONTENT_URI
import hr.algebra.countryapp.MONUMENT_PROVIDER_CONTENT_URI
import hr.algebra.countryapp.RULER_PROVIDER_CONTENT_URI
import hr.algebra.countryapp.api.model.Country
import hr.algebra.countryapp.api.model.Monument
import hr.algebra.countryapp.api.model.Ruler

fun View.applyAnimation(animationId: Int) = startAnimation(
    AnimationUtils.loadAnimation(context, animationId)
)

inline fun <reified T : Activity> Context.startActivity() =
    startActivity(Intent(this, T::class.java)
        .apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    )

inline fun <reified T : Activity> Context.startActivity(key:String, value:Int) =
    startActivity(Intent(this, T::class.java)
        .apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(key, value)
        }
    )

inline fun <reified T : BroadcastReceiver> Context.sendBroadcast() =
    sendBroadcast(Intent(this, T::class.java))

fun Context.setBooleanPreference(key: String, value: Boolean = true) =
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit()
        .putBoolean(key, value)
        .apply()

fun Context.getBooleanPreference(key: String) = PreferenceManager
    .getDefaultSharedPreferences(this)
    .getBoolean(key, false)

fun callDelayed(delay: Long, work: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(
        work,
        delay
    )
}

fun Context.isOnline(): Boolean {

    val connectivityManager = getSystemService<ConnectivityManager>()

    connectivityManager?.activeNetwork?.let { network ->
        connectivityManager.getNetworkCapabilities(network)?.let {networkCapabilities ->
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }
    }

    return false
}

@SuppressLint("Range")
fun Context.fetchCountries(): MutableList<Country>{
    val countries = mutableListOf<Country>()

    val cursor = contentResolver.query(
        COUNTRY_PROVIDER_CONTENT_URI,
        null,
        null,
        null,
        null
    )
    while (cursor != null && cursor.moveToNext()){
        countries.add(Country(
            cursor.getLong(cursor.getColumnIndexOrThrow(Country::_id.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Country::name.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Country::foundation.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Country::fall.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Country::capital.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Country::location.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Country::officialLanguage.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Country::religion.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Country::government.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Country::map.name)),
            cursor.getInt(cursor.getColumnIndexOrThrow(Country::read.name)) == 1

        ))
    }
    return countries
}

@SuppressLint("Range")
fun Context.fetchRulers(): MutableList<Ruler>{
    val rulers = mutableListOf<Ruler>()

    val cursor = contentResolver.query(
        RULER_PROVIDER_CONTENT_URI,
        null,
        null,
        null,
        null
    )
    while (cursor != null && cursor.moveToNext()){
        rulers.add(Ruler(
            cursor.getLong(cursor.getColumnIndexOrThrow(Ruler::_id.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Ruler::name.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Ruler::reign.name)),
            cursor.getLong(cursor.getColumnIndexOrThrow(Ruler::CountryID.name))
        ))
    }
    return rulers
}



@SuppressLint("Range")
fun Context.fetchMonuments(): MutableList<Monument>{
    val monuments = mutableListOf<Monument>()

    val cursor = contentResolver.query(
        MONUMENT_PROVIDER_CONTENT_URI,
        null,
        null,
        null,
        null
    )
    while (cursor != null && cursor.moveToNext()){
        monuments.add(
            Monument(
            cursor.getLong(cursor.getColumnIndexOrThrow(Monument::_id.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Monument::name.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Monument::location.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Monument::description.name)),
            cursor.getLong(cursor.getColumnIndexOrThrow(Monument::CountryID.name))
        ))
    }
    return monuments
}


@SuppressLint("Range")
fun Context.fetchCountryById(countryId: Long): Country? {
    val selection = "${Country::_id.name} = ?"
    val selectionArgs = arrayOf(countryId.toString())

    val cursor = contentResolver.query(
        COUNTRY_PROVIDER_CONTENT_URI,
        null,
        selection,
        selectionArgs,
        null
    )

    cursor?.use { cursor ->
        if (cursor.moveToFirst()) {
            return Country(
                cursor.getLong(cursor.getColumnIndexOrThrow(Country::_id.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(Country::name.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(Country::foundation.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(Country::fall.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(Country::capital.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(Country::location.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(Country::officialLanguage.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(Country::religion.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(Country::government.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(Country::map.name)),
                cursor.getInt(cursor.getColumnIndexOrThrow(Country::read.name)) == 1
            )
        }
    }
    return null
}
