package com.example.stockapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.stockapp.presentation.company_info.CompanyInfoScreen
import com.example.stockapp.presentation.company_listings.CompanyListingsScreen
import com.example.stockapp.ui.theme.StockAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockAppTheme {

                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination ="company_listings_screen" ){
                        composable(route = "company_listings_screen"){
                            CompanyListingsScreen(navController=navController)
                        }

                        composable(
                            route = "company_info_screen/{symbol}",
                            arguments = listOf(
                                navArgument("symbol"){
                                    type = NavType.StringType
                                }
                            )
                        ){
                            val symbol = remember{
                                it.arguments?.getString("symbol")
                            }
                            CompanyInfoScreen(symbol = symbol?:"")
//                            CompanyInfoDemo(symbol?:"No symbol")

                        }
                    }


                }
            }
        }
    }
}

