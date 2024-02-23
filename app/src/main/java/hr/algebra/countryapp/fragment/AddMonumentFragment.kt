package hr.algebra.countryapp.fragment

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import hr.algebra.countryapp.COUNTRY_PROVIDER_CONTENT_URI
import hr.algebra.countryapp.MONUMENT_PROVIDER_CONTENT_URI
import hr.algebra.countryapp.R
import hr.algebra.countryapp.RULER_PROVIDER_CONTENT_URI
import hr.algebra.countryapp.api.model.Monument
import hr.algebra.countryapp.api.model.Ruler


class AddMonumentFragment : Fragment() {

    private lateinit var etName: EditText
    private lateinit var etLocation: EditText
    private lateinit var etDescription: EditText
    private lateinit var spinnerCountries: Spinner
    private lateinit var btnAddMonument: Button
    private lateinit var countryIds: Map<String, Long>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_monument, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etName = view.findViewById(R.id.etName)
        etLocation = view.findViewById(R.id.etLocation)
        etDescription = view.findViewById(R.id.etDescription)
        spinnerCountries = view.findViewById(R.id.spinnerCountries)
        btnAddMonument = view.findViewById(R.id.btnAddMonument)

        val countries = getCountryNames()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountries.adapter = adapter

        view.findViewById<Button>(R.id.btnAddMonument).setOnClickListener {
            saveMonument()

            hideKeyboard()
        }
    }

    private fun getCountryNames(): List<String> {
        val countryNames = mutableListOf<String>()
        val ids = mutableMapOf<String, Long>() // Mapa za države i njihov Id

        val cursor = requireContext().contentResolver.query(
            COUNTRY_PROVIDER_CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.use { c ->
            while (c.moveToNext()) {
                val countryId = c.getLong(c.getColumnIndexOrThrow("_id"))
                val countryName = c.getString(c.getColumnIndexOrThrow("name"))
                countryNames.add(countryName)
                ids[countryName] = countryId // ID spremi u mapu
            }
        }
        countryIds = ids
        return countryNames
    }

    private fun saveMonument() {
        val name = etName.text.toString()
        val location = etLocation.text.toString()
        val description = etDescription.text.toString()
        val countryName = spinnerCountries.selectedItem.toString()
        val countryId = countryIds[countryName] ?: -1 // ID države iz mape countryIds

        val contentValues = ContentValues().apply {
            put(Monument::name.name, name)
            put(Monument::location.name, location)
            put(Monument::description.name, description)
            put(Monument::CountryID.name, countryId)
        }

        val contentResolver = requireContext().contentResolver
        val rulerUri = contentResolver.insert(
            MONUMENT_PROVIDER_CONTENT_URI,
            contentValues
        )

        if (rulerUri != null) {
            Toast.makeText(requireContext(), "Monument saved", Toast.LENGTH_SHORT).show()
            etName.text.clear()
            etLocation.text.clear()
            etDescription.text.clear()
        } else {
            Toast.makeText(requireContext(), "Error in saving monument", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etName.windowToken, 0)
    }
}