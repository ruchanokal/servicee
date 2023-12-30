package com.servicee.servicee.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.servicee.servicee.R
import com.servicee.servicee.adapter.HizmetlerAdapter
import com.servicee.servicee.adapter.ServisAdapter
import com.servicee.servicee.databinding.FragmentServisDetailsBinding
import com.servicee.servicee.model.ServisDetails
import com.servicee.servicee.util.Constants.Companion.randevular


class ServisDetailsFragment : Fragment() {

    private var binding : FragmentServisDetailsBinding? = null
    private val TAG = "ServisDetailsFragment"
    private var hizmetlerList = arrayListOf<ServisDetails>()
    private var adapter : HizmetlerAdapter? = null
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServisDetailsBinding.inflate(inflater,container,false)
        val view = binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()

        arguments?.let {

            val selectedServis = ServisDetailsFragmentArgs.fromBundle(it).servis
            val kullaniciAdi = ServisDetailsFragmentArgs.fromBundle(it).kullaniciAdi

            for ((key, value) in selectedServis.services) {
                val servisDetails = ServisDetails(key as String, value as String,1)
                hizmetlerList.add(servisDetails)
            }


            binding!!.servisNameText.text = selectedServis.name
            adapter = HizmetlerAdapter(hizmetlerList)
            binding!!.hizmetlerListesi.layoutManager = LinearLayoutManager(requireContext())
            binding!!.hizmetlerListesi.adapter = adapter


            binding!!.randevuAlButton.setOnClickListener {


                if (adapter!!.getSelectedHizmetlerList().isEmpty()){

                    Toast.makeText(requireContext(),"Hiç hizmet seçmediniz",Toast.LENGTH_LONG).show()

                } else {

                    val hashMap = hashMapOf<Any,Any>()

                    hashMap.put("from",kullaniciAdi)
                    hashMap.put("to",selectedServis.name)
                    hashMap.put("time",Timestamp.now())
                    hashMap.put("todo",adapter!!.getSelectedHizmetlerList())

                    db.collection(randevular).add(hashMap).addOnSuccessListener {

                        binding!!.randevuAlmadanOnceLayout.visibility = View.GONE
                        binding!!.randevuAldiktanSonraLayout.visibility = View.VISIBLE

                    }.addOnFailureListener {

                        Toast.makeText(requireContext(),"Randevu oluşturulamadı, lütfen tekrar deneyin! ",Toast.LENGTH_LONG).show()

                    }

                }




            }

            binding!!.geriDon.setOnClickListener {
                findNavController().popBackStack()
            }


        }


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