package com.example.postask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postask.data.utils.IsoUtils
import com.example.postask.data.utils.UiEvent
import com.example.postask.domain.model.IsoField
import com.example.postask.domain.usecase.IsoInteractorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IsoViewModel @Inject constructor(
    private val interactor: IsoInteractorUseCase
) : ViewModel() {

    val pan = MutableStateFlow("")
    val amount = MutableStateFlow("")
    val fields = MutableStateFlow<List<IsoField>>(emptyList())
    val packedHex = MutableStateFlow("")
    private val _events = Channel<UiEvent>()
    val events = _events.receiveAsFlow()

    fun onPanChange(v: String) {
        pan.value = v
    }

    fun onAmountChange(v: String) {
        amount.value = v
    }

    // Builds an ISO 8583 message from the current PAN
    fun buildMessage() = viewModelScope.launch {
        val currentPan = pan.value
        val currentAmount = amount.value

        if (currentPan.isBlank() || currentAmount.isBlank()) {
            _events.send(UiEvent.ShowToast("شماره کارت و مبلغ را وارد کنید"))
            return@launch
        }
        if (currentPan.length != 16) {
            _events.send(UiEvent.ShowToast("شماره کارت را به صورت صحیح وارد کنید"))
            return@launch
        }

        // amount Updates packedHex and fields, and sends a toast event
        try {
            val (iso, packed) = interactor.buildIsoMessage(currentPan, currentAmount)
            packedHex.value = IsoUtils.bytesToHex(packed)
            val list = mutableListOf<IsoField>()
            list.add(IsoField(0, iso.mti, "MTI"))
            for (i in 2..128) iso.getString(i)?.let { list.add(IsoField(i, it)) }
            fields.value = list
            _events.send(UiEvent.ShowToast("پیام ISO ساخته شد"))
        } catch (e: Exception) {
            _events.send(UiEvent.ShowToast("خطا: ${e.message}"))
        }
    }


    // Parses a hex ISO 8583 message, updates fields and packedHex and sends a toast event
    fun parseMessage(hex: String) = viewModelScope.launch {
        try {
            val bytes = hex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
            val iso = interactor.parseIsoMessage(bytes)
            packedHex.value = hex
            val list = mutableListOf<IsoField>()
            list.add(IsoField(0, iso.mti, "MTI"))
            for (i in 2..128) iso.getString(i)?.let { list.add(IsoField(i, it)) }
            fields.value = list
            _events.send(UiEvent.ShowToast("پیام ISO پارس شد"))
        } catch (e: Exception) {
            _events.send(UiEvent.ShowToast("خطا در پارس کردن: ${e.message}"))
        }
    }
}
