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
import com.example.woodscraft.DataModels.RegisterResponse
import com.example.woodscraft.DataModels.RegisterUser
import com.example.woodscraft.R
import com.example.woodscraft.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.signUpBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.registerBtn.setOnClickListener {
            val fullName = binding.registerFullname.text.toString()
            val email = binding.registerEmail.text.toString()
            val age = binding.registerAge.text.toString().toInt()
            val address = binding.registerAddress.text.toString()
            val password = binding.loginPassword.text.toString()
            val registerRequest = RegisterUser(fullName, email, age, address, password)


            if(age >= 16){
                ApiManagers.authService.registerUser(registerRequest).enqueue(object : retrofit2.Callback<RegisterResponse> {
                   override fun onResponse(
                       call: Call<RegisterResponse>,
                       response: Response<RegisterResponse>
                   ){
                       if (response.isSuccessful  && response.body() != null){
                           val accessToken = response.body()?.data?.AccessToken
                           val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                           val editor = sharedPreferences.edit()
                           editor.putString("AccessToken", accessToken)
                           editor.apply()

                           val userRegisterSave = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                           val userEditor = userRegisterSave.edit()
                           userEditor.putString("user_id",response.body()?.data?.User?._id)
                           userEditor.putString("user_fullname",response.body()?.data?.User?.fullName)
                           userEditor.putString("user_email",response.body()?.data?.User?.email)
                           userEditor.putString("user_age", response.body()?.data?.User?.age.toString())
                           userEditor.apply()

                           Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                           startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                           finish()

                       } else{
                           val errorBody = response.errorBody()?.string()
                           Log.e("RegistrationActivity", "Login failed: $errorBody")
                           Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                       }
                   }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Log.e("RegisterActivity onFailure", "Registration failed: ${t.message}", t)
                        Toast.makeText(this@RegisterActivity, "Registration failed due to network error", Toast.LENGTH_SHORT).show()
                    }
                })
            }


        }
    }
}
