package hr.algebra.countryapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.countryapp.R
import hr.algebra.countryapp.api.model.Monument
import hr.algebra.countryapp.framework.fetchCountryById

class MonumentPagerAdapter(
    private val context: Context,
    private val monuments: MutableList<Monument>

) : RecyclerView.Adapter<MonumentPagerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val tvName = itemView.findViewById<TextView>(R.id.tvName)
        private val tvLocation = itemView.findViewById<TextView>(R.id.tvLocation)
        private val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        private val tvCountry = itemView.findViewById<TextView>(R.id.tvCountry)

        fun bind(context: Context, monument: Monument) {
            tvLocation.text = monument.location
            tvDescription.text = monument.description
            tvName.text = monument.name

            val country = context.fetchCountryById(monument.CountryID)
            tvCountry.text = country?.name ?: "Unknown Country"
        }

}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.monument_pager, parent, false)
        )
    }

    override fun getItemCount() = monuments.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ruler = monuments[position]
        holder.bind(context, ruler)
    }
}