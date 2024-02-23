package hr.algebra.countryapp.api

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class CountryWorker(private val context: Context,
    workerParams: WorkerParameters
    ) : Worker(context, workerParams) {

    override fun doWork(): Result {
        CountryFetcher(context).fetchItems(10)
        return Result.success()
    }
}