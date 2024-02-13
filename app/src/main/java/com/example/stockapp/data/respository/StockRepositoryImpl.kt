package com.example.stockapp.data.respository

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.example.stockapp.data.csv.CsvParser
import com.example.stockapp.data.local.StockDatabase
import com.example.stockapp.data.mapper.toCompanyInfo
import com.example.stockapp.data.mapper.toCompanyListing
import com.example.stockapp.data.mapper.toCompanyListingEntity
import com.example.stockapp.data.remote.StockApi
import com.example.stockapp.domain.model.CompanyInfo
import com.example.stockapp.domain.model.CompanyListing
import com.example.stockapp.domain.model.IntradayInfo
import com.example.stockapp.domain.repository.StockRepository
import com.example.stockapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    val api : StockApi,
    val db : StockDatabase,
    val companyListingParser : CsvParser<CompanyListing>,
    val intradayInfoParser : CsvParser<IntradayInfo>

) : StockRepository{
    private val dao = db.dao
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListings(query)
            emit(Resource.Success(
                data = localListings.map { it.toCompanyListing() }
            ))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if(shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListings = try {
                val response = api.getListings()
                companyListingParser.parse(response.byteStream())
            } catch(e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }

            remoteListings?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListing(
                    listings.map { it.toCompanyListingEntity() }
                )
                emit(Resource.Success(
                    data = dao
                        .searchCompanyListings("")
                        .map { it.toCompanyListing() }
                ))
                emit(Resource.Loading(false))
            }
        }

    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response = api.getIntradayInfo(symbol)
            val results = intradayInfoParser.parse(response.byteStream())

            Resource.Success(results)

        }catch (e : IOException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info"
            )

        }catch (e : HttpException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info"
            )

        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol)
            Resource.Success(result.toCompanyInfo())

        }catch (e : IOException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load company info"
            )

        }catch (e : HttpException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load company info"
            )

        }    }

}