package com.sangam.muscleplay.botton_nav.home.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.sangam.muscleplay.R
import com.sangam.muscleplay.botton_nav.home.model.FitnessFactsResponseModel
import com.sangam.muscleplay.databinding.FitnessFactsItemLayoutBinding

class FitnessFactsAdapter(val context: Context, val list: List<FitnessFactsResponseModel>) :
    Adapter<FitnessFactsAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: FitnessFactsItemLayoutBinding) :
        ViewHolder(binding.root) {
        fun bindView(items: FitnessFactsResponseModel, context: Context, position: Int) {
            binding.Headline.text = items.factsHeadline
            binding.summary.text = items.factsSummary
            Glide.with(context).load(items.imageUrl).placeholder(R.drawable.baseline_error_outline_24)
                .into(binding.imageViewUrl)

            binding.share.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, binding.summary.text.toString().trim())
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = FitnessFactsItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        return list[position].let {
            holder.bindView(it, context, position)
        }
    }
}