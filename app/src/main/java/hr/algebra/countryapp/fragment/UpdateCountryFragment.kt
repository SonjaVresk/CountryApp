package hr.algebra.countryapp.fragment

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import hr.algebra.countryapp.COUNTRY_PROVIDER_CONTENT_URI
import hr.algebra.countryapp.R
import hr.algebra.countryapp.api.model.Country
import hr.algebra.countryapp.framework.fetchCountryById
import java.io.File

private const val REQUEST_IMAGE_PICK = 1

class UpdateCountryFragment : Fragment() {

    private lateinit var etName: EditText
    private lateinit var etFundation: EditText
    private lateinit var etFall: EditText
    private lateinit var etCapital: EditText
    private lateinit var etLanguage: EditText
    private lateinit var etReligion: EditText
    private lateinit var btnUpdateCountry: Button
    private lateinit var ivPicture: ImageView
    private var selectedImageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_country, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etName = view.findViewById(R.id.etName)
        etFundation = view.findViewById(R.id.etFundation)
        etFall = view.findViewById(R.id.etFall)
        etCapital = view.findViewById(R.id.etCapital)
        etLanguage = view.findViewById(R.id.etLanguage)
        etReligion = view.findViewById(R.id.etReligion)
        btnUpdateCountry = view.findViewById(R.id.btnUpdateCountry)
        ivPicture = view.findViewById(R.id.ivPicture)

        val countryId = arguments?.getLong("countryId") ?: -1

        val country = requireContext().fetchCountryById(countryId)

        country?.let {
            populateFields(it)
        }

        ivPicture.setOnClickListener {
            openGallery()
        }

        btnUpdateCountry.setOnClickListener {
            val name = etName.text.toString()
            val foundation = etFundation.text.toString()
            val fall = etFall.text.toString()
            val capital = etCapital.text.toString()
            val language = etLanguage.text.toString()
            val religion = etReligion.text.toString()
            val map = selectedImageUri?.let { uri -> uri.path}

            val contentValues = ContentValues().apply {
                put(Country::name.name, name)
                put(Country::foundation.name, foundation)
                put(Country::fall.name, fall)
                put(Country::capital.name, capital)
                put(Country::officialLanguage.name, language)
                put(Country::religion.name, religion)
                put(Country::map.name, map ?: "")
            }

            val updatedRows = requireContext().contentResolver.update(
                COUNTRY_PROVIDER_CONTENT_URI,
                contentValues,
                "${Country::_id.name}=?",
                arrayOf(countryId.toString())
            )

            if (updatedRows > 0) {
                Toast.makeText(requireContext(), "Country updated", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Failed to update country", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun populateFields(country: Country) {
        etName.setText(country.name)
        etFundation.setText(country.foundation)
        etFall.setText(country.fall)
        etCapital.setText(country.capital)
        etLanguage.setText(country.officialLanguage)
        etReligion.setText(country.religion)

        if (country.map.isNotEmpty()) {
            Picasso.get()
                .load(File(country.map))
                .error(R.drawable.world)
                .into(ivPicture)
        } else {
            ivPicture.setImageResource(R.drawable.world)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            selectedImageUri?.let { uri ->
                val timestamp = System.currentTimeMillis()
                val filename = "image_$timestamp.jpg"
                val destinationFile = File(requireContext().getExternalFilesDir(null), filename)
                context?.contentResolver?.openInputStream(uri)?.use { inputStream ->
                    destinationFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                this.selectedImageUri = Uri.fromFile(destinationFile)
                ivPicture.setImageURI(this.selectedImageUri)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }
}