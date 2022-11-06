package com.example.githubusersample.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.githubusersample.R
import com.example.githubusersample.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val popped = findNavController(R.id.my_nav_host_fragment).popBackStack()
            if (!popped) {
                finish()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        setSupportActionBar(binding.toolbar)
    }
}