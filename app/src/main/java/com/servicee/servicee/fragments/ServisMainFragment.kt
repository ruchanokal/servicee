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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.servicee.servicee.R
import com.servicee.servicee.adapter.ServisAdapter
import com.servicee.servicee.databinding.FragmentServisMainBinding
import com.servicee.servicee.model.Servis
import com.servicee.servicee.util.Constants.Companion.servis


class ServisMainFragment : Fragment() {

    private val TAG = "ServisMainFragment"
    private var binding : FragmentServisMainBinding? = null
    private var adapter : ServisAdapter? = null
    private var servisList = arrayListOf<Servis>()
    lateinit var mAuth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    var userUid = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServisMainBinding.inflate(inflater,container,false)
        val view = binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        userUid = mAuth.currentUser?.uid.toString()


        arguments?.let {

            val kullaniciAdi = ServisMainFragmentArgs.fromBundle(it).kullaniciAdi

            adapter = ServisAdapter(servisList,kullaniciAdi)
            binding!!.servislerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding!!.servislerRecyclerView.adapter = adapter


            db.collection(servis).addSnapshotListener { value, error ->

                if (error != null) {

                    Toast.makeText(requireContext(),"Hata: " + error.localizedMessage, Toast.LENGTH_LONG).show()

                } else {

                    if (value != null){

                        if ( !value.isEmpty ) {

                            val documents = value.documents

                            Log.i(TAG,"value is not empty ")
                            servisList.clear()


                            for (document in documents) {

                                Log.i(TAG,"for item: " + document)


                                val kullaniciAdi = document.getString("kullaniciAdi")
                                val location = document.getString("location")
                                val score = document.getDouble("score")
                                val services = document.get("hizmetler") as HashMap<Any,Any>

                                if ( kullaniciAdi != null && location != null && score != null ) {

                                    val servis = Servis(kullaniciAdi,location,score,services)
                                    servisList.add(servis)
                                    Log.i(TAG,"item: " + servisList)
                                    Log.i(TAG,"item size: " + servisList.size)

                                }
                            }

                            adapter!!.notifyDataSetChanged()

                        }
                    }

                }

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