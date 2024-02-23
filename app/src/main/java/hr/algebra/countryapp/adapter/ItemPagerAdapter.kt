package hr.algebra.countryapp.adapter

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

import hr.algebra.countryapp.COUNTRY_PROVIDER_CONTENT_URI

import hr.algebra.countryapp.R
import hr.algebra.countryapp.api.model.Country

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class ItemPagerAdapter(
    private val context: Context,
    private val countries: MutableList<Country>

) : RecyclerView.Adapter<ItemPagerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        val ivRead = itemView.findViewById<ImageView>(R.id.ivRead)


        private val tvName = itemView.findViewById<TextView>(R.id.tvName)
        private val tvFundation = itemView.findViewById<TextView>(R.id.tvFundation)
        private val tvFall = itemView.findViewById<TextView>(R.id.tvFall)
        private val tvCapital= itemView.findViewById<TextView>(R.id.tvCapital)
        private val tvLanguage = itemView.findViewById<TextView>(R.id.tvLanguage)
        private val tvReligion= itemView.findViewById<TextView>(R.id.tvReligion)

        fun bind(country: Country) {
            tvFundation.text = country.foundation
            tvFall.text = country.fall
            tvCapital.text = country.capital
            tvLanguage.text = country.officialLanguage
            tvReligion.text = country.religion
            tvName.text = country.name

            ivRead.setImageResource(
                if (country.read)
                    R.drawable.green_flag
                else
                    R.drawable.red_flag
            )

            Picasso.get()
                .load(File(country.map))
                .error(R.drawable.world)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)
        }

}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_pager, parent, false)
        )
    }

    override fun getItemCount() = countries.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val country = countries[position]
            val _id: String = country._id.toString()

            holder.ivRead.setOnLongClickListener{
                country.read = !country.read

                context.contentResolver.update(
                    ContentUris.withAppendedId(COUNTRY_PROVIDER_CONTENT_URI, country._id!!),
                    ContentValues().apply {
                        put(Country::read.name, country.read)
                    },
                    "_id=?", arrayOf(_id)
            )
            notifyItemChanged(position)
            true
        }
            holder.bind(country)
        }
    }