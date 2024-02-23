package hr.algebra.countryapp.adapter

import android.content.ContentUris
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.countryapp.COUNTRY_PROVIDER_CONTENT_URI
import hr.algebra.countryapp.ItemPagerActivity
import hr.algebra.countryapp.MONUMENT_PROVIDER_CONTENT_URI
import hr.algebra.countryapp.MonumentPagerActivity
import hr.algebra.countryapp.POSITION
import hr.algebra.countryapp.POSITIONMONUMENT
import hr.algebra.countryapp.R
import hr.algebra.countryapp.RULER_PROVIDER_CONTENT_URI
import hr.algebra.countryapp.api.model.Country
import hr.algebra.countryapp.api.model.Monument
import hr.algebra.countryapp.api.model.Ruler
import hr.algebra.countryapp.framework.startActivity
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class MonumentsAdapter(
    private val context: Context,
    private val monuments: MutableList<Monument>,

) : RecyclerView.Adapter<MonumentsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val tvName = itemView.findViewById<TextView>(R.id.tvName)

        fun bind(monument: Monument) {
            tvName.text = monument.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.monument, parent, false)
        )
    }

    override fun getItemCount() = monuments.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.setOnLongClickListener{
            showDeleteConfirmationDialog(position)
            true
        }

        holder.itemView.setOnClickListener{
            //edit
            context.startActivity<MonumentPagerActivity>(POSITIONMONUMENT, position)
        }
        holder.bind(monuments[position])
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Potvrda brisanja")
            .setMessage("Brisanje spomenika?")
            .setPositiveButton("Da") { dialog, _ ->
                deleteMonument(position)
                dialog.dismiss()
            }
            .setNegativeButton("Ne") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteMonument(position: Int) {
        val monument = monuments[position]
        val _id: String = monument._id.toString()

        context.contentResolver.delete(
            ContentUris.withAppendedId(MONUMENT_PROVIDER_CONTENT_URI, monument._id!!),
            "_id=?",
            arrayOf(_id)
        )
        monuments.removeAt(position)
        notifyDataSetChanged()
    }
}