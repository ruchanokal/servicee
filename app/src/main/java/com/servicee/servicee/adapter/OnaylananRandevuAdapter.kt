package com.servicee.servicee.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.servicee.servicee.R
import com.servicee.servicee.activity.SignInActivity
import com.servicee.servicee.databinding.RandevuRowBinding
import com.servicee.servicee.fragments.girisType
import com.servicee.servicee.model.Randevu
import com.servicee.servicee.util.Constants
import com.servicee.servicee.util.Constants.Companion.servisGirisi
import java.text.SimpleDateFormat
import java.util.*

class OnaylananRandevuAdapter(val randevuList : ArrayList<Randevu>) : RecyclerView.Adapter<OnaylananRandevuAdapter.ORHolder>() {


    private val TAG = "OnaylananRandevuAdapter"

    class ORHolder(val binding: RandevuRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ORHolder {
        val binding = RandevuRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ORHolder(binding)
    }

    override fun onBindViewHolder(holder: ORHolder, position: Int) {

        if (girisType.equals(servisGirisi))
            holder.binding.hizmetAlacakKisiText.text = randevuList.get(position).from
        else
            holder.binding.hizmetAlacakKisiText.text = randevuList.get(position).to

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val formattedDate = sdf.format(randevuList.get(position).time.toDate())
        holder.binding.tarihText.text = formattedDate

        val str = randevuList.get(position).todo.joinToString(separator = ", ")
        holder.binding.yapilacakIslemlerText.text = str
        holder.binding.buttonsLayout.visibility = View.GONE

        if (girisType.equals(servisGirisi)){

            holder.itemView.setOnLongClickListener(MyLongClickListener(randevuList.get(position)))

        }

    }

    override fun getItemCount(): Int {
        return randevuList.size
    }

    class MyLongClickListener(randevu: Randevu) : View.OnLongClickListener {

        private val TAG = "OnaylananRandevuAdapter-2"

        private val randevum = randevu

        override fun onLongClick(v: View): Boolean {

            Log.i(TAG,"uzun tıklanıldı")
            val alertDialog = AlertDialog.Builder(v.context)

            alertDialog.setTitle(v.context.getString(R.string.hizmet_tamam))
            alertDialog.setMessage(v.context.getString(R.string.hizmet_tamam_desc))
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton(v.context.getString(R.string.evetstring)) { dialog, which ->

                val documentId = randevum.documentId
                val sourceDocument = FirebaseFirestore.getInstance().collection(Constants.onaylananRandevular).document(documentId)

                val targetCollection = FirebaseFirestore.getInstance().collection(Constants.tamamlananRandevular)
                sourceDocument.get().addOnSuccessListener { documentSnapshot ->
                    val data = documentSnapshot.data
                    if (data != null) {
                        targetCollection.add(data).addOnSuccessListener {
                            Log.d(TAG, "Document copied successfully")

                            sourceDocument.delete().addOnSuccessListener {
                                Log.d(TAG, "Document deleted successfully")
                            }.addOnFailureListener { exception ->
                                Log.e(TAG, "Error deleting document", exception)
                            }

                        }.addOnFailureListener { exception ->
                            Log.e(TAG, "Error copying document", exception)
                        }
                    }
                }.addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting document", exception)
                }

            }.setNeutralButton(v.context.getString(R.string.cancelstring)) { dialog, which ->


            }

            alertDialog.show()

            return true
        }
    }
}

