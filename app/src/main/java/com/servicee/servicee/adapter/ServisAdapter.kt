package com.servicee.servicee.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.servicee.servicee.databinding.ServisRowBinding
import com.servicee.servicee.fragments.ServisMainFragment
import com.servicee.servicee.fragments.ServisMainFragmentDirections
import com.servicee.servicee.model.Servis
import com.servicee.servicee.util.Constants.Companion.defaultScore

class ServisAdapter(val servisList : ArrayList<Servis>, val kullaniciAdi : String ) : RecyclerView.Adapter<ServisAdapter.ServisHolder>() {

    class ServisHolder(val binding : ServisRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServisHolder {
        val binding = ServisRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ServisHolder(binding)
    }

    override fun onBindViewHolder(holder: ServisHolder, position: Int) {

        holder.binding.otoServisName.text = servisList.get(position).name
        holder.binding.otoServisLocation.text = servisList.get(position).location

        if (servisList.get(position).score != defaultScore)
            holder.binding.otoServisScore.text = "%.2f".format(servisList.get(position).score)
        else
            holder.binding.otoServisScore.text = "Derecelendirilmemiş"


        holder.itemView.setOnClickListener {

            val action = ServisMainFragmentDirections.actionServisMainFragmentToServisDetailsFragment(servisList.get(position),kullaniciAdi)
            Navigation.findNavController(it).navigate(action)

        }

    }

    override fun getItemCount(): Int {
        return servisList.size
    }


}