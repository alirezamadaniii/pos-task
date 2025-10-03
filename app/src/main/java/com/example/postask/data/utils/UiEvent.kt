package com.example.postask.data.utils

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
}
