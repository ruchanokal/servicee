package com.servicee.servicee.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.servicee.servicee.R
import com.servicee.servicee.adapter.OnaylananRandevuAdapter
import com.servicee.servicee.databinding.FragmentRandevularimBinding
import com.servicee.servicee.model.Randevu
import com.servicee.servicee.util.Constants
import com.servicee.servicee.util.Constants.Companion.onaylananRandevular
import com.servicee.servicee.util.Constants.Companion.servisGirisi


class RandevularimFragment : Fragment() {

    private var binding : FragmentRandevularimBinding? = null
    private lateinit var db : FirebaseFirestore
    private var onaylananRandevuList = arrayListOf<Randevu>()
    private var adapter : OnaylananRandevuAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRandevularimBinding.inflate(inflater,container,false)
        val view = binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()


        if (girisType.equals(servisGirisi)){

            adapter = OnaylananRandevuAdapter(onaylananRandevuList)
            binding!!.onaylananRandevuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding!!.onaylananRandevuRecyclerView.adapter = adapter

            db.collection(onaylananRandevular).whereEqualTo("to",kullaniciAdi).addSnapshotListener { value, error ->

                if (error != null) {

                    Toast.makeText(requireContext(),"Hata! Veriler alınamadı: ${error.localizedMessage}",
                        Toast.LENGTH_LONG).show()

                } else {

                    if (value != null) {

                        if (!value.isEmpty){

                            onaylananRandevuList.clear()

                            val documents = value.documents
                            for (document in documents){

                                val from = document.getString("from")
                                val to = document.getString("to")
                                val todo = document.get("todo") as ArrayList<String>
                                val time = document.getTimestamp("time")
                                val documentId = document.id

                                if (from != null && to != null && time != null){
                                    val randevu = Randevu(from,to,todo,time,documentId)
                                    onaylananRandevuList.add(randevu)
                                }

                            }

                            adapter!!.notifyDataSetChanged()

                        }

                    }

                }
            }


        } else {

            adapter = OnaylananRandevuAdapter(onaylananRandevuList)
            binding!!.onaylananRandevuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding!!.onaylananRandevuRecyclerView.adapter = adapter

            db.collection(onaylananRandevular).whereEqualTo("from",kullaniciAdi).addSnapshotListener { value, error ->

                if (error != null) {

                    Toast.makeText(requireContext(),"Hata! Veriler alınamadı: ${error.localizedMessage}",
                        Toast.LENGTH_LONG).show()

                } else {

                    if (value != null) {

                        if (!value.isEmpty){

                            onaylananRandevuList.clear()

                            val documents = value.documents
                            for (document in documents){

                                val from = document.getString("from")
                                val to = document.getString("to")
                                val todo = document.get("todo") as ArrayList<String>
                                val time = document.getTimestamp("time")
                                val documentId = document.id

                                if (from != null && to != null && time != null){
                                    val randevu = Randevu(from,to,todo,time,documentId)
                                    onaylananRandevuList.add(randevu)
                                }

                            }

                            adapter!!.notifyDataSetChanged()

                        }

                    }

                }
            }

        }


    }


}