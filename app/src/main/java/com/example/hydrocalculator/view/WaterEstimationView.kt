package com.example.hydrocalculator.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydrocalculator.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HydroCalculatorScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Hydro Calculator Logo",
                tint = Color(0xFF00ADB5),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "HYDRO CALCULATOR",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        CustomDropdownMenu(
            label = "Select",
            options = listOf("Option 1", "Option 2", "Option 3"),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        TextField(
            value = "",
            onValueChange = {},
            label = { Text("Estimation result") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color(0xFF00ADB5),
                unfocusedIndicatorColor = Color(0xFF00ADB5),
                focusedContainerColor = Color(0xFF1A1A2E),
                unfocusedContainerColor = Color(0xFF1A1A2E),
                cursorColor = Color(0xFF00ADB5),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Button(
            onClick = { /* Handle calculate action */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF00ADB5))
        ) {
            Text(text = "Calculate", color = Color.White)
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconWithLabel(iconRes = R.drawable.ic_launcher_background, label = "Water supply")
            IconWithLabel(iconRes = R.drawable.ic_launcher_background, label = "Sewer")
            IconWithLabel(iconRes = R.drawable.ic_launcher_background, label = "History")
        }
    }
}

@Composable
fun Spacer(modifier: Any) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownMenu(label: String, options: List<String>, modifier: Modifier = Modifier) {
    var expanded = remember { mutableStateOf(false) }
    var selectedOption = remember { mutableStateOf(options[0]) }

    Column(modifier = modifier) {

        TextField(value = selectedOption.value, onValueChange = {})


//        TextField(
//            value = selectedOption,
//            onValueChange = {},
//            readOnly = true,
//            label = { Text(label) },
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable { expanded.value = true },
//            textStyle = LocalTextStyle.current.copy(color = Color.White),
//            colors = TextFieldDefaults.colors(
//                focusedContainerColor = Color(0xFF1A1A2E),
//                unfocusedContainerColor = Color(0xFF1A1A2E),
//                focusedIndicatorColor = Color(0xFF00ADB5),
//                unfocusedIndicatorColor = Color(0xFF00ADB5),
//                cursorColor = Color(0xFF00ADB5)
//            )
//        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption.value = option
                        expanded.value = false
                    },
                    text = { Text(text = option) }
                )
            }
        }
    }
}

@Composable
fun IconWithLabel(iconRes: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            tint = Color(0xFF00ADB5),
            modifier = Modifier.size(48.dp)
        )
        Text(text = label, color = Color.White)
    }
}
