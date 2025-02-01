package com.example.woodscraft.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.woodscraft.CartFragment
import com.example.woodscraft.HomeFragment
import com.example.woodscraft.LikeFragment
import com.example.woodscraft.ProfileFragment
import com.example.woodscraft.R
import com.example.woodscraft.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bottomNavigationView = binding.bottomNavigation

        bottomNavigationView.setOnItemSelectedListener {
            menuItem ->
            when(menuItem.itemId){
             R.id.bottom_home -> {
                 replaceFragment(HomeFragment())
                 bottomNavigationView.menu.findItem(R.id.bottom_home).iconTintList = ContextCompat.getColorStateList(this,
                     R.color.dark_brawn
                 )

                 bottomNavigationView.menu.findItem(R.id.bottom_like).iconTintList = ContextCompat.getColorStateList(this,
                     R.color.light_brawn
                 )
                 bottomNavigationView.menu.findItem(R.id.bottom_cart).iconTintList = ContextCompat.getColorStateList(this,
                     R.color.light_brawn
                 )
                 bottomNavigationView.menu.findItem(R.id.bottom_profile).iconTintList = ContextCompat.getColorStateList(this,
                     R.color.light_brawn
                 )
                 true
             }
                R.id.bottom_like -> {
                    replaceFragment(LikeFragment())

                    bottomNavigationView.menu.findItem(R.id.bottom_home).iconTintList = ContextCompat.getColorStateList(this,
                        R.color.light_brawn
                    )

                    bottomNavigationView.menu.findItem(R.id.bottom_like).iconTintList = ContextCompat.getColorStateList(this,
                        R.color.dark_brawn
                    )
                    bottomNavigationView.menu.findItem(R.id.bottom_cart).iconTintList = ContextCompat.getColorStateList(this,
                        R.color.light_brawn
                    )
                    bottomNavigationView.menu.findItem(R.id.bottom_profile).iconTintList = ContextCompat.getColorStateList(this,
                        R.color.light_brawn
                    )

                    true
                }

                R.id.bottom_cart -> {
                    replaceFragment(CartFragment())

                    bottomNavigationView.menu.findItem(R.id.bottom_home).iconTintList = ContextCompat.getColorStateList(this,
                        R.color.light_brawn
                    )

                    bottomNavigationView.menu.findItem(R.id.bottom_like).iconTintList = ContextCompat.getColorStateList(this,
                        R.color.light_brawn
                    )
                    bottomNavigationView.menu.findItem(R.id.bottom_cart).iconTintList = ContextCompat.getColorStateList(this,
                        R.color.dark_brawn
                    )
                    bottomNavigationView.menu.findItem(R.id.bottom_profile).iconTintList = ContextCompat.getColorStateList(this,
                        R.color.light_brawn
                    )

                    true
                }

                R.id.bottom_profile -> {
                    replaceFragment(ProfileFragment())

                    bottomNavigationView.menu.findItem(R.id.bottom_home).iconTintList = ContextCompat.getColorStateList(this,
                        R.color.light_brawn
                    )

                    bottomNavigationView.menu.findItem(R.id.bottom_like).iconTintList = ContextCompat.getColorStateList(this,
                        R.color.light_brawn
                    )
                    bottomNavigationView.menu.findItem(R.id.bottom_cart).iconTintList = ContextCompat.getColorStateList(this,
                        R.color.light_brawn
                    )
                    bottomNavigationView.menu.findItem(R.id.bottom_profile).iconTintList = ContextCompat.getColorStateList(this,
                        R.color.dark_brawn
                    )

                    true
                }

                else -> false
            }
        }

        val fragmentToLoad = intent.getStringExtra("fragmentToLoad")
        if (fragmentToLoad == "profile") {
            replaceFragment(ProfileFragment())
            bottomNavigationView.selectedItemId = R.id.bottom_profile
        } else {
            replaceFragment(HomeFragment())
        }
//        replaceFragment(HomeFragment())
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }
}