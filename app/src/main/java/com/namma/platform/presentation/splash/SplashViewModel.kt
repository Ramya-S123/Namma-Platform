package com.namma.platform.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namma.platform.data.local.DataInitializer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dataInitializer: DataInitializer
) : ViewModel() {

    init {
        viewModelScope.launch {
            dataInitializer.initializeIfNeeded()
        }
    }
}
