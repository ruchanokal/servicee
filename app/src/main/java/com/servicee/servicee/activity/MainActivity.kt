package com.servicee.servicee.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.servicee.servicee.R
import com.servicee.servicee.databinding.ActivityMainBinding
import com.servicee.servicee.fragments.MainFragment
import com.servicee.servicee.fragments.RandevularimFragment

class MainActivity : AppCompatActivity() {

    private var binding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        val intent = intent
        val definite = intent.getIntExtra("definite",0)

        if (definite == 3 || definite == 4){
            val menu = binding!!.bottomNavView.menu.findItem(R.id.profilimFragment)
            menu?.title = "Profilim"
        }

        val navController = this.findNavController(R.id.fragmentContainerView)
        binding!!.bottomNavView.setupWithNavController(navController)
    }



}