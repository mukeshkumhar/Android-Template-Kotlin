package com.example.woodscraft.Adapter

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.woodscraft.Activity.ProductActivity
import com.example.woodscraft.DataModels.Product
import com.example.woodscraft.R
import javax.sql.DataSource

class AllProductAdapter(private val productList: List<Product>) : RecyclerView.Adapter<AllProductAdapter.ProductViewHolder>() {
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productName: TextView = itemView.findViewById(R.id.productTitle)
        val productSubtitle: TextView = itemView.findViewById(R.id.productSubtitle)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int =productList.size


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        Glide.with(holder.itemView.context)
            .load(product.images[0])
            .timeout(10000)
            .listener(object : RequestListener<Drawable> {

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    e?.logRootCauses("GlideLog")
                    return false
                }

            })
            .placeholder(R.drawable.progress_png) // Show while loading
            .error(R.drawable.image_not_lode) // Show if loading fails
            .into(holder.productImage)

        holder.productName.text = product.name
        holder.productSubtitle.text = product.summery
        holder.productPrice.text = "₹ ${product.price.currentPrice}"

        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            val intent = Intent(context, ProductActivity::class.java).apply {
                putExtra("productId", product.productId)
                putExtra("productName", product.name)
                putExtra("productImage", product.images[0])
                putExtra("productDes", product.description)
                putExtra("productSubtitle", product.summery)
                putExtra("productRating", product.rating.toString())
                putExtra("productPrice", product.price.currentPrice.toString())

            }
            context.startActivity(intent)
        }
    }
}