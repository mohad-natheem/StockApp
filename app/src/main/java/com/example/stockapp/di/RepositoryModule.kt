package com.example.stockapp.di

import com.example.stockapp.data.csv.CompanyListingsParser
import com.example.stockapp.data.csv.CsvParser
import com.example.stockapp.data.csv.IntradayInfoParser
import com.example.stockapp.data.respository.StockRepositoryImpl
import com.example.stockapp.domain.model.CompanyListing
import com.example.stockapp.domain.model.IntradayInfo
import com.example.stockapp.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingParser(
        companyListingsParser: CompanyListingsParser
    ):CsvParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun  bindIntradayInfoParser(
        intradayInfoParser: IntradayInfoParser
    ):CsvParser<IntradayInfo>

    @Binds
    @Singleton
    abstract fun bindsStockRepository(
        stockRepositoryImpl:StockRepositoryImpl
    ):StockRepository


}