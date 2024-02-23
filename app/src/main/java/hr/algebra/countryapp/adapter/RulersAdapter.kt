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
import hr.algebra.countryapp.POSITION
import hr.algebra.countryapp.POSITIONRULER
import hr.algebra.countryapp.R
import hr.algebra.countryapp.RULER_PROVIDER_CONTENT_URI
import hr.algebra.countryapp.RulerPagerActivity
import hr.algebra.countryapp.api.model.Country
import hr.algebra.countryapp.api.model.Monument
import hr.algebra.countryapp.api.model.Ruler
import hr.algebra.countryapp.framework.startActivity
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class RulersAdapter(
    private val context: Context,
    private val rulers: MutableList<Ruler>,

) : RecyclerView.Adapter<RulersAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val tvName = itemView.findViewById<TextView>(R.id.tvName)

        fun bind(ruler: Ruler) {
            tvName.text = ruler.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.ruler, parent, false)
        )
    }

    override fun getItemCount() = rulers.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.setOnLongClickListener{
            showDeleteConfirmationDialog(position)
            true
        }

        holder.itemView.setOnClickListener{
            //edit
            context.startActivity<RulerPagerActivity>(POSITIONRULER, position)
        }
        holder.bind(rulers[position])
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Potvrda brisanja")
            .setMessage("Brisanje vladara?")
            .setPositiveButton("Da") { dialog, _ ->
                deleteRuler(position)
                dialog.dismiss()
            }
            .setNegativeButton("Ne") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteRuler(position: Int) {
        val ruler = rulers[position]
        val _id: String = ruler._id.toString()

        context.contentResolver.delete(
            ContentUris.withAppendedId(RULER_PROVIDER_CONTENT_URI, ruler._id!!),
            "_id=?",
            arrayOf(_id)
        )
        rulers.removeAt(position)
        notifyDataSetChanged()
    }
}