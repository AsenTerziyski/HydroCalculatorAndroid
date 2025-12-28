package com.asentt.hydrocalculator.ui.views.screens.calculationtype

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.asentt.hydrocalculator.ui.theme.HydroCalculatorTheme
import com.asentt.hydrocalculator.ui.theme.HydroCyan
import com.asentt.hydrocalculator.ui.theme.HydroGreen
import com.asentt.hydrocalculator.ui.theme.White
import com.asentt.hydrocalculator.utils.CalculationTypeItem
import com.asentt.hydrocalculator.utils.calculationTypeItems

@Composable
fun CalculationTypeScreen(badgeCount: Int = 0, onCardClick: (CalculationTypeItem) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val pagerState = rememberPagerState(pageCount = { calculationTypeItems.size })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) { page ->
                val item = calculationTypeItems[page]

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CalculationTypeCard(
                        title = item.title,
                        description = item.description,
                        hasBadge = item.hasBadge,
                        badgeCount = badgeCount,
                    ) {
                        onCardClick.invoke(item)
                    }
                }
            }
            PageIndicator(
                pageCount = pagerState.pageCount,
                currentPage = pagerState.currentPage,
                modifier = Modifier.padding(vertical = 32.dp)
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
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(pageCount) { pageIndex ->
            val color = if (pageIndex == currentPage) {
                HydroGreen.copy(alpha = 2F)
            } else {
                HydroCyan.copy(0.7F)
            }
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .border(if (pageIndex == currentPage) 1.dp else 0.dp, White, CircleShape)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
fun CalculationTypeScreenPreview() {
    HydroCalculatorTheme {
        CalculationTypeScreen(badgeCount = 3) {}
    }
}

@Composable
@Preview(showBackground = true)
fun PageIndicatorPreview() {
    PageIndicator(3, 1)
}
