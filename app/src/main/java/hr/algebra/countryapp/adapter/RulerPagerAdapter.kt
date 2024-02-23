package hr.algebra.countryapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.countryapp.R
import hr.algebra.countryapp.api.model.Ruler
import hr.algebra.countryapp.framework.fetchCountryById

class RulerPagerAdapter(
    private val context: Context,
    private val rulers: MutableList<Ruler>

) : RecyclerView.Adapter<RulerPagerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val tvName = itemView.findViewById<TextView>(R.id.tvName)
        private val tvReign = itemView.findViewById<TextView>(R.id.tvReign)
        private val tvCountry = itemView.findViewById<TextView>(R.id.tvCountry)

        fun bind(context: Context, ruler: Ruler) {
            tvReign.text = ruler.reign
            tvName.text = ruler.name

            val country = context.fetchCountryById(ruler.CountryID)
            tvCountry.text = country?.name ?: "Unknown Country"
        }

}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.ruler_pager, parent, false)
        )
    }

    override fun getItemCount() = rulers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ruler = rulers[position]
        holder.bind(context, ruler)
    }
}