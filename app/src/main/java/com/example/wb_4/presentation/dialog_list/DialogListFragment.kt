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
import com.example.wb_4.Resource
import com.example.wb_4.appComponent
import com.example.wb_4.databinding.FragmentDialogListBinding
import com.example.wb_4.domain.model.CompanionUserDomain
import com.example.wb_4.domain.model.MessageDomain
import javax.inject.Inject


class DialogListFragment : Fragment() {

    private var binding: FragmentDialogListBinding? = null
    private var adapter: DialogListAdapter? = null
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
        setupRecyclerView()
        setupDataObservers()
        setupListeners()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupListeners() {
        binding?.dialogListSwipeRefresh?.setOnRefreshListener {
            Log.e("Refresh", "Refresh is called")
            setupDataObservers()
        }
    }

    private fun setupDataObservers() {
        vm.updateDialogList().observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when(resource){
                    is Resource.Success -> {
                        adapter?.submitList(it.data)
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
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}