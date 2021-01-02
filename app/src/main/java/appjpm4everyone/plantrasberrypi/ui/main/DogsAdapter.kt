package appjpm4everyone.plantrasberrypi.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import appjpm4everyone.plantrasberrypi.databinding.ItemDogBinding
import appjpm4everyone.plantrasberrypi.utils.fromUrl
import kotlinx.android.synthetic.main.item_dog.view.*

class DogsAdapter (val images: List<String>) : RecyclerView.Adapter<DogsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = images[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemDogsBinding = ItemDogBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemDogsBinding.root)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(image: String) {
            //Picasso
            itemView.ivDog.fromUrl(image)
        }
    }
}