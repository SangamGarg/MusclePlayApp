package com.sangam.muscleplay.Calculators.caloryinfoodcalculator.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sangam.muscleplay.Calculators.caloryinfoodcalculator.model.CaloriesInFoodItem
import com.sangam.muscleplay.R
import com.sangam.muscleplay.databinding.CaloriesInFoodItemLayoutBinding
import com.sangam.muscleplay.databinding.CaloriesInFoodKnwoMoreBottomDailogBinding
import com.sangam.muscleplay.databinding.FragmentBmiBottomSheetBinding

class CaloriesInFoodAdapter(
    val context: Context,
    private val list: ArrayList<CaloriesInFoodItem>,
    private val onItemClick: (CaloriesInFoodItem?) -> Unit
) : Adapter<CaloriesInFoodAdapter.MyViewHolder>() {


    inner class MyViewHolder(val binding: CaloriesInFoodItemLayoutBinding) :
        ViewHolder(binding.root) {
        fun bindView(caloriesInFoodItem: CaloriesInFoodItem, context: Context, position: Int) {
            binding.foodCalories.text = caloriesInFoodItem.calories + "cal"
            binding.foodName.text = caloriesInFoodItem.name
            binding.foodServingSize.text = "Serving size-> ${caloriesInFoodItem.serving_size_g} g "
            binding.foodFat.text = caloriesInFoodItem.fat_total_g + " g"
            binding.foodProtein.text = caloriesInFoodItem.protein_g + " g"
            binding.foodCarbs.text = caloriesInFoodItem.carbohydrates_total_g + " g"
            var flag = 0

            binding.tvKnowMore.setOnClickListener {
                binding.tvCholesterolValue.text = caloriesInFoodItem.cholesterol_mg + " g"
                binding.tvFibreValue.text = caloriesInFoodItem.fiber_g + " g"
                binding.tvFatSaturatedValue.text = caloriesInFoodItem.fat_saturated_g + " g"
                binding.tvPotassiumValue.text = caloriesInFoodItem.potassium_mg + " g"
                binding.tvSodiumValue.text = caloriesInFoodItem.sodium_mg + " g"
                binding.tvSugarValue.text = caloriesInFoodItem.sugar_g + " g"
                if (flag == 0) {
                    binding.constraintLayout3.visibility = View.VISIBLE
                    binding.tvKnowMore.text = "Hide details"
                    flag = 1
                } else {
                    binding.tvKnowMore.text = "More details"
                    binding.constraintLayout3.visibility = View.GONE
                    flag = 0
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CaloriesInFoodItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
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