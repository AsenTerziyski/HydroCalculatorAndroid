package com.example.hydrocalculator.views.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hydrocalculator.utils.CalculationTypeItem
import com.example.hydrocalculator.utils.calculationTypeItems
import com.example.hydrocalculator.views.calculationtype.CalculationTypeCard

@Composable
fun CalculationTypeScreen(onCardClick: (CalculationTypeItem) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        val pagerState = rememberPagerState(
            pageCount = { calculationTypeItems.size })

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val item = calculationTypeItems[page]
                CalculationTypeCard(
                    title = item.title,
                    description = item.description
                ) {
                    onCardClick.invoke(item)
                }
            }

            PageIndicator(
                pageCount = pagerState.pageCount,
                currentPage = pagerState.currentPage,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            )
        }
    }
}

@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp) // 2. Add spacing between the dots
    ) {
        repeat(pageCount) { pageIndex ->
            val color = if (pageIndex == currentPage) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            }
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PageIndicatorPreview() {
    PageIndicator(3, 1)
}
