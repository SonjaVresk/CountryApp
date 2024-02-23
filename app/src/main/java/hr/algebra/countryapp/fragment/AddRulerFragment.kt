package hr.algebra.countryapp.fragment

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
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
import hr.algebra.countryapp.R
import hr.algebra.countryapp.RULER_PROVIDER_CONTENT_URI
import hr.algebra.countryapp.api.model.Ruler
import hr.algebra.countryapp.dao.Repository

class AddRulerFragment : Fragment() {

    private lateinit var etName: EditText
    private lateinit var etReign: EditText
    private lateinit var spinnerCountries: Spinner
    private lateinit var btnAddRuler: Button
    private lateinit var countryIds: Map<String, Long>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_ruler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etName = view.findViewById(R.id.etName)
        etReign = view.findViewById(R.id.etReign)
        spinnerCountries = view.findViewById(R.id.spinnerCountries)
        btnAddRuler = view.findViewById(R.id.btnAddRuler)

        val countries = getCountryNames()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountries.adapter = adapter

        view.findViewById<Button>(R.id.btnAddRuler).setOnClickListener {
            saveRuler()

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
                ids[countryName] = countryId // Id u mapu
            }
        }
        countryIds = ids
        return countryNames
    }


    private fun saveRuler() {
        val name = etName.text.toString()
        val reign = etReign.text.toString()
        val countryName = spinnerCountries.selectedItem.toString()
        val countryId = countryIds[countryName] ?: -1 // nađi ID države iz mape countryIds

        val contentValues = ContentValues().apply {
            put(Ruler::name.name, name)
            put(Ruler::reign.name, reign)
            put(Ruler::CountryID.name, countryId)
        }

        val contentResolver = requireContext().contentResolver
        val rulerUri = contentResolver.insert(
            RULER_PROVIDER_CONTENT_URI,
            contentValues
        )

        if (rulerUri != null) {
            Toast.makeText(requireContext(), "Ruler saved", Toast.LENGTH_SHORT).show()
            etName.text.clear()
            etReign.text.clear()
        } else {
            Toast.makeText(requireContext(), "Error in saving ruler", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etName.windowToken, 0)
    }

}
