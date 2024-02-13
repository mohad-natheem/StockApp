package com.example.stockapp.presentation.company_listings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun CompanyListingsScreen(
    navController: NavController,
    viewModel : CompanyListingViewModel = hiltViewModel()
    ) {
    val state = viewModel.state
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = viewModel.state.isRefreshing )
    Column (
        modifier = Modifier.fillMaxSize()
    ){
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = {
                viewModel.onEvent(
                    CompanyListingEvent.OnSearchQueryChange(it)
                )
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(text = "Search....")
            },
            maxLines = 1,
            singleLine = true
        )
//        SwipeRefresh(
//            state = swipeRefreshState,
//            onRefresh = {
//                viewModel.onEvent(CompanyListingEvent.Refresh)
//            }
//        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ){
                items(state.companies.size){i ->
                    val company = state.companies[i]
                    CompanyItem(
                        company = company,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("company_info_screen/${company.symbol}")

                            }
                            .padding(16.dp)
                    )
                    if(i < state.companies.size){
                        Divider(modifier = Modifier.padding(
                            horizontal = 16.dp
                        ))
                    }

                }
            }
            if(state.companies.isEmpty()){
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Error",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                }
            }

//        }

    }

}