package com.servicee.servicee.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.servicee.servicee.databinding.RandevuRowBinding
import com.servicee.servicee.fragments.RandevularimFragment
import com.servicee.servicee.model.Randevu
import com.servicee.servicee.util.Constants.Companion.onaylananRandevular
import com.servicee.servicee.util.Constants.Companion.randevular
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RandevuAdapter(val randevuList : ArrayList<Randevu>, val fragment: Fragment) : RecyclerView.Adapter<RandevuAdapter.RandevuHolder>() {

    private val TAG = "RandevuAdapter"

    class RandevuHolder(val binding: RandevuRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandevuHolder {

        val binding = RandevuRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RandevuHolder(binding)
    }

    override fun onBindViewHolder(holder: RandevuHolder, position: Int) {

        Log.i(TAG,"onBindViewHolder")

        holder.binding.hizmetAlacakKisiText.text = randevuList.get(position).from
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val formattedDate = sdf.format(randevuList.get(position).time.toDate())
        holder.binding.tarihText.text = formattedDate

        val str = randevuList.get(position).todo.joinToString(separator = ", ")
        holder.binding.yapilacakIslemlerText.text = str



        holder.binding.onaylaButton.setOnClickListener {

            val documentId = randevuList.get(position).documentId
            val sourceDocument = FirebaseFirestore.getInstance().collection(randevular).document(documentId)

            val targetCollection = FirebaseFirestore.getInstance().collection(onaylananRandevular)
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



        }

        holder.binding.reddetButton.setOnClickListener {

            val documentId = randevuList.get(position).documentId
            val sourceDocument = FirebaseFirestore.getInstance().collection(randevular).document(documentId)

            sourceDocument.delete().addOnSuccessListener {
                Log.d(TAG, "Document deleted successfully")
            }.addOnFailureListener { exception ->
                Log.e(TAG, "Error deleting document", exception)
            }
        }



    }

    fun updateList(newList :ArrayList<Randevu>) {

        randevuList.clear()
        randevuList.addAll(newList)
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return randevuList.size
    }
}