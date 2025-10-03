package com.example.postask.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.postask.databinding.ItemIsoFieldBinding
import com.example.postask.domain.model.IsoField

class IsoFieldAdapter : RecyclerView.Adapter<IsoFieldAdapter.ViewHolder>() {

    private var list: List<IsoField> = emptyList()
    fun submitList(newList: List<IsoField>) {
        list = newList; notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemIsoFieldBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(f: IsoField) {
            binding.tvFieldNumber.text = if (f.number == 0) "MTI" else "Field ${f.number}"
            binding.tvFieldValue.text = f.value
            binding.tvFieldDescription.text = f.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemIsoFieldBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = list.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])
}
