package com.sangam.muscleplay.drawer_nav.drawer_nav_recipes.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sangam.muscleplay.databinding.RecepiesItemLayoutBinding
import com.sangam.muscleplay.drawer_nav.drawer_nav_recipes.model.RecipesResponseModelItem
import java.util.ArrayList

class RecipesAdapter(val context: Context, val list: ArrayList<RecipesResponseModelItem>) :
    Adapter<RecipesAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: RecepiesItemLayoutBinding) : ViewHolder(binding.root) {
        fun bindView(recipes: RecipesResponseModelItem, context: Context, position: Int) {
            binding.title.text = recipes.title
            binding.serving.text = recipes.servings
            binding.ingredients.text = recipes.ingredients
            binding.instructions.text = recipes.instructions
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            RecepiesItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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