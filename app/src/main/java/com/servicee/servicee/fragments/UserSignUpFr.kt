package com.servicee.servicee.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ListenerRegistration
import com.servicee.servicee.R
import com.servicee.servicee.activity.MainActivity
import com.servicee.servicee.databinding.FragmentServiceSignUpBinding
import com.servicee.servicee.databinding.FragmentUserSignUpBinding


class UserSignUpFr : Fragment() {

    private var binding: FragmentUserSignUpBinding? = null
    private lateinit var db: FirebaseFirestore
    private val TAG = "UserSignUpFr"
    private lateinit var mAuth: FirebaseAuth
    var reference: ListenerRegistration? = null
    var userUid = ""
    private lateinit var email: String
    private lateinit var kullaniciAdi: String
    private var sifre: String = ""
    private var sifre2: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserSignUpBinding.inflate(inflater, container, false)
        val view = binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding?.kayitOlButton?.setOnClickListener {

            binding?.progressBarSignUp?.visibility = View.VISIBLE

            email = binding?.editTextEmailKayit?.text.toString()
            kullaniciAdi = binding?.editTextKullaniciAdiKayit?.text.toString()
            sifre = binding?.editTextSifreKayit?.text.toString()
            sifre2 = binding?.editTextSifreKayit2?.text.toString()

            databaseCollection()

        }
    }

    private fun databaseCollection() {

        reference = db.collection("User")
            .whereEqualTo("kullaniciAdi", kullaniciAdi).addSnapshotListener { value, error ->

                Log.i(TAG, "kullaniciAdi: " + kullaniciAdi)

                if (value != null) {

                    Log.i(TAG, "null değil")

                    if (!value.isEmpty) {
                        Log.i(TAG, "empty değil")

                        Toast.makeText(
                            context, "Lütfen başka bir kullanıcı adı deneyin!",
                            Toast.LENGTH_LONG
                        ).show()

                        binding?.progressBarSignUp?.visibility = View.GONE

                        reference?.remove()

                    } else {
                        Log.i(TAG, "empty")
                        kontroller()
                    }

                } else {
                    Log.i(TAG, "null")
                    kontroller()
                }

                if (error != null)
                    Log.i(TAG, "error: " + error)

            }
    }

    private fun kontroller() {


        if (email.equals("")
            || kullaniciAdi.equals("")
            || sifre.equals("")
        ) {

            reference?.remove()

            Toast.makeText(activity, "Lütfen gerekli alanları doldurunuz", Toast.LENGTH_LONG).show()

            binding?.progressBarSignUp?.visibility = View.GONE

        } else if (!sifre.equals(sifre2)) {

            reference?.remove()

            Toast.makeText(activity, "Şifreler aynı olmalıdır!", Toast.LENGTH_LONG).show()

            binding?.progressBarSignUp?.visibility = View.GONE

        } else {

            mAuth.createUserWithEmailAndPassword(email, sifre).addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    reference?.remove()

                    userUid = mAuth.currentUser?.uid.toString()

                    binding?.progressBarSignUp?.visibility = View.GONE

                    val hashMap = hashMapOf<Any, Any>()
                    val email = mAuth.currentUser?.email

                    email?.let { hashMap.put("email", it) }
                    kullaniciAdi.let { hashMap.put("kullaniciAdi", it) }

                    db.collection("User").document(userUid).set(hashMap).addOnSuccessListener {

                        Log.i(TAG, "user bir kullanıcı eklendi")

                        val intent = Intent(activity, MainActivity::class.java)
                        intent.putExtra("kullaniciAdi", kullaniciAdi)
                        intent.putExtra("definite", 1)
                        startActivity(intent)
                        requireActivity().finish()

                        Toast.makeText(activity, "Hoşgeldin ${kullaniciAdi}", Toast.LENGTH_LONG)
                            .show()

                    }.addOnFailureListener {

                        Log.e(TAG, "kullanıcı oluşturulamadı yeniden deneyin!")

                    }


                }

            }.addOnFailureListener { exception ->


                try {
                    throw exception
                } catch (e: FirebaseAuthUserCollisionException) {

                    reference?.remove()

                    Toast.makeText(
                        activity,
                        "Bu e-posta adresi zaten başka bir hesap tarafından kullanılıyor",
                        Toast.LENGTH_LONG
                    ).show()
                    binding?.progressBarSignUp?.visibility = View.GONE

                } catch (e: FirebaseAuthWeakPasswordException) {

                    reference?.remove()

                    Toast.makeText(
                        activity,
                        "Lütfen en az 6 haneli bir şifre giriniz",
                        Toast.LENGTH_LONG
                    ).show()
                    binding?.progressBarSignUp?.visibility = View.GONE

                } catch (e: FirebaseNetworkException) {

                    reference?.remove()

                    Toast.makeText(
                        activity,
                        "Lütfen internet bağlantınızı kontrol edin",
                        Toast.LENGTH_LONG
                    ).show()
                    binding?.progressBarSignUp?.visibility = View.GONE

                } catch (e: FirebaseAuthInvalidCredentialsException) {

                    reference?.remove()

                    Toast.makeText(activity, e.localizedMessage, Toast.LENGTH_LONG).show()
                    binding?.progressBarSignUp?.visibility = View.GONE

                } catch (e: Exception) {

                    reference?.remove()

                    Toast.makeText(activity, e.localizedMessage, Toast.LENGTH_LONG).show()
                    binding?.progressBarSignUp?.visibility = View.GONE
                }

            }


        }
    }
}