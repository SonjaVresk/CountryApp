package hr.algebra.countryapp.fragment

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import hr.algebra.countryapp.R
import hr.algebra.countryapp.api.model.Country
import java.io.File


private const val REQUEST_IMAGE_PICK = 1

class AddFragment : Fragment() {

    private lateinit var etName: EditText
    private lateinit var etFundation: EditText
    private lateinit var etFall: EditText
    private lateinit var etCapital: EditText
    private lateinit var etLanguage: EditText
    private lateinit var etReligion: EditText
    private lateinit var btnCommit: Button
    private lateinit var ivPicture: ImageView
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etName = view.findViewById(R.id.etName)
        etFundation = view.findViewById(R.id.etFundation)
        etFall = view.findViewById(R.id.etFall)
        etCapital = view.findViewById(R.id.etCapital)
        etLanguage = view.findViewById(R.id.etLanguage)
        etReligion = view.findViewById(R.id.etReligion)
        btnCommit = view.findViewById(R.id.btnCommit)
        ivPicture = view.findViewById(R.id.ivPicture)

        ivPicture.setOnClickListener {
            openGallery()
        }

        btnCommit.setOnClickListener {
            saveCountries()

            hideKeyboard()
        }
    }

    private fun saveCountries() {
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
            put(Country::location.name, "")
            put(Country::officialLanguage.name, language)
            put(Country::religion.name, religion)
            put(Country::government.name, "")
            put(Country::map.name, map ?: "")
            put(Country::read.name, false)
        }

        val contentResolver = requireContext().contentResolver
        val countryUri = contentResolver.insert(
            hr.algebra.countryapp.COUNTRY_PROVIDER_CONTENT_URI,
            contentValues
        )

        if (countryUri != null) {
            Toast.makeText(requireContext(), "Successfully saved", Toast.LENGTH_SHORT).show()
            etName.text.clear()
            etFundation.text.clear()
            etFall.text.clear()
            etCapital.text.clear()
            etLanguage.text.clear()
            etReligion.text.clear()
        } else {
            Toast.makeText(requireContext(), "Unsuccessful save", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
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

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etName.windowToken, 0)
    }

}