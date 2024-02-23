package hr.algebra.countryapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import hr.algebra.countryapp.api.CountryWorker
import hr.algebra.countryapp.databinding.ActivityStartScreenBinding
import hr.algebra.countryapp.framework.applyAnimation
import hr.algebra.countryapp.framework.callDelayed
import hr.algebra.countryapp.framework.getBooleanPreference
import hr.algebra.countryapp.framework.isOnline
import hr.algebra.countryapp.framework.startActivity

private const val DELAY = 3000L

const val DATA_IMPORTED = "hr.algebra.countryapp.data_imported"

class StartScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimations()
        redirect()
    }

    private fun startAnimations() {
        binding.ivStart.applyAnimation(R.anim.rotate)
        binding.tvStart.applyAnimation(R.anim.scale)
    }

    private fun redirect() {
        if (getBooleanPreference(DATA_IMPORTED)) {
            callDelayed(DELAY) {
                startActivity<CountryActivity>()
            }
        } else {
            if(isOnline()) {

                WorkManager.getInstance(this).apply{
                    enqueueUniqueWork(
                        DATA_IMPORTED,
                        ExistingWorkPolicy.KEEP,
                        OneTimeWorkRequest.Companion.from(CountryWorker::class.java)
                    )
                }

            } else{
                binding.tvStart.text = getString(R.string.no_internet)
                callDelayed(DELAY) {
                    finish()
                }
            }
        }
    }
}