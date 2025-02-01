package com.example.woodscraft.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.woodscraft.Activity.ProductActivity
import com.example.woodscraft.Authentication.AuthInterceptor
import com.example.woodscraft.DataModels.AddToCartResponse
import com.example.woodscraft.DataModels.LikedProduct
import com.example.woodscraft.DataModels.RemoveWishlistResponse
import com.example.woodscraft.R
import com.example.woodscraft.Routes.ApiService
import com.example.woodscraft.URLs.RetrofitInstance
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.remove

class LikeProductAdapter(private val productList: MutableList<LikedProduct>): RecyclerView.Adapter<LikeProductAdapter.ProductViewHolder>() {
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val productImage: ImageView = itemView.findViewById(R.id.productImag)
        val productTitle: TextView = itemView.findViewById(R.id.titleText)
        val productSubtitle: TextView = itemView.findViewById(R.id.discrptionText)
        val productRating: TextView = itemView.findViewById(R.id.ratingText)
        val productPrice: TextView = itemView.findViewById(R.id.priceText)
        val likeBtn: ImageView = itemView.findViewById(R.id.likeBtn)
        val buyBtn: TextView = itemView.findViewById(R.id.buyBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.like_items, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int= productList.size


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        Glide.with(holder.itemView.context)
            .load(product.images[0])
            .into(holder.productImage)
        holder.productTitle.text = product.name
        holder.productSubtitle.text = product.description
        holder.productRating.text = product.rating.toString()
        holder.productPrice.text = "₹ ${product.price.currentPrice}"

        holder.likeBtn.setOnClickListener{
            removeProductFromWishlist(holder.itemView.context,product.productId){
                success ->
                if (success){
                    notifyItemRemoved(position)
                } else {
                    println("Product was not removed from wishlist LikeProductAdapter")
                }
            }
        }

        holder.buyBtn.setOnClickListener{
            addToCart(holder.itemView.context, product.productId){
                    success ->
                if (success){
                    notifyItemRemoved(position)
                } else {
                    println("Product was not Added to Cart LikeProductAdapter")
                }
            }
        }

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

    private fun addToCart(context: Context, productId: String?,callback: (Boolean) -> Unit) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val authInterceptor = AuthInterceptor(sharedPreferences)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitInstance.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val apiServiceWithInterceptor = retrofit.create(ApiService::class.java)

        if (productId != null) {
            apiServiceWithInterceptor.addToCart(productId).enqueue(object : Callback<AddToCartResponse>{
                override fun onResponse(
                    p0: Call<AddToCartResponse>,
                    p1: Response<AddToCartResponse>
                ) {
                    if (p1.isSuccessful && p1.body() != null) {
                        callback(true)
                    } else {
                        callback(false)
                    }
                }

                override fun onFailure(p0: Call<AddToCartResponse>, p1: Throwable) {
                    Log.e("LikeProductAdapter", "Adding Product From Cart failed: ${p1.message}", p1)
                }

            })
        }
    }

// Remove the product from wishlist
    private fun removeProductFromWishlist(
        context: Context,
        productId: String?,
        callback: (Boolean) -> Unit
    ) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val authInterceptor = AuthInterceptor(sharedPreferences)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitInstance.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val apiServiceWithInterceptor = retrofit.create(ApiService::class.java)

        if (productId != null) {
            apiServiceWithInterceptor.removeFromWishlist(productId).enqueue(object : Callback<RemoveWishlistResponse> {
                override fun onResponse(
                    p0: Call<RemoveWishlistResponse>,
                    p1: Response<RemoveWishlistResponse>
                ) {
                    if (p1.isSuccessful && p1.body() != null) {
                        callback(true)
                        val productToRemove = productList.find { it.productId == productId }
                        if (productToRemove != null) {
                            val indexToRemove = productList.indexOf(productToRemove)
                            productList.removeAt(indexToRemove)
                            notifyItemRemoved(indexToRemove) // Notify adapter of removal
                        }
                    } else{
                        callback(false)
                    }
                }

                override fun onFailure(p0: Call<RemoveWishlistResponse>, p1: Throwable) {
                    Log.e("LikeProductAdapter", "Removing Product From Wishlist failed: ${p1.message}", p1)
                }

            })
        }

    }
}