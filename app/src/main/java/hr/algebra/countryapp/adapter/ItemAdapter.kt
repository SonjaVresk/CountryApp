package hr.algebra.countryapp.adapter

import android.content.ContentUris
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.countryapp.COUNTRY_PROVIDER_CONTENT_URI
import hr.algebra.countryapp.ItemPagerActivity
import hr.algebra.countryapp.POSITION
import hr.algebra.countryapp.R
import hr.algebra.countryapp.api.model.Country
import hr.algebra.countryapp.api.model.Monument
import hr.algebra.countryapp.api.model.Ruler
import hr.algebra.countryapp.fragment.UpdateCountryFragment
import hr.algebra.countryapp.framework.startActivity
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class ItemAdapter(
    private val context: Context,
    private val countries: MutableList<Country>,

) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val ivEdit = itemView.findViewById<ImageView>(R.id.ivEdit)

        fun bind(country: Country) {
            tvTitle.text = country.name
            Picasso.get()
                .load(File(country.map))
                .error(R.drawable.old_globe)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)

            //Update
            ivEdit.setOnClickListener {
                val fragmentManager = (itemView.context as AppCompatActivity).supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()

                val updateCountryFragment = UpdateCountryFragment()

                val args = Bundle()
                args.putLong("countryId", country._id!!)
                updateCountryFragment.arguments = args

                fragmentTransaction.replace(R.id.ItemsFragment, updateCountryFragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        )
    }

    override fun getItemCount() = countries.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.setOnLongClickListener{
            showDialog(position)
            true
        }

        holder.itemView.setOnClickListener{
            context.startActivity<ItemPagerActivity>(POSITION, position)
        }
        holder.bind(countries[position])

    }

    private fun showDialog(position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Potvrda brisanja")
            .setMessage("Obrisati državu?")
            .setPositiveButton("Da") { dialog, _ ->
                deleteCountry(position)
                dialog.dismiss()
            }
            .setNegativeButton("Odustani") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteCountry(position: Int) {
        val country = countries[position]
        val _id: String = country._id.toString()
        context.contentResolver.delete(
            ContentUris.withAppendedId(COUNTRY_PROVIDER_CONTENT_URI, country._id!!),
            "_id=?",
            arrayOf(_id)
        )
        File(country.map).delete()
        countries.removeAt(position)
        notifyDataSetChanged()
    }


}