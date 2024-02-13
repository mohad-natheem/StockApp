package com.example.stockapp.presentation.company_info

import com.example.stockapp.domain.model.CompanyInfo
import com.example.stockapp.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInfos : List<IntradayInfo> = emptyList(),
    val company : CompanyInfo? = null,
    val isLoading : Boolean = false,
    val error : String? = null
)
