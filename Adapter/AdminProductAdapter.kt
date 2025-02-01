package com.example.woodscraft.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.woodscraft.Activity.ProductActivity
import com.example.woodscraft.Authentication.AuthInterceptor
import com.example.woodscraft.DataModels.DeleteProductResponse
import com.example.woodscraft.DataModels.Product
import com.example.woodscraft.R
import com.example.woodscraft.Routes.ApiService
import com.example.woodscraft.URLs.RetrofitInstance
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AdminProductAdapter(private val productList: MutableList<Product>): RecyclerView.Adapter<AdminProductAdapter.ProductViewHolder>() {
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImag)
        val productName: TextView = itemView.findViewById(R.id.titleText)
        val productSubtitle: TextView = itemView.findViewById(R.id.discrptionText)
        val productRating: TextView = itemView.findViewById(R.id.ratingText)
        val productMrp: TextView = itemView.findViewById(R.id.mrpText)
        val productPrice: TextView = itemView.findViewById(R.id.currentText)
        val productUpdate: Button = itemView.findViewById(R.id.updateBtn)
        val productAddImg: Button = itemView.findViewById(R.id.addImgBtn)
        val productDelete: Button = itemView.findViewById(R.id.deleteBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int =productList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        Glide.with(holder.itemView.context)
            .load(product.images[0])
            .timeout(1000)
            .placeholder(R.drawable.progress_png) // Show while loading
            .error(R.drawable.image_not_lode) // Show if loading fails
            .into(holder.productImage)

        holder.productName.text = product.name
        holder.productSubtitle.text = product.description
        holder.productRating.text = product.rating.toString()
        holder.productMrp.text = "M.R.P.: ₹ ${product.price.wasPrice}"
        holder.productPrice.text = "₹ ${product.price.currentPrice}"



        holder.productUpdate.setOnClickListener{
            updateProduct(holder.itemView.context, product.productId){
                    success ->
                if (success){
                    notifyItemRemoved(position)
                } else {
                    println("Product was not Added to Cart LikeProductAdapter")
                }
            }
        }
        holder.productAddImg.setOnClickListener {
            addImages(holder.itemView.context, product.productId){
                    success ->
                if (success){
                    notifyItemRemoved(position)
                } else {
                    println("Product was not Added to Cart LikeProductAdapter")
                }
            }
        }
        holder.productDelete.setOnClickListener {
            deleteProduct(holder.itemView.context, product.productId){
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

    private fun addImages(context: Context, productId: String?, callback: (Boolean) -> Unit) {
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


    }

    private fun deleteProduct(context: Context, productId: String?,callback: (Boolean) -> Unit) {
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
            apiServiceWithInterceptor.deleteProduct(productId).enqueue(object : retrofit2.Callback<DeleteProductResponse> {
                override fun onResponse(
                    p0: Call<DeleteProductResponse>,
                    p1: Response<DeleteProductResponse>
                ) {
                    if (p1.isSuccessful && p1.body() != null) {
                        println("Product Deleted")
                        callback(true)
                        val productToRemove = productList.find { it.productId == productId }
                        if (productToRemove != null) {
                            val indexToRemove = productList.indexOf(productToRemove)
                            productList.removeAt(indexToRemove)
                            notifyItemRemoved(indexToRemove) // Notify adapter of removal
                        }
                    } else {
                        callback(false)
                    }
                }

                override fun onFailure(p0: Call<DeleteProductResponse>, p1: Throwable) {
                    Log.e("AdminProductAdapter", "Deleting Product From Your Product failed: ${p1.message}", p1)

                }

            })
        }
    }

    private fun updateProduct(context: Context, productId: String?,callback: (Boolean) -> Unit) {

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
    }
}