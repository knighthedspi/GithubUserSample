package com.example.githubusersample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.githubusersample.R
import com.example.githubusersample.databinding.FragmentUsersBinding
import com.example.githubusersample.ui.adapter.LoadStateAdapter
import com.example.githubusersample.ui.adapter.UserAdapter
import com.example.githubusersample.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersFragment : Fragment() {
    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var binding: FragmentUsersBinding
    private lateinit var adapter: UserAdapter
    private lateinit var header: LoadStateAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DataBindingUtil.inflate<FragmentUsersBinding>(
            layoutInflater,
            R.layout.fragment_users,
            container,
            false
        ).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
        setupObserver()
    }

    private fun setupBinding() {
        (activity as AppCompatActivity).supportActionBar?.apply {
            setTitle(R.string.users_title)
            setHomeButtonEnabled(false)
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }
        adapter = UserAdapter(requireContext()) {
            val action = UsersFragmentDirections.nextAction(it.url)
            findNavController().navigate(action)
        }
        header = LoadStateAdapter(requireContext()) {
            adapter.retry()
        }
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = header,
            footer = LoadStateAdapter(requireContext()) {
                adapter.retry()
            }
        )
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            viewModel.users.collectLatest(adapter::submitData)
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collect {
                header.loadState = it.mediator
                    ?.refresh
                    ?.takeIf { state -> state is LoadState.Error && adapter.itemCount > 0 }
                    ?: it.prepend
                binding.list.isVisible = it.source.refresh is LoadState.NotLoading || it.mediator?.refresh is LoadState.NotLoading
                binding.isLoading = it.mediator?.refresh is LoadState.Loading
                binding.isError = it.mediator?.refresh is LoadState.Error && adapter.itemCount == 0
                val errorState = it.source.append as? LoadState.Error
                    ?: it.source.prepend as? LoadState.Error
                    ?: it.append as? LoadState.Error
                    ?: it.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        requireContext(),
                        "Error ${errorState.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}