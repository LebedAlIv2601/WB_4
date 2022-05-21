package com.example.wb_4.presentation.dialog_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cesarferreira.tempo.toString
import com.example.wb_4.R
import com.example.wb_4.databinding.DialogListRvItemBinding
import com.example.wb_4.domain.model.CompanionUserDomain


class DialogListAdapter:
    ListAdapter<CompanionUserDomain, DialogListAdapter.DialogListViewHolder>(AsyncDifferConfig.Builder(DiffCallback()).build()) {

    class DialogListViewHolder(private val binding: DialogListRvItemBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(item: CompanionUserDomain){
            binding.apply {
                dialogListItemNameTextView.text = item.name
                if(item.lastMessage != null) {
                    if(item.lastMessage.isYour) {
                        yourMessageLastTextView.visibility = View.VISIBLE
                    } else {
                        yourMessageLastTextView.visibility = View.GONE
                    }
                    dialogListItemLastMessageTextView.text = item.lastMessage.message
                    if(item.receivedUnreadMessagesCount != 0 && !item.lastMessage.isYour){
                        dialogListItemNewMessagesCountTextView.visibility = View.VISIBLE
                        unreadYourMessagesMarker.visibility = View.GONE
                        dialogListItemNewMessagesCountTextView.text =
                            item.receivedUnreadMessagesCount.toString()
                    } else if (item.receivedUnreadMessagesCount != 0 && item.lastMessage.isYour){
                        dialogListItemNewMessagesCountTextView.visibility = View.INVISIBLE
                        unreadYourMessagesMarker.visibility = View.VISIBLE
                    } else{
                        dialogListItemNewMessagesCountTextView.visibility = View.INVISIBLE
                        unreadYourMessagesMarker.visibility = View.GONE
                    }

                    timeTextView.text = item.lastMessageTime?.toString("HH:mm")

                } else {
                    dialogListItemNewMessagesCountTextView.visibility = View.INVISIBLE
                    unreadYourMessagesMarker.visibility = View.GONE
                    timeTextView.visibility = View.GONE
                }

                Glide.with(binding.root.context).load(item.avatar)
                    .placeholder(R.drawable.avatar_placeholder).into(binding.dialogListItemAvatarImageView)
            }

        }

        companion object{
            fun from(parent: ViewGroup): DialogListViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(
                    R.layout.dialog_list_rv_item,
                    parent, false)
                return DialogListViewHolder(DialogListRvItemBinding.bind(view))
            }
        }
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogListViewHolder {
        return DialogListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DialogListViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    private class DiffCallback: ItemCallback<CompanionUserDomain>() {
        override fun areItemsTheSame(
            oldItem: CompanionUserDomain,
            newItem: CompanionUserDomain
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: CompanionUserDomain,
            newItem: CompanionUserDomain
        ) = (oldItem.lastMessage == newItem.lastMessage) &&
                (oldItem.name == newItem.name) && (oldItem.avatar == newItem.avatar) &&
                (oldItem.lastMessageTime == newItem.lastMessageTime) &&
                (oldItem.receivedUnreadMessagesCount == newItem.receivedUnreadMessagesCount)

    }
}