package com.daniher.ejerciciosprcticos

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.daniher.ejerciciosprcticos.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentFragment: Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setUpBottomNav()
        val navController = findNavController(R.id.nav_host)
        binding.navView.setupWithNavController(navController)
    }

    private fun setUpBottomNav() {
        val homeFragment = HomeFragment()
        val favouriteFragment = FavouriteFragment()
        currentFragment = homeFragment

        with(supportFragmentManager) {
            beginTransaction()
                .add(R.id.nav_host, favouriteFragment)
                .hide(favouriteFragment).commit()
            beginTransaction()
                .add(R.id.nav_host, homeFragment)
                .commit()

            binding.navView.setOnItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.navigation_home -> {
                    beginTransaction().hide(currentFragment).show(homeFragment).commit()
                        currentFragment = homeFragment
                    }
                    R.id.navigation_favourite -> {
                    beginTransaction().hide(currentFragment).show(favouriteFragment).commit()
                        currentFragment = favouriteFragment
                    }
                }
                true
            }
        }
    }
}