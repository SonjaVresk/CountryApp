package hr.algebra.countryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.algebra.countryapp.adapter.RulerPagerAdapter
import hr.algebra.countryapp.api.model.Ruler
import hr.algebra.countryapp.databinding.ActivityRulersPagerBinding
import hr.algebra.countryapp.framework.fetchRulers


const val POSITIONRULER = "hr.algebra.countryapp.ruler_pos"

class RulerPagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRulersPagerBinding
    private lateinit var rulers: MutableList<Ruler>
    private var position = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRulersPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPager()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initPager() {
        rulers = fetchRulers()
        position = intent.getIntExtra(POSITIONRULER, position)
        binding.viewPager.adapter = RulerPagerAdapter(this, rulers)
        binding.viewPager.currentItem = position
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}