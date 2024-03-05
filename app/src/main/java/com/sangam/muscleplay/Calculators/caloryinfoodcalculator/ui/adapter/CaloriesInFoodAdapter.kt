package com.sangam.muscleplay.Calculators.caloryinfoodcalculator.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sangam.muscleplay.Calculators.caloryinfoodcalculator.model.CaloriesInFoodItem
import com.sangam.muscleplay.databinding.CaloriesInFoodItemLayoutBinding

class CaloriesInFoodAdapter(
    val context: Context,
    private val list: ArrayList<CaloriesInFoodItem>,
    private val onItemClick: (CaloriesInFoodItem?) -> Unit
) :
    Adapter<CaloriesInFoodAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: CaloriesInFoodItemLayoutBinding) :
        ViewHolder(binding.root) {
        fun bindView(caloriesInFoodItem: CaloriesInFoodItem, context: Context, position: Int) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CaloriesInFoodItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val items = list[position]
        holder.itemView.setOnClickListener { onItemClick(items) }
        holder.bindView(items, context, position)
    }
}