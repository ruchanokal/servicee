package com.servicee.servicee.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.servicee.servicee.R
import com.servicee.servicee.activity.SignInActivity
import com.servicee.servicee.adapter.RandevuAdapter
import com.servicee.servicee.adapter.ServisAdapter
import com.servicee.servicee.databinding.DialogHizmetPuanlamaBinding
import com.servicee.servicee.databinding.FragmentMainBinding
import com.servicee.servicee.model.Randevu
import com.servicee.servicee.model.Servis
import com.servicee.servicee.util.Constants
import com.servicee.servicee.util.Constants.Companion.defaultScore
import com.servicee.servicee.util.Constants.Companion.kullaniciGirisi
import com.servicee.servicee.util.Constants.Companion.randevular
import com.servicee.servicee.util.Constants.Companion.servis
import com.servicee.servicee.util.Constants.Companion.servisGirisi
import com.servicee.servicee.util.Constants.Companion.tamamlananRandevular
import com.servicee.servicee.util.Constants.Companion.userfb
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

var girisType = ""
var kullaniciAdi = ""
var userUid = ""

class MainFragment : Fragment() {

    private var binding : FragmentMainBinding? = null
    private val TAG = "MainFragment"
    var reference : ListenerRegistration? = null
    private lateinit var alertDialog: AlertDialog.Builder
    lateinit var mAuth: FirebaseAuth
    lateinit var user : FirebaseUser
    private lateinit var db : FirebaseFirestore
    private var randevuList = arrayListOf<Randevu>()
    private var adapter : RandevuAdapter? = null
    var dialog = activity?.let { Dialog(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater,container,false)
        val view = binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = activity?.let { Dialog(it) }

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        userUid = mAuth.currentUser?.uid.toString()

        geriButonu()

        giris()

        signOut()

        buttonClicks()




    }

    private fun giris() {

        val intent = requireActivity().intent
        val definiteNumber = intent.getIntExtra("definite",0)

        Log.i(TAG,"definiteNumber --> " + definiteNumber)

        if ( definiteNumber == 1) {

            girisType = kullaniciGirisi
            binding!!.servisLayout.visibility = View.GONE
            kullaniciAdi = intent.getStringExtra("kullaniciAdi")!!
            binding?.kullaniciAdiAnaFragment?.setText(kullaniciAdi)
            tamamlananRandevulariDinle()
            Log.i(TAG,"yeni oluşturulan kullanıcı --> " + kullaniciAdi)

        } else if ( definiteNumber == 2) {

            girisType = kullaniciGirisi
            binding!!.servisLayout.visibility = View.GONE
            Log.i(TAG,"kullanıcı giriş yaptı --> " + mAuth.currentUser?.email)
            kullaniciAdiGetir(userfb)
        } else if ( definiteNumber == 3) {

            Log.i(TAG,"yeni oluşturulan (servis) kullanıcı --> " + kullaniciAdi)
            girisType = servisGirisi
            binding!!.userLayout.visibility = View.GONE
            kullaniciAdi = intent.getStringExtra("kullaniciAdi")!!
            binding?.kullaniciAdiAnaFragment?.setText(kullaniciAdi)
            randevuListesi()

        } else if ( definiteNumber == 4) {

            Log.i(TAG,"(servis) kullanıcı giriş yaptı --> " + mAuth.currentUser?.email)
            girisType = servisGirisi
            binding!!.userLayout.visibility = View.GONE
            kullaniciAdiGetir(servis)


        }

    }


    private fun randevuListesi() {

        adapter = RandevuAdapter(randevuList,this)
        binding!!.randevuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding!!.randevuRecyclerView.adapter = adapter

        db.collection(randevular).whereEqualTo("to",kullaniciAdi).addSnapshotListener { value, error ->

            if (error != null) {

                Toast.makeText(requireContext(),"Hata! Veriler alınamadı: ${error.localizedMessage}",Toast.LENGTH_LONG).show()

            } else {

                if (value != null) {

                    Log.i(TAG,"value is not null")

                    if (!value.isEmpty){

                        Log.i(TAG,"value is not empty")

                        randevuList.clear()

                        val documents = value.documents

                        for (document in documents){

                            val from = document.getString("from")
                            val to = document.getString("to")
                            val todo = document.get("todo") as ArrayList<String>
                            val time = document.getTimestamp("time")
                            val documentId = document.id

                            if (from != null && to != null && time != null){
                                val randevu = Randevu(from,to,todo,time,documentId)
                                randevuList.add(randevu)
                            }

                        }

                        adapter!!.notifyDataSetChanged()

                    } else {

                        randevuList.clear()
                        adapter!!.updateList(randevuList)
                    }

                }



            }



        }


    }

    private fun tamamlananRandevulariDinle() {

        Log.i(TAG,"tamamlananRandevularıDinle -> " + kullaniciAdi)

        db.collection(tamamlananRandevular).whereEqualTo("from",kullaniciAdi).get().addOnSuccessListener { value ->


                if (value != null) {

                    if (!value.isEmpty){

                        Log.i(TAG,"tamamlanan randevular is not empty")

                        val documents = value.documents

                        for (document in documents){

                            val from = document.getString("from")
                            val to = document.getString("to")
                            val todo = document.get("todo") as ArrayList<String>
                            val time = document.getTimestamp("time")
                            val documentId = document.id

                            Log.i(TAG,"from: " + from)
                            Log.i(TAG,"to: " + to)
                            Log.i(TAG,"todo: " + todo)
                            Log.i(TAG,"time: " + time)
                            Log.i(TAG,"documentId: " + documentId)


                            if (from != null && to != null && time != null) {

                                val randevu = Randevu(from,to,todo,time,documentId)
                                makeAlertDialog(randevu)

                            }



                        }

                    } else
                        Log.i(TAG,"value is empty" )

                } else
                    Log.i(TAG,"value is null" )


        }


    }

    private fun makeAlertDialog(randevu: Randevu) {

        Log.i(TAG,"makeAlertDialog")

        //dialog = activity?.let { Dialog(it) }

        if (dialog != null){

            if (dialog!!.isShowing)
                dialog!!.dismiss()

            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setCancelable(false)

            val binding2 : DialogHizmetPuanlamaBinding = DialogHizmetPuanlamaBinding.inflate(layoutInflater)
            dialog!!.setContentView(binding2.root)

            binding2.hizmetVerenServisText.text = randevu.to
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val formattedDate = sdf.format(randevu.time.toDate())
            binding2.tarihText.text = formattedDate
            val str = randevu.todo.joinToString(separator = ", ")
            binding2.yapilanIslemlerText.text = str

            binding2.puaniGonderButton.setOnClickListener {

                Log.i(TAG,"puanı gönder butonuna tıklandı")

                val rating = binding2.ratingBar.rating

                if (rating == 0.0f){

                    Toast.makeText(requireContext(),"Lütfen herhangi bir derecelendirme seçiniz!",Toast.LENGTH_SHORT).show()

                } else {

                    Log.i(TAG,"rating: " + rating +  ", to:" + randevu.to)


                    db.collection(servis).whereEqualTo("kullaniciAdi",randevu.to).get().addOnSuccessListener { value ->



                            if (value != null) {

                                Log.i(TAG,"value is not null")

                                if (!value.isEmpty){
                                    Log.i(TAG,"value is not empty")

                                    val documents = value.documents

                                    for (document in documents) {

                                        val score = document.getDouble("score")
                                        val oySayisi = document.getLong("oySayisi")

                                        val documentId = document.id

                                        Log.i(TAG,"score: " + score)
                                        Log.i(TAG,"oySayisi: " + oySayisi)
                                        Log.i(TAG,"documentId: " + documentId)


                                        if (score == defaultScore){
                                           ilkPuaniGonder(documentId,rating,randevu.documentId)
                                        } else {
                                           if (oySayisi != null && score != null)
                                             puaniDegistir(oySayisi,score,documentId,rating,randevu.documentId)
                                        }

                                    }
                                } else
                                    Log.i(TAG,"value is empty")
                            } else
                                Log.i(TAG,"value is null")



                    }

                }

            }

            binding2.carpi.setOnClickListener {

                dialog!!.dismiss()

            }



            dialog!!.show()

        }

    }

    private fun puaniDegistir(oySayisi: Long, oldScore: Double, documentId: String, rating: Float,tamamlananDocumentId: String) {

        Log.i(TAG,"puaniDegistir")

        val totalScore = oldScore * oySayisi + rating
        val yeniOySayisi = oySayisi + 1

        val newScore = totalScore / yeniOySayisi

        val scoreMap = hashMapOf(
            "score" to newScore,
            "oySayisi" to yeniOySayisi
        )

        db.collection(servis).document(documentId).update(scoreMap as Map<String, Any>).addOnSuccessListener {
            Log.i(TAG,"puan değiştirilmiştir")


            db.collection(tamamlananRandevular).document(tamamlananDocumentId).delete().addOnSuccessListener {

                if (dialog != null)
                    dialog!!.dismiss()

            }




        }


    }

    private fun ilkPuaniGonder(documentId: String, rating : Float,tamamlananDocumentId: String) {

        Log.i(TAG,"ilkPuaniGonder")

        val scoreMap = hashMapOf(
            "score" to rating,
            "oySayisi" to 1
        )

        db.collection(servis).document(documentId).update(scoreMap as Map<String, Any>).addOnSuccessListener {
            Log.i(TAG,"ilk puan gönderildi")
            db.collection(tamamlananRandevular).document(tamamlananDocumentId).delete().addOnSuccessListener {
                if (dialog != null)
                    dialog!!.dismiss()

            }
        }
    }

    private fun kullaniciAdiGetir(collectionName: String) {

        db.collection(collectionName).document(userUid).get().addOnSuccessListener { doc ->

            if (doc != null) {

                if (doc.exists()){

                    kullaniciAdi = doc["kullaniciAdi"] as String
                    binding?.kullaniciAdiAnaFragment?.text = kullaniciAdi

                    if (collectionName.equals(servis)){
                        randevuListesi()
                    } else {
                        tamamlananRandevulariDinle()
                    }

                }
            }

        }.addOnFailureListener {

            Toast.makeText(requireContext(),"Lütfen internet bağlantınızı kontrol edin!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun buttonClicks() {

        binding!!.servisButton.setOnClickListener {

            if (kullaniciAdi.isNotEmpty()){
                val action = MainFragmentDirections.actionMainFragmentToServisMainFragment(kullaniciAdi)
                findNavController(it).navigate(action)
            }

        }


        binding!!.cekiciCagirButton.setOnClickListener {

            val action = MainFragmentDirections.actionMainFragmentToCekiciCagirFragment()
            findNavController(it).navigate(action)

        }

    }

    private fun signOut() {

        binding?.signOutButton?.setOnClickListener {

            alertDialog = AlertDialog.Builder(requireContext())

            alertDialog.setTitle(getString(R.string.exitstring))
            alertDialog.setMessage(getString(R.string.exit_desc))
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton(getString(R.string.exitstring)) { dialog,which ->

                reference?.remove()

                mAuth.signOut()
                val intent = Intent(requireActivity(),SignInActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

            }.setNeutralButton(getString(R.string.cancelstring)) { dialog,which ->


            }

            alertDialog.show()


        }
    }

    private fun geriButonu() {

        val callback = object  : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

    }


}