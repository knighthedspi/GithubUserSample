package com.example.githubusersample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.githubusersample.R
import com.example.githubusersample.databinding.FragmentUserInfoBinding
import com.example.githubusersample.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInfoFragment : Fragment() {
    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var binding: FragmentUserInfoBinding
    private val safeArgs: UserInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DataBindingUtil.inflate<FragmentUserInfoBinding>(
            layoutInflater,
            R.layout.fragment_user_info,
            container,
            false
        ).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setupBinding()
        setupObserver()
    }

    private fun init() {
        viewModel.getUserInfo(safeArgs.url)
    }

    private fun setupBinding() {
        (activity as AppCompatActivity).supportActionBar?.apply {
            setTitle(R.string.user_info_title)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        binding.retryButton.setOnClickListener {
            init()
        }
    }

    private fun setupObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.isLoading = it
            binding.isError = false
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            binding.isError = true
        }
        viewModel.userInfoLiveData.observe(viewLifecycleOwner) {
            binding.user = it
            binding.isError = false
        }
    }
}