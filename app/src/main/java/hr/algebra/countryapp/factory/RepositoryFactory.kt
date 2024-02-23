package hr.algebra.countryapp.factory

import android.content.Context
import hr.algebra.countryapp.dao.CountrySqlHelper

fun getRepository(context: Context?) = CountrySqlHelper(context)