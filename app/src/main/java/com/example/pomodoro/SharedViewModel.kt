package com.example.pomodoro

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val currentTimeEnd = MutableLiveData<String>()
}
