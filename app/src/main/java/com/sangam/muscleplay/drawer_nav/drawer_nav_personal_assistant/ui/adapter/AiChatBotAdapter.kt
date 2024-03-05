package com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sangam.muscleplay.R
import com.sangam.muscleplay.databinding.ChatbotLeftLayoutBinding
import com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.model.MessageModel

class AiChatBotAdapter(val context: Context, val list: ArrayList<MessageModel>) :
    Adapter<AiChatBotAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : ViewHolder(view) {
        val msgTxt = view.findViewById<TextView>(R.id.show_message)
        val imageContainer = view.findViewById<LinearLayout>(R.id.imageCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view: View?
        val from = LayoutInflater.from(parent.context)
        if (viewType == 0) {
            view = from.inflate(R.layout.chatbot_right_layout, parent, false)
        } else {
            view = from.inflate(R.layout.chatbot_left_layout, parent, false)

        }
        return MyViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        val message = list[position]
        return if (message.isUser) 0 else 1
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message = list[position]
        if (!message.isUser) {
            holder.imageContainer.visibility = View.GONE
        }
        holder.msgTxt.text = message.message
    }
}