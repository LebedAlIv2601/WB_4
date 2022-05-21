package com.example.wb_4.presentation.dialog_list

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wb_4.presentation.utils.Resource
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

        //Устанавливаем слушатель для метода, получающего первые 10 элементов начиная с 0 индекса
        setupDataObserver(0)

        //Настраиваем слушателей
        setupListeners()
        super.onViewCreated(view, savedInstanceState)
    }

    //Метод для установки слушателя на метод для обновления списка диалогов
    private fun setupUpdateDataObserver() {
        vm.updateDialogList().observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when(resource){
                    is Resource.Success -> {

                        adapter?.submitList(it.data)

                        //Разрешаем загрузку элементов и устанвливаем последний индекс 0
                        loadPermission = true
                        dataLastIndex = 0

                        binding?.dialogListSwipeRefresh?.isRefreshing = false
                    }

                    is Resource.Error -> {
                        Toast.makeText(context, "Some error", Toast.LENGTH_SHORT).show()
                        binding?.dialogListSwipeRefresh?.isRefreshing = false
                    }

                    is Resource.Loading -> {
                        binding?.dialogListSwipeRefresh?.isRefreshing = true
                    }
                }
            }
        })
    }

    private fun setupListeners() {
        binding?.dialogListSwipeRefresh?.setOnRefreshListener {
            Log.e("Refresh", "Refresh is called")

            //Вызываем метод для установки слушателя для метода обновления списка
            setupUpdateDataObserver()
        }
    }

    //Метод установки слушателя для метода получения части списка диалогов
    private fun setupDataObserver(lastId: Int) {
        vm.getDialogList(lastId).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when(resource){
                    is Resource.Success -> {
                        //Если список полученных данных не пустой, то соединяем полученные данные
                        //со списком уже имещихся данных, иначе устанавливаем
                        // флаг разрешения загрузки false
                        if(it.data?.isNotEmpty() == true){
                            val prevList = adapter?.currentList?.toMutableList()
                            val resultList = mutableListOf<CompanionUserDomain>()
                            prevList?.let { it1 -> resultList.addAll(it1) }
                            resultList.addAll(it.data)
                            adapter?.submitList(resultList)
                        } else{
                            loadPermission = false
                        }
                        binding?.dialogListSwipeRefresh?.isRefreshing = false
                    }

                    is Resource.Error -> {
                        Toast.makeText(context, "Some error", Toast.LENGTH_SHORT).show()
                        binding?.dialogListSwipeRefresh?.isRefreshing = false
                    }

                    is Resource.Loading -> {
                        binding?.dialogListSwipeRefresh?.isRefreshing = true
                    }
                }
            }
        })
    }

    private fun setupRecyclerView(){
        adapter = DialogListAdapter()

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
                                dataLastIndex = layoutManager.findLastVisibleItemPosition()
                                setupDataObserver(layoutManager.findLastVisibleItemPosition())
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
}