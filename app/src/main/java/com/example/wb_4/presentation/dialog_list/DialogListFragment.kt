package com.example.wb_4.presentation.dialog_list

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wb_4.R
import com.example.wb_4.appComponent
import com.example.wb_4.databinding.FragmentDialogListBinding
import com.example.wb_4.domain.model.CompanionUserDomain
import javax.inject.Inject


class DialogListFragment : Fragment() {

    //Переменная для viewBinding
    private var binding: FragmentDialogListBinding? = null

    //Переменная для адаптера RecyclerView
    private var adapter: DialogListAdapter? = null

    //Флаг для отправки запроса для получения следующих 10 элементов
    //Если флаг равен true, то грузить можно
    private var loadPermission = true

    //Переменная для хранения индекса, с которого были загружены последние данные.
    //Нужен, чтобы ScrollListener не посылал несколько запросов для получения данных с одного и
    //того же места
    private var dataLastIndex = 0

    //ViewModel
    private val vm: DialogListViewModel by viewModels {
        viewModelFactory
    }

    @Inject
    lateinit var viewModelFactory: DialogListViewModelFactory

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDialogListBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        //Вызываем метод для настройки RecyclerView
        setupRecyclerView()

        setupObservers()

        setupListeners()
        super.onViewCreated(view, savedInstanceState)
    }


    private fun setupListeners() {
        binding?.dialogListSwipeRefresh?.setOnRefreshListener {
            Log.e("Refresh", "Refresh is called")

            binding?.dialogListSwipeRefresh?.isRefreshing = true

            vm.updateDialogs()
        }
    }

    private fun setupObservers(){
        vm.dialogsList.observe(viewLifecycleOwner, Observer {
            adapter?.submitList(it)
            binding?.dialogListSwipeRefresh?.isRefreshing = false
        })

        vm.loadPermission.observe(viewLifecycleOwner, Observer {
            loadPermission = it
        })

        vm.dataLastIndex.observe(viewLifecycleOwner, Observer {
            dataLastIndex = it
        })


    }



    private fun navigateToChat(item: CompanionUserDomain){
        val navController = findNavController()
        val userInfoBundle = Bundle()
        userInfoBundle.putString("name", "${item.name}")
        userInfoBundle.putString("avatar", "${item.avatar}")
        navController.navigate(R.id.action_dialogListFragment_to_chatFragment, userInfoBundle)
    }

    private fun setupRecyclerView(){
        adapter = DialogListAdapter{ navigateToChat(it) }

        binding?.apply {
            dialogListRv.adapter = adapter
            dialogListRv.layoutManager = LinearLayoutManager(this@DialogListFragment.context)
            dialogListRv.addItemDecoration(
                DividerItemDecoration(
                    dialogListRv.context,
                    DividerItemDecoration.VERTICAL
                )
            )

            //Убираем itemAnimator, так как с ним приложение падает при обновлении списка
            dialogListRv.itemAnimator = null


            //Устанавлием слушателя скролла для вызова метода получения следующей порции данных
            dialogListRv.addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    if(loadPermission) {
                        val layoutManager = binding?.dialogListRv?.layoutManager
                        if (layoutManager is LinearLayoutManager) {

                            //Если индекс последнего видимого элемента равен индексу последнего
                            //элемента в списке в адаптере, и при этом до этого с этого места не были
                            //загружены данные, то загрузить новый данные
                            if (layoutManager.findLastVisibleItemPosition() ==
                                adapter?.currentList?.size?.minus(1) &&
                                dataLastIndex != layoutManager.findLastVisibleItemPosition()
                            ) {
                                Log.e("Scroll", "Need more data detected by scroll")
                                vm.setupDataLastIndex(layoutManager.findLastVisibleItemPosition())
                                vm.getDialogs(layoutManager.findLastVisibleItemPosition())
                            }
                        }
                    }
                    super.onScrolled(recyclerView, dx, dy)
                }

            })
        }
    }

    override fun onDestroyView() {
        binding = null
        adapter = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.e("onDestroy", "onDestroy")
        super.onDestroy()
    }
}