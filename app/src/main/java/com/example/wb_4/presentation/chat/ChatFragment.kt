package com.example.wb_4.presentation.chat

import android.os.Bundle
import android.renderscript.ScriptGroup
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.wb_4.R
import com.example.wb_4.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    private var binding: FragmentChatBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply{
            toolbarNameTextView.text = arguments?.getString("name")

            Glide.with(this@ChatFragment).load(arguments?.getString("avatar"))
                .placeholder(R.drawable.avatar_placeholder).into(toolbarAvatarImageView)
        }


    }

}