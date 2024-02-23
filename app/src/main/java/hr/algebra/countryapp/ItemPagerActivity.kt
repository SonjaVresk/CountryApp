package hr.algebra.countryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.algebra.countryapp.adapter.ItemPagerAdapter
import hr.algebra.countryapp.api.model.Country
import hr.algebra.countryapp.databinding.ActivityItemPagerBinding
import hr.algebra.countryapp.framework.fetchCountries


const val POSITION = "hr.algebra.countryapp.item_pos"
class ItemPagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemPagerBinding
    private lateinit var countries: MutableList<Country>
    private var position = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPager()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initPager() {
        countries = fetchCountries()
        position = intent.getIntExtra(POSITION, position)
        binding.viewPager.adapter = ItemPagerAdapter(this, countries)
        binding.viewPager.currentItem = position
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}