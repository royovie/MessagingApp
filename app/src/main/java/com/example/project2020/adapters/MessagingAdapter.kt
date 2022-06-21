package com.example.project2020.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project2020.R
import com.example.project2020.user_utilities.Message

class MessagingAdapter(private var messages: ArrayList<Message>, val userId: String?): RecyclerView.Adapter<MessagingAdapter.MessagesViewHolder>() {

    companion object {
        val MESSAGE_CURRENT_USER = 1
        val MESSAGE_OTHER_USER = 2
    }

    fun addMessage(message: Message) {
        messages.add(message)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): MessagesViewHolder {
        if(type == MESSAGE_CURRENT_USER) {
            return MessagesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_current_user_message, parent, false))
        } else {
            return MessagesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.other_user_message, parent, false))
        }
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemViewType(position: Int): Int {
        if(messages[position].sentBy.equals(userId)) {
            return MESSAGE_CURRENT_USER
        } else {
            return MESSAGE_OTHER_USER
        }
    }

    class MessagesViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bind(message: Message) {
            view.findViewById<TextView>(R.id.messageTV).text = message.message
        }
    }
}