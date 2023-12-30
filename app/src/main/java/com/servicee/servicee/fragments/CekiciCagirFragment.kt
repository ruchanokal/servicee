package com.servicee.servicee.fragments

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
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.servicee.servicee.R
import com.servicee.servicee.adapter.CekiciCagirAdapter
import com.servicee.servicee.databinding.FragmentCekiciCagirBinding
import com.servicee.servicee.model.Cekici
import kotlin.random.Random


class CekiciCagirFragment : Fragment() {

    private var binding : FragmentCekiciCagirBinding? = null
    private var adapter : CekiciCagirAdapter? = null
    private var cekiciList = arrayListOf<Cekici>()
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    val phoneNumber = "+908505555555"
    private val TAG = "CekiciCagirFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCekiciCagirBinding.inflate(inflater,container,false)
        val view = binding!!.root
        registerLauncher()
        return view
    }

    fun getPermission(view : View) {

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

            Log.i(TAG,"henüz izin alınmadı")

            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),android.Manifest.permission.CALL_PHONE)){
                Snackbar.make(view,"Arama yapabilmek için izin vermelisin", Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver") {
                    requestPermissionLauncher.launch(android.Manifest.permission.CALL_PHONE)
                }.show()
            }else {
                requestPermissionLauncher.launch(android.Manifest.permission.CALL_PHONE)
            }
        } else {

            Log.i(TAG,"izin alındı")

            requestPermissionLauncher.launch(android.Manifest.permission.CALL_PHONE)
        }

    }

    private fun registerLauncher() {

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // İzin verildi, arama yap

                Log.i(TAG,"izin verildi arama yapabilirsin!")
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:$phoneNumber")
                startActivity(intent)
            } else {
                // İzin verilmedi, kullanıcıya bilgi ver
                Log.i(TAG,"İzin verilmedi!, lütfen izin al")
                Toast.makeText(requireContext(), "Telefon arama izni verilmedi", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cekiciNameList = arrayListOf<String>("Ahmet Güneş","Murat Kumcu","Şahin Duman","Levent Göksu","Abdullah Yılmaz"
            ,"Durmuş Adıgüzel","Cüneyt Gümüşçü","Fatih Atmaca","Ömer Göl","İzzet Altın","Rüzgar Altıner","Hakan Tan")

        for (a in 1..8){

            val random = Random.nextInt(cekiciNameList.size)
            val name = cekiciNameList.get(a)
            val cekici = Cekici(name,random.toString())
            cekiciList.add(cekici)
        }

        adapter = CekiciCagirAdapter(cekiciList,this)
        binding!!.cekiciCagirRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding!!.cekiciCagirRecyclerView.adapter = adapter

        geriButonu()
    }

    private fun geriButonu() {

        val callback = object  : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

    }
}