package com.example.woodscraft.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.woodscraft.R

class ImageSliderAdapter(private val images: List<Int>) :
RecyclerView.Adapter<ImageSliderAdapter.SliderViewHolder>(){

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageSlideView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_slider,parent,false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.imageView.setImageResource(images[position])
    }
    override fun getItemCount(): Int {
        return images.size
    }

}