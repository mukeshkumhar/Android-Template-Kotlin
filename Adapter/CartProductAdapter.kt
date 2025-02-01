package com.example.woodscraft.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.woodscraft.Authentication.AuthInterceptor
import com.example.woodscraft.DataModels.CartProduct
import com.example.woodscraft.DataModels.RemoveFromCartResponse
import com.example.woodscraft.R
import com.example.woodscraft.Routes.ApiService
import com.example.woodscraft.URLs.RetrofitInstance
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CartProductAdapter(private val productList: MutableList<CartProduct>): RecyclerView.Adapter<CartProductAdapter.ProductViewHolder>() {
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.cardImg)
        val productTitle: TextView = itemView.findViewById(R.id.titleCard)
        val productSubtitle: TextView = itemView.findViewById(R.id.cardDis)
        val productRating: TextView = itemView.findViewById(R.id.cartRating)
        val productPriceOld: TextView = itemView.findViewById(R.id.priceOld)
        val productPriceNew: TextView = itemView.findViewById(R.id.peiceT)
        val crossBtn: ImageView = itemView.findViewById(R.id.crossBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        Glide.with(holder.itemView.context)
            .load(product.images[0])
            .into(holder.productImage)
        holder.productTitle.text = product.name
        holder.productSubtitle.text = product.description
        holder.productRating.text = product.rating.toString()
        holder.productPriceOld.text = "M.R.P.: ₹ ${product.price.wasPrice}"
        holder.productPriceNew.text = "₹ ${product.price.currentPrice}"

        holder.crossBtn.setOnClickListener {
            removeProductFromCart(holder.itemView.context, product.productId) { success ->
                if (success) {
                    notifyItemRemoved(position)
                } else {
                    println("Product was not removed from wishlist LikeProductAdapter")
                }
            }
        }
    }

    private fun removeProductFromCart(
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
            apiServiceWithInterceptor.removeFromCart(productId).enqueue(object :
                Callback<RemoveFromCartResponse> {
                override fun onResponse(
                    p0: Call<RemoveFromCartResponse>,
                    p1: Response<RemoveFromCartResponse>
                ) {
                    if (p1.isSuccessful && p1.body() != null){
                        callback(true)
                        val productToRemove = productList.find { it.productId == productId }
                        if (productToRemove != null) {
                            val indexToRemove = productList.indexOf(productToRemove)
                            productList.removeAt(indexToRemove)
                            notifyItemRemoved(indexToRemove)
                        } else{
                            callback(false)
                        }
                    }
                }

                override fun onFailure(p0: Call<RemoveFromCartResponse>, p1: Throwable) {
                    Log.e("CartProductAdapter", "Removing Product From Cart failed: ${p1.message}", p1)
                }
            })
        }
    }
}