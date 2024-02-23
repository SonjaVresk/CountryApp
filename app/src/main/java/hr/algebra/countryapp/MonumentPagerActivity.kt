package hr.algebra.countryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.algebra.countryapp.adapter.MonumentPagerAdapter
import hr.algebra.countryapp.api.model.Monument
import hr.algebra.countryapp.databinding.ActivityMonumentPagerBinding
import hr.algebra.countryapp.framework.fetchMonuments


const val POSITIONMONUMENT = "hr.algebra.countryapp.monument_pos"

class MonumentPagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMonumentPagerBinding
    private lateinit var monuments: MutableList<Monument>
    private var position = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonumentPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPager()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initPager() {
        monuments = fetchMonuments()
        position = intent.getIntExtra(POSITIONMONUMENT, position)
        binding.viewPager.adapter = MonumentPagerAdapter(this, monuments)
        binding.viewPager.currentItem = position
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}