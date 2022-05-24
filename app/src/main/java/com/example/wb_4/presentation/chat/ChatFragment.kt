package com.example.wb_4.presentation.chat

import android.content.Context
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    //Флаг для отправки запроса для получения следующих 10 элементов
    //Если флаг равен true, то грузить можно
    private var loadPermission = true

    //Переменная для хранения индекса, с которого были загружены последние данные.
    //Нужен, чтобы ScrollListener не посылал несколько запросов для получения данных с одного и
    //того же места
    private var dataLastIndex = -1

    private var userId: Int? = -1

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

        userId = arguments?.getInt("id")


        setupRecyclerView()

        setupObservers()

        userId?.let { vm.getMessages(it, dataLastIndex) }

    }

    private fun setupObservers() {
        vm.messagesList.observe(viewLifecycleOwner, Observer {
            adapter?.data = it
        })

        vm.loadPermission.observe(viewLifecycleOwner, Observer {
            loadPermission = it
        })

        vm.dataLastIndex.observe(viewLifecycleOwner, Observer {
            dataLastIndex = it
        })
    }

    private fun setupRecyclerView() {
        adapter = ChatAdapter()

        binding?.apply {
            chatRecyclerView.adapter = adapter
            val layoutManager = LinearLayoutManager(context)
            layoutManager.reverseLayout = true
            layoutManager.stackFromEnd = true
            chatRecyclerView.layoutManager = layoutManager
            chatRecyclerView.itemAnimator = null

            chatRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){

//                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                    if(newState == RecyclerView.SCROLL_STATE_IDLE){
//                        if(loadPermission) {
//                            val layoutManager = binding?.chatRecyclerView?.layoutManager
//                            if (layoutManager is LinearLayoutManager) {
//
//                                //Если индекс последнего видимого элемента равен индексу последнего
//                                //элемента в списке в адаптере, и при этом до этого с этого места не были
//                                //загружены данные, то загрузить новый данные
//                                if (layoutManager.findFirstVisibleItemPosition() == 0 &&
//                                    dataLastIndex != adapter?.data?.get(layoutManager.findFirstVisibleItemPosition())?.id
//                                ) {
//                                    Log.e("Scroll", "Need more data detected by scroll")
//                                    adapter?.data?.get(layoutManager.findFirstVisibleItemPosition())?.id?.let {
//                                        vm.setupDataLastIndex(
//                                            it
//                                        )
//                                    }
//                                    userId?.let { vm.getMessages(it, dataLastIndex) }
//                                }
//                            }
//                        }
//                    }
//                    super.onScrollStateChanged(recyclerView, newState)
//                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    if(loadPermission) {
                        val layoutManager = binding?.chatRecyclerView?.layoutManager
                        if (layoutManager is LinearLayoutManager) {

                            //Если индекс последнего видимого элемента равен индексу последнего
                            //элемента в списке в адаптере, и при этом до этого с этого места не были
                            //загружены данные, то загрузить новый данные
                            if (layoutManager.findLastVisibleItemPosition() ==
                                adapter?.data?.size?.minus(1) &&
                                dataLastIndex != layoutManager.findLastVisibleItemPosition()
                            ) {
                                Log.e("Scroll", "Need more data detected by scroll")
                                vm.setupDataLastIndex(layoutManager.findLastVisibleItemPosition())
                                userId?.let { vm.getMessages(it, layoutManager.findLastVisibleItemPosition()) }
                            }
                        }
                    }
                    super.onScrolled(recyclerView, dx, dy)
                }

            })
        }
    }

}