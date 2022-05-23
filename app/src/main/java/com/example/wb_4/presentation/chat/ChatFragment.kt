package com.example.wb_4.presentation.chat

import android.content.Context
import android.os.Bundle
import android.renderscript.ScriptGroup
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.wb_4.R
import com.example.wb_4.appComponent
import com.example.wb_4.databinding.FragmentChatBinding
import com.example.wb_4.domain.model.MessageDomain
import com.example.wb_4.presentation.dialog_list.DialogListViewModel
import com.example.wb_4.presentation.dialog_list.DialogListViewModelFactory
import javax.inject.Inject

class ChatFragment : Fragment() {

    private var binding: FragmentChatBinding? = null
    private var adapter: ChatAdapter? = null

    //ViewModel
    private val vm: ChatViewModel by viewModels {
        viewModelFactory
    }

    @Inject
    lateinit var viewModelFactory: ChatViewModelFactory

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

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

            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        setupRecyclerView()

        setupObservers()

        arguments?.getInt("id")?.let { vm.getMessages(it) }

    }

    private fun setupObservers() {
        vm.messagesList.observe(viewLifecycleOwner, Observer {
            adapter?.data = it
            adapter?.data?.size?.minus(1)?.let { binding?.chatRecyclerView?.scrollToPosition(it) }
        })
    }

    private fun setupRecyclerView() {
        adapter = ChatAdapter()

        binding?.apply {
            chatRecyclerView.adapter = adapter
            chatRecyclerView.layoutManager = LinearLayoutManager(context)
            chatRecyclerView.itemAnimator = null
            adapter?.data?.size?.minus(1)?.let { chatRecyclerView.scrollToPosition(it) }
        }
    }

}