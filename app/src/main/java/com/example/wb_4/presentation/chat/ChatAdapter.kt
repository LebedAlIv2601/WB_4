package com.example.wb_4.presentation.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wb_4.R
import com.example.wb_4.databinding.ChatRecyclerViewNotYourMessageItemBinding
import com.example.wb_4.databinding.ChatRecyclerViewYourMessageItemBinding
import com.example.wb_4.domain.model.CompanionUserDomain
import com.example.wb_4.domain.model.MessageDomain

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data = listOf<MessageDomain>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ChatYourMessageViewHolder(private val binding: ChatRecyclerViewYourMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MessageDomain){
            binding.yourMessageTextView.text = item.message
        }

        companion object{
            fun from(parent: ViewGroup): ChatYourMessageViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(
                    R.layout.chat_recycler_view_your_message_item,
                    parent, false)
                return ChatYourMessageViewHolder(ChatRecyclerViewYourMessageItemBinding.bind(view))
            }
        }
    }

    class ChatNotYourMessageViewHolder(private val binding: ChatRecyclerViewNotYourMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MessageDomain){
            binding.notYourMessageTextView.text = item.message
        }

        companion object{
            fun from(parent: ViewGroup): ChatNotYourMessageViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(
                    R.layout.chat_recycler_view_not_your_message_item,
                    parent, false)
                return ChatNotYourMessageViewHolder(ChatRecyclerViewNotYourMessageItemBinding.bind(view))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == 1){
            ChatYourMessageViewHolder.from(parent)
        } else {
            ChatNotYourMessageViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(data[position].isYour){
            (holder as ChatYourMessageViewHolder).bind(data[position])
        } else {
            (holder as ChatNotYourMessageViewHolder).bind(data[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(data[position].isYour) 1 else 0
    }

    override fun getItemCount(): Int {
        return data.size
    }

}