package com.example.woodscraft.Activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.woodscraft.Authentication.AuthInterceptor
import com.example.woodscraft.DataModels.ProductItemResponse
import com.example.woodscraft.R
import com.example.woodscraft.Routes.ApiService
import com.example.woodscraft.URLs.RetrofitInstance
import com.example.woodscraft.databinding.ActivityAddProductBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit
import kotlin.text.substringAfterLast

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding

    private lateinit var imageUri: Uri  // Store the selected image URI
    private lateinit var getContent: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backBtn.setOnClickListener {
            // Check if the Profile fragment is already in the back stack
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragmentToLoad", "profile")
            startActivity(intent)
            finish()
        }


        getContent = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                imageUri = uri
                binding.productJpg.setImageURI(uri) // Display image in ImageView
                val fileName = imageUri.path?.substringAfterLast('/')
                println(imageUri)
                println(fileName)
                binding.imageName.text = fileName // Display image name in TextView
            }
        }
        binding.productJpg.setOnClickListener {
            getContent.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        binding.titleTxt.setOnClickListener {
            showEditTextDialog("Enter Title",binding.titleTxt)
        }
        binding.disTxt.setOnClickListener {
            showEditTextDialog("Enter Description",binding.disTxt)
        }
        binding.summeryTxt.setOnClickListener {
            showEditTextDialog("Enter Summery",binding.summeryTxt)
        }
        binding.productId.setOnClickListener {
            showEditTextDialog("Enter Product Id",binding.productId)
        }
        binding.ratingTxt.setOnClickListener {
            showEditTextDialog("Enter Rating",binding.ratingTxt)
        }
        binding.mrpTxt.setOnClickListener {
            showEditTextDialog("Enter MRP",binding.mrpTxt)
        }
        binding.currentPrice.setOnClickListener {
            showEditTextDialog("Enter Current Price",binding.currentPrice)
        }






        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val authInterceptor = AuthInterceptor(sharedPreferences)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS) // Connect timeout
            .readTimeout(60, TimeUnit.SECONDS)    // Read timeout
            .writeTimeout(60, TimeUnit.SECONDS)   // Write timeout
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitInstance.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val apiServiceWithInterceptor = retrofit.create(ApiService::class.java)



        binding.addBtn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            val productName = binding.titleTxt.text.toString()
            val productDescription = binding.disTxt.text.toString()
            val productSummery = binding.summeryTxt.text.toString()
            val productId = binding.productId.text.toString()
            val productRating = binding.ratingTxt.text.toString()
            val productMrp = binding.mrpTxt.text.toString()
            val productPrice = binding.currentPrice.text.toString()



            val productIdBody = productId.toRequestBody("text/plain".toMediaTypeOrNull())
            val nameBody = productName.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionBody = productDescription.toRequestBody("text/plain".toMediaTypeOrNull())
            val summeryBody = productSummery.toRequestBody("text/plain".toMediaTypeOrNull())
            val oldPriceBody = productMrp.toRequestBody("text/plain".toMediaTypeOrNull())
            val newPriceBody = productPrice.toRequestBody("text/plain".toMediaTypeOrNull())
            val ratingBody = productRating.toRequestBody("text/plain".toMediaTypeOrNull())


            val filesDir = applicationContext.filesDir
            val file = File(filesDir, "image.jpg")
            val inputStream = contentResolver.openInputStream(imageUri)
            val outPutStream = FileOutputStream(file)
            inputStream!!.copyTo(outPutStream)
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val images = MultipartBody.Part.createFormData("images", file.name, requestBody)





            apiServiceWithInterceptor.addProduct(images,productIdBody,nameBody,descriptionBody,summeryBody,oldPriceBody,newPriceBody,ratingBody).enqueue(object: Callback<ProductItemResponse>{
                override fun onResponse(
                    p0: Call<ProductItemResponse>,
                    p1: Response<ProductItemResponse>
                ) {
                    if (p1.isSuccessful && p1.body() != null){
                        println(p1.body())
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@AddProductActivity,"New Product Added",Toast.LENGTH_SHORT).show()
                        binding.titleTxt.text = ""
                        binding.disTxt.text= ""
                        binding.summeryTxt.text = ""
                        binding.productId.text = ""
                        binding.ratingTxt.text = ""
                        binding.mrpTxt.text = ""
                        binding.currentPrice.text = ""
                        binding.productJpg.setImageURI(null)
                    } else {
                        println("Adding New Product failed not getting response")
                    }
                }

                override fun onFailure(p0: Call<ProductItemResponse>, p1: Throwable) {
                    Log.e("AddProductActivity", "Adding New Product failed: ${p1.message}", p1)
                    Toast.makeText(this@AddProductActivity, "Failed to Add New Product", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE

                }

            })

        }
    }

    private fun showEditTextDialog(title: String, editText: TextView) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)

//        // Inflate custom layout
//        val dialogView = layoutInflater.inflate(R.layout.dialog_edittext, null)
//        builder.setView(dialogView)
//
//        val input = dialogView.findViewById<EditText>(R.id.dialogEditText)
//        input.setText(editText.text.toString())

        // Get the CardView (root layout)
//        val cardView = dialogView.findViewById<CardView>(R.id.MaterialCardView)
//
//        cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.dark_brawn1))
//        cardView.radius = 8f

        val input = EditText(this)
        input.setText(editText.text.toString()) // Set initial text
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val newText = input.text.toString()
            editText.setText(newText) // Update the original EditText
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}