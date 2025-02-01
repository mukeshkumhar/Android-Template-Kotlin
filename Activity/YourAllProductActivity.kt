package com.example.woodscraft.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.woodscraft.Adapter.AdminProductAdapter
import com.example.woodscraft.Authentication.AuthInterceptor
import com.example.woodscraft.DataModels.ListOfProduct
import com.example.woodscraft.DataModels.Product
import com.example.woodscraft.R
import com.example.woodscraft.Routes.ApiService
import com.example.woodscraft.URLs.RetrofitInstance
import com.example.woodscraft.databinding.ActivityYourAllProductBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class YourAllProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityYourAllProductBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminProductAdapter
    private val productList = mutableListOf<Product>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityYourAllProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = findViewById<RecyclerView>(R.id.my_product_recycle_view)
//        recyclerView.layoutManager = GridLayoutManager(context, 1)
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        binding.backBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragmentToLoad", "profile")
            startActivity(intent)
            finish()
        }

        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
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

        apiServiceWithInterceptor.getAllProduct().enqueue(object : Callback<ListOfProduct> {
            override fun onResponse(p0: Call<ListOfProduct>, p1: Response<ListOfProduct>) {
                if (p1.isSuccessful && p1.body() != null){
                    val products = p1.body()?.data // Assuming your ListOfProduct has a property named "products"
                    productList.clear()
                    if (products != null) {
                        productList.addAll(products)
                    } else {
                        println("product will be null")
                    }
//                    adapter.notifyDataSetChanged()
                    println(productList)
                    val Adapter = AdminProductAdapter(productList)
                    recyclerView.adapter = Adapter

                } else {
                    println("Getting product failed")
                }
            }

            override fun onFailure(p0: Call<ListOfProduct>, p1: Throwable) {
                Log.e("HomeFragment", "Getting AllProduct failed: ${p1.message}", p1)
                Toast.makeText(this@YourAllProductActivity, "Getting AllProduct failed due to network error", Toast.LENGTH_SHORT).show()

            }

        })
    }
}