package com.servicee.servicee.fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.servicee.servicee.R
import com.servicee.servicee.adapter.HizmetlerAdapter
import com.servicee.servicee.databinding.FragmentProfilimBinding
import com.servicee.servicee.model.ServisDetails
import com.servicee.servicee.util.Constants.Companion.defaultScore
import com.servicee.servicee.util.Constants.Companion.kullaniciGirisi
import com.servicee.servicee.util.Constants.Companion.servis
import com.servicee.servicee.util.Constants.Companion.servisGirisi


class ProfilimFragment : Fragment() {

    private var binding : FragmentProfilimBinding? = null
    private lateinit var db : FirebaseFirestore
    private val TAG = "ProfilimFragment"
    private var hizmetlerList = arrayListOf<ServisDetails>()
    private var adapter : HizmetlerAdapter? = null
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    val phoneNumber = "+908505555555"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilimBinding.inflate(inflater,container,false)
        val view = binding!!.root
        registerLauncher()
        return view
    }

    private fun registerLauncher() {

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // İzin verildi, arama yap

                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:$phoneNumber")
                startActivity(intent)
            } else {
                // İzin verilmedi, kullanıcıya bilgi ver
                Toast.makeText(requireContext(), "Telefon arama izni verilmedi", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        if (girisType.equals(kullaniciGirisi)) {

            binding!!.kullaniciLayout.visibility = View.VISIBLE
            binding!!.servisLayout.visibility = View.GONE

            binding!!.araButtonLayout.setOnClickListener {

                if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),android.Manifest.permission.CALL_PHONE)){
                        Snackbar.make(view,"Arama yapabilmek için izin vermelisin", Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver") {
                            requestPermissionLauncher.launch(android.Manifest.permission.CALL_PHONE)
                        }.show()
                    }else {
                        requestPermissionLauncher.launch(android.Manifest.permission.CALL_PHONE)
                    }
                } else {
                    requestPermissionLauncher.launch(android.Manifest.permission.CALL_PHONE)
                }

            }

        } else {

            binding!!.kullaniciLayout.visibility = View.GONE
            binding!!.servisLayout.visibility = View.VISIBLE

            db = FirebaseFirestore.getInstance()
            db.collection(servis).document(userUid).addSnapshotListener { value, error ->

                if (error != null) {

                } else {

                    if (value != null) {

                        if (value.exists()) {


                            val email = value["email"] as String?
                            val kullaniciAdi = value["kullaniciAdi"] as String?
                            val location = value["location"] as String?
                            val score = value["score"] as Double?
                            val hizmetlerMap = value["hizmetler"] as HashMap<Any,Any>?


                            binding!!.servisNameText.text = kullaniciAdi
                            binding!!.locationText.text = location

                            score?.let {

                                if (!score.equals(defaultScore)){
                                    binding!!.puanText.text = "%.2f".format(score)
                                } else
                                    binding!!.puanText.text = "Derecelendirilmemiş"

                            }


                            if (hizmetlerMap != null) {
                                for ((myKey, myValue) in hizmetlerMap) {
                                    val servisDetails = ServisDetails(myKey as String, myValue as String,2)
                                    hizmetlerList.add(servisDetails)
                                }
                            }

                            adapter = HizmetlerAdapter(hizmetlerList)
                            binding!!.hizmetlerListesi.layoutManager = LinearLayoutManager(requireContext())
                            binding!!.hizmetlerListesi.adapter = adapter


                        }
                    }
                }
            }



        }



    }
}