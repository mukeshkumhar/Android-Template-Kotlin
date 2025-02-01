package com.example.woodscraft.LoginActivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.woodscraft.Activity.MainActivity
import com.example.woodscraft.ApiManager.ApiManagers
import com.example.woodscraft.DataModels.LoginResponse
import com.example.woodscraft.DataModels.LoginUser
import com.example.woodscraft.R
import com.example.woodscraft.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("AccessToken", null)

        binding.registerBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        if ( accessToken != null){
            Toast.makeText(this,"You logged in", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this,"You are not logged in", Toast.LENGTH_SHORT).show()
        }

        binding.signUpBtn.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            val userLogin = LoginUser(email, password)

            ApiManagers.authService.loginUser(userLogin).enqueue(object : retrofit2.Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null){
                        val loginResponse = response.body()
                        println(loginResponse)
                        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("AccessToken", response.body()?.data?.AccessToken)
                        editor.apply()

                        val userLoginSave = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                        val userEditor = userLoginSave.edit()
                        userEditor.putString("user_id",response.body()?.data?.User?._id)
                        userEditor.putString("user_fullname",response.body()?.data?.User?.fullName)
                        userEditor.putString("user_age",response.body()?.data?.User?.age.toString())
                        userEditor.putString("user_email",response.body()?.data?.User?.email)
                        userEditor.putString("user_address",response.body()?.data?.User?.address)
                        userEditor.putBoolean("user_admin",response.body()?.data?.User?.admin!!)
                        userEditor.apply()

                        val login = response.body()?.message
                        Toast.makeText(this@LoginActivity,login, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()

                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("LoginActivity", "Login failed: $errorBody")
                        Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("LoginActivity onFailure", "Login failed: ${t.message}", t)
                    Toast.makeText(this@LoginActivity, "Login failed due to network error", Toast.LENGTH_SHORT).show()
                }


            })
        }
    }
}