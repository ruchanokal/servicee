package com.servicee.servicee.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.servicee.servicee.R
import com.servicee.servicee.activity.MainActivity
import com.servicee.servicee.databinding.FragmentServisHizmetlerBinding
import com.servicee.servicee.util.Constants.Companion.defaultScore
import kotlin.random.Random


class ServisHizmetlerFragment : Fragment() {

    private var binding : FragmentServisHizmetlerBinding? = null
    private lateinit var db : FirebaseFirestore
    private val TAG = "ServisHizmetlerFragment"
    private lateinit var mAuth: FirebaseAuth
    var userUid = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServisHizmetlerBinding.inflate(inflater,container,false)
        val view = binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val locationList = arrayListOf<String>("Bağcılar","Esenler","Mecidiyeköy","Levent","Kağıthane"
            ,"Beyoğlu","Çekmeköy","Fatih","Unkapanı","Yenikapı","Aksaray","Ümraniye","Üsküdar","Beykoz","Küçükçekmece","Çatalca")

        arguments?.let {

            val kullaniciAdi = ServisHizmetlerFragmentArgs.fromBundle(it).kullaniciAdi
            val email = ServisHizmetlerFragmentArgs.fromBundle(it).email
            val definite = ServisHizmetlerFragmentArgs.fromBundle(it).definite


            binding!!.checkUp.setOnCheckedChangeListener { compoundButton, b ->
                binding!!.checkUpFiyatEditText.isEnabled = b
                binding!!.checkUpFiyatEditText.requestFocus()
            }

            binding!!.farAyari.setOnCheckedChangeListener { compoundButton, b ->
                binding!!.farAyariFiyatEditText.isEnabled = b
                binding!!.farAyariFiyatEditText.requestFocus()
            }

            binding!!.lastikDegisimi.setOnCheckedChangeListener { compoundButton, b ->
                binding!!.lastikDegisimiFiyatEditText.isEnabled = b
                binding!!.lastikDegisimiFiyatEditText.requestFocus()
            }

            binding!!.suspansiyonTesti.setOnCheckedChangeListener { compoundButton, b ->
                binding!!.suspansiyonTestiFiyatEditText.isEnabled = b
                binding!!.suspansiyonTestiFiyatEditText.requestFocus()
            }

            binding!!.sogutmaSivisiDegisimi.setOnCheckedChangeListener { compoundButton, b ->
                binding!!.sogutmaSivisiDegisimiFiyatEditText.isEnabled = b
                binding!!.sogutmaSivisiDegisimiFiyatEditText.requestFocus()
            }

            binding!!.yagFiltresiDegisimi.setOnCheckedChangeListener { compoundButton, b ->
                binding!!.yagFiltresiDegisimiFiyatEditText.isEnabled = b
                binding!!.yagFiltresiDegisimiFiyatEditText.requestFocus()
            }

            binding!!.periyodikBakim.setOnCheckedChangeListener { compoundButton, b ->
                binding!!.periyodikBakimFiyatEditText.isEnabled = b
                binding!!.periyodikBakimFiyatEditText.requestFocus()
            }

            val hizmetlerHashMap = hashMapOf<Any,Any>()

            binding!!.kayitOlButton.setOnClickListener {

                if (binding!!.checkUp.isChecked) {
                    if (binding!!.checkUpFiyatEditText.text.isNotEmpty()){
                        val fiyat = binding!!.checkUpFiyatEditText.text.toString()
                        hizmetlerHashMap.put("Check Up",fiyat)
                    } else {
                        Toast.makeText(requireContext(),"Lütfen seçtiğiniz hizmet için fiyat bilgisi giriniz", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                }

                if (binding!!.farAyari.isChecked) {
                    if (binding!!.farAyariFiyatEditText.text.isNotEmpty()){
                        val fiyat = binding!!.farAyariFiyatEditText.text.toString()
                        hizmetlerHashMap.put("Far Ayarı",fiyat)
                    } else {
                        Toast.makeText(requireContext(),"Lütfen seçtiğiniz hizmet için fiyat bilgisi giriniz", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                }

                if (binding!!.lastikDegisimi.isChecked) {
                    if (binding!!.lastikDegisimiFiyatEditText.text.isNotEmpty()){
                        val fiyat = binding!!.lastikDegisimiFiyatEditText.text.toString()
                        hizmetlerHashMap.put("Lastik Değişimi",fiyat)
                    } else {
                        Toast.makeText(requireContext(),"Lütfen seçtiğiniz hizmet için fiyat bilgisi giriniz", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                }

                if (binding!!.suspansiyonTesti.isChecked) {
                    if (binding!!.suspansiyonTestiFiyatEditText.text.isNotEmpty()){
                        val fiyat = binding!!.suspansiyonTestiFiyatEditText.text.toString()
                        hizmetlerHashMap.put("Süspansiyon Testi",fiyat)
                    } else {
                        Toast.makeText(requireContext(),"Lütfen seçtiğiniz hizmet için fiyat bilgisi giriniz", Toast.LENGTH_LONG).show()
                        return@setOnClickListener

                    }
                }

                if (binding!!.sogutmaSivisiDegisimi.isChecked) {
                    if (binding!!.sogutmaSivisiDegisimiFiyatEditText.text.isNotEmpty()){
                        val fiyat = binding!!.sogutmaSivisiDegisimiFiyatEditText.text.toString()
                        hizmetlerHashMap.put("Soğutma Sıvısı Değişimi",fiyat)
                    } else {
                        Toast.makeText(requireContext(),"Lütfen seçtiğiniz hizmet için fiyat bilgisi giriniz", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                }


                if (binding!!.yagFiltresiDegisimi.isChecked) {
                    if (binding!!.yagFiltresiDegisimiFiyatEditText.text.isNotEmpty()){
                        val fiyat = binding!!.yagFiltresiDegisimiFiyatEditText.text.toString()
                        hizmetlerHashMap.put("Yağ Filtresi Değişimi",fiyat)
                    } else {
                        Toast.makeText(requireContext(),"Lütfen seçtiğiniz hizmet için fiyat bilgisi giriniz", Toast.LENGTH_LONG).show()
                        return@setOnClickListener

                    }
                }

                if (binding!!.periyodikBakim.isChecked) {
                    if (binding!!.periyodikBakimFiyatEditText.text.isNotEmpty()){
                        val fiyat = binding!!.periyodikBakimFiyatEditText.text.toString()
                        hizmetlerHashMap.put("Periyodik Bakım",fiyat)
                    } else {
                        Toast.makeText(requireContext(),"Lütfen seçtiğiniz hizmet için fiyat bilgisi giriniz", Toast.LENGTH_LONG).show()
                        return@setOnClickListener

                    }
                }

                if (!binding!!.checkUp.isChecked && !binding!!.farAyari.isChecked
                    && !binding!!.lastikDegisimi.isChecked && !binding!!.suspansiyonTesti.isChecked
                    && !binding!!.sogutmaSivisiDegisimi.isChecked && !binding!!.yagFiltresiDegisimi.isChecked
                    && !binding!!.periyodikBakim.isChecked) {

                    Toast.makeText(requireContext(),"Lütfen en az bir hizmet seçiniz",Toast.LENGTH_LONG).show()
                    return@setOnClickListener

                }


                val hashMap = hashMapOf<Any,Any>()
                val email = mAuth.currentUser?.email

                userUid = mAuth.currentUser?.uid.toString()
                email?.let { hashMap.put("email", it) }
                kullaniciAdi.let { hashMap.put("kullaniciAdi",it) }
                hizmetlerHashMap.let { hashMap.put("hizmetler",it) }
                val randomIndex = java.util.Random().nextInt(locationList.size)
                hashMap.put("location",locationList.get(randomIndex))
                hashMap.put("score",defaultScore)


                db.collection("Service").document(userUid).set(hashMap).addOnSuccessListener {

                    Log.i(TAG,"service bir kullanıcı eklendi")


                    val intent = Intent(activity, MainActivity::class.java)
                    intent.putExtra("kullaniciAdi",kullaniciAdi)
                    intent.putExtra("definite",3)
                    startActivity(intent)
                    requireActivity().finish()

                    Toast.makeText(activity,"Hoşgeldin ${kullaniciAdi}",Toast.LENGTH_LONG).show()

                }.addOnFailureListener {

                    Log.e(TAG,"kullanıcı oluşturulamadı yeniden deneyin!")

                }


            }



        }


    }


}