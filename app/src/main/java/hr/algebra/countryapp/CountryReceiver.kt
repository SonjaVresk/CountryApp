package hr.algebra.countryapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.algebra.countryapp.framework.setBooleanPreference
import hr.algebra.countryapp.framework.startActivity

class CountryReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        context.setBooleanPreference(DATA_IMPORTED)
        context.startActivity<CountryActivity>()
    }
}