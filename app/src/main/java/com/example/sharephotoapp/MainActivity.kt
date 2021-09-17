package com.example.sharephotoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sharephotoapp.databinding.ActivityMainBinding

private lateinit var binding : ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(this , R.id.fragmentContainerView)
        binding.bottomNavMenu.setupWithNavController(navController)
        
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id){
                R.id.feedFragment -> showBottomNavMenu()
                R.id.settingsFragment -> showBottomNavMenu()
                R.id.shareFragment -> showBottomNavMenu()

                else -> hideBottomNavMenu()
            }
        }

    }

    private fun showBottomNavMenu(){
        binding.bottomNavMenu.visibility = View.VISIBLE
    }

    private fun hideBottomNavMenu(){
        binding.bottomNavMenu.visibility = View.GONE
    }
}