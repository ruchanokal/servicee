package com.servicee.servicee.adapter

import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.servicee.servicee.databinding.CekiciRowBinding
import com.servicee.servicee.fragments.CekiciCagirFragment
import com.servicee.servicee.model.Cekici

class CekiciCagirAdapter(val cekiciList : ArrayList<Cekici>, val fragment : CekiciCagirFragment) : RecyclerView.Adapter<CekiciCagirAdapter.CHolder>() {

    class CHolder(val binding : CekiciRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CHolder {
        val binding = CekiciRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CHolder(binding)
    }

    override fun onBindViewHolder(holder: CHolder, position: Int) {
        holder.binding.cekiciNameText.text = cekiciList.get(position).name
        holder.binding.cekiciLocationText.text = cekiciList.get(position).location + " km"
        holder.binding.cekiciCagirButton.setOnClickListener {

            fragment.getPermission(holder.itemView)

        }

    }

    override fun getItemCount(): Int {
        return cekiciList.size
    }
}