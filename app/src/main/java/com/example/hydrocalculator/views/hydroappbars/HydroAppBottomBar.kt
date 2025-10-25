package com.example.hydrocalculator.views.hydroappbars

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HydroAppBottomBar(

) {

    BottomAppBar(
        containerColor = Color.Transparent,
    ) {

        Spacer(modifier = Modifier.width(4.dp))

        BottomBarItem(
            modifier = Modifier.weight(1f),
            {
                Text(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(2.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp,
                    lineHeight = 16.sp,
                    text = "Pressurized Pipes"
                )
            }
        )

        Spacer(modifier = Modifier.width(4.dp))

        BottomBarItem(
            modifier = Modifier.weight(1f),
            {
                Text(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(2.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp,
                    lineHeight = 16.sp,

                    text = "Gravity pipes"
                )
            }
        )

        Spacer(modifier = Modifier.width(4.dp))

        IconButton(
            modifier = Modifier.weight(1f),
            onClick = {}
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Filled.PowerSettingsNew,
                    contentDescription = "Switch Off",
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Switch Off",
                    fontSize = 10.sp,
                    color = Color.Red,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        Spacer(modifier = Modifier.width(4.dp))

        BottomBarItem(
            modifier = Modifier.weight(1f),
            {
                Text(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(2.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp,
                    text = "Results"
                )
            }
        )

        Spacer(modifier = Modifier.width(4.dp))

        BottomBarItem(
            modifier = Modifier.weight(1f),
            {
                Text(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(2.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp,
                    text = "Contacts"
                )
            }
        )
    }
}

@Composable
fun BottomBarItem(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val itemShape = RoundedCornerShape(8.dp)

    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(vertical = 2.dp, horizontal = 4.dp)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.2f),
                shape = itemShape
            )
            .padding(horizontal = 4.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Preview
@Composable
fun HydroAppBottomBarPreview() {
    HydroAppBottomBar()
}
