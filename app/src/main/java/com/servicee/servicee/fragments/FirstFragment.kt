package com.servicee.servicee.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.servicee.servicee.R
import com.servicee.servicee.activity.MainActivity
import com.servicee.servicee.databinding.FragmentFirstBinding


class FirstFragment : Fragment() {

    private var binding: FragmentFirstBinding? = null
    private lateinit var mAuth : FirebaseAuth
    private lateinit var user : FirebaseUser
    var girisSekli = ""
    var value = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstBinding.inflate(inflater,container,false)
        val view = binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

        val prefences = requireActivity().getSharedPreferences("com.servicee.servicee", Context.MODE_PRIVATE)

        hizliGiris(prefences)


        binding!!.kullaniciGirisiButton.setOnClickListener {

            val action = FirstFragmentDirections.actionFirstFragmentToUserSignInFr()
            Navigation.findNavController(it).navigate(action)
            prefences.edit().putString("giris","kullanici").apply()

        }

        binding!!.servisGirisiButton.setOnClickListener {

            val action = FirstFragmentDirections.actionFirstFragmentToServiceSignInFr()
            Navigation.findNavController(it).navigate(action)
            prefences.edit().putString("giris","servis").apply()

        }

    }


    private fun hizliGiris(prefences  : SharedPreferences) {
        if (mAuth.currentUser != null ) {

            girisSekli = prefences.getString("giris","")!!

            if (girisSekli.equals("kullanici")) {
                value = 2
            } else if (girisSekli.equals("servis")){
                value = 4
            } else
                value = 0

            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("definite",value)
            startActivity(intent)
            requireActivity().finish()

        }
    }
}