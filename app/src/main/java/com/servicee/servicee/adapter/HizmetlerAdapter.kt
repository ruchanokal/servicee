package com.servicee.servicee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.servicee.servicee.databinding.HizmetRow2Binding
import com.servicee.servicee.databinding.HizmetRowBinding
import com.servicee.servicee.databinding.ServisRowBinding
import com.servicee.servicee.model.ServisDetails

class HizmetlerAdapter(val hizmetlerListesi : ArrayList<ServisDetails>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val selectedHizmetlerList = arrayListOf<String>()

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    class HizmetlerViewHolder1(val binding : HizmetRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ServisDetails,selectedHizmetlerList : ArrayList<String>) {
            binding.hizmetCheckBox.text = item.key + ":  " + item.value + " ₺"
            binding.hizmetCheckBox.setOnCheckedChangeListener { compoundButton, b ->

                if (b){
                    selectedHizmetlerList.add(item.key)
                } else {
                    selectedHizmetlerList.remove(item.key)
                }

            }
        }
    }

    class HizmetlerViewHolder2(val binding : HizmetRow2Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ServisDetails) {
            binding.hizmetTextView.text = item.key + ":  " + item.value + " ₺"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == VIEW_TYPE_ONE) {
            val binding = HizmetRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            HizmetlerViewHolder1(binding)
        } else {
            val binding = HizmetRow2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
            HizmetlerViewHolder2(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = hizmetlerListesi.get(position)
        return if (item.type == 1) {
            VIEW_TYPE_ONE
        } else {
            VIEW_TYPE_TWO
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = hizmetlerListesi[position]
        if (holder.itemViewType == VIEW_TYPE_ONE) {
            val viewHolderType1 = holder as HizmetlerViewHolder1
            viewHolderType1.bind(item,selectedHizmetlerList)
        } else {
            val viewHolderType2 = holder as HizmetlerViewHolder2
            viewHolderType2.bind(item)
        }
    }

    fun getSelectedHizmetlerList(): List<String>{
        return selectedHizmetlerList
    }

    override fun getItemCount(): Int {
        return hizmetlerListesi.size
    }
}