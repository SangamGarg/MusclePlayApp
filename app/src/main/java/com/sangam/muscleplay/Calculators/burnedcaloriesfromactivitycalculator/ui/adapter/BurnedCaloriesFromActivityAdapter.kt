package com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.model.BurnedCaloriesFromActivityModelItem
import com.sangam.muscleplay.databinding.BurnedCaloriesFromActivityItemLayoutBinding

class BurnedCaloriesFromActivityAdapter(
    val context: Context, val list: ArrayList<BurnedCaloriesFromActivityModelItem>
) : Adapter<BurnedCaloriesFromActivityAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: BurnedCaloriesFromActivityItemLayoutBinding) :
        ViewHolder(binding.root) {
        fun bindView(items: BurnedCaloriesFromActivityModelItem, context: Context, position: Int) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = BurnedCaloriesFromActivityItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        list[position].let {
            holder.bindView(it, context, position)
        }
    }

}