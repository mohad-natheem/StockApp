package com.example.stockapp.presentation.company_info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockapp.domain.repository.StockRepository
import com.example.stockapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle : SavedStateHandle,
    private val repository: StockRepository

):ViewModel() {

    var state by mutableStateOf(CompanyInfoState())
    lateinit var symbol1 : String
    init {
        viewModelScope.launch {
//            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            val symbol = "AAA"
            symbol1 = symbol
            state = state.copy(isLoading = true)
            val companyInfoResult = async { repository.getCompanyInfo(symbol) }
            val intradayResult = async { repository.getIntradayInfo(symbol) }

            when(val result = companyInfoResult.await()){
                is Resource.Success ->{
                    state = state.copy(
                        company = result.data,
                        isLoading = false,
                        error = null
                    )

                }
                is Resource.Error ->{
                    state = state.copy(
                        isLoading = false,
                        error = result.message,
                        company = null
                    )

                }
                else -> Unit
            }
            when(val result = intradayResult.await()){
                is Resource.Success ->{
                    state = state.copy(
                        stockInfos = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )

                }
                is Resource.Error ->{
                    state = state.copy(
                        isLoading = false,
                        error = result.message,
                        company = null
                    )

                }
                else -> Unit
            }
        }


    }
    fun Demofun(symbol:String){

    }
}