package com.example.postask.presentation.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.postask.data.utils.UiEvent
import com.example.postask.databinding.ActivityMainBinding
import com.example.postask.presentation.ui.adapter.IsoFieldAdapter
import com.example.postask.presentation.viewmodel.IsoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: IsoViewModel by viewModels()
    private val adapter = IsoFieldAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecycler()
        setupWatchers()
        setupButtons()
        observe()
    }

    private fun setupRecycler() {
        binding.rvFields.layoutManager = LinearLayoutManager(this)
        binding.rvFields.adapter = adapter
    }

    private fun setupWatchers() {
        binding.etPan.addTextChangedListener { viewModel.onPanChange(it.toString()) }
        binding.etAmount.addTextChangedListener { viewModel.onAmountChange(it.toString()) }
    }

    private fun setupButtons() {
        binding.btnBuildMessage.setOnClickListener {
            binding.etHexMessageInput.setText("")
            viewModel.buildMessage()
        }
        binding.btnParseMessage.setOnClickListener {
            val hex = binding.etHexMessageInput.text.toString()
            if (hex.isNotBlank()) viewModel.parseMessage(hex)
            else Toast.makeText(this, "لطفاً پیام را وارد کنید", Toast.LENGTH_SHORT).show()
        }
    }

    //observe viewmodel data
    private fun observe() {
        lifecycleScope.launchWhenStarted {
            viewModel.packedHex.collect { binding.tvFullMessageHex.text = it }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.fields.collect { adapter.submitList(it) }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.events.collect {
                if (it is UiEvent.ShowToast) Toast.makeText(
                    this@MainActivity,
                    it.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}