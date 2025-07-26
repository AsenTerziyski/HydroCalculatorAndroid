package com.example.hydrocalculator

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Assuming your R class is generated in this package or imported correctly
// If not, you might need: import com.yourpackage.R (replace with your actual package)

@Composable
fun SplashScreen() { // Current empty function from your file
    // You could call your new fancy text Composable here if this is where it's needed
    // For example:
     FancyTextWithImageBelow()
}

@Composable
fun FancyTextWithImageBelow() {
    Column(
        modifier = Modifier
            .fillMaxWidth() // Take full width
            .padding(16.dp), // Add some padding around the column
        horizontalAlignment = Alignment.CenterHorizontally, // Center content horizontally
        verticalArrangement = Arrangement.Center // Center content vertically within its bounds
    ) {
        Text(
            text = "Hello",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge.copy( // Start with a Material theme style
                // If you added a custom font:
                // fontFamily = FontFamily(Font(R.font.your_fancy_font)), // Replace your_fancy_font
                fontWeight = FontWeight.Bold, // Make it bold
                fontSize = 48.sp, // Larger font size
                color = Color(0xFF00695C) // Example: A deep teal color
            )
        )
        Text(
            text = "Hydrocalculator",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium.copy(
                // If you added a custom font:
                // fontFamily = FontFamily(Font(R.font.your_fancy_font)), // Replace your_fancy_font
                fontWeight = FontWeight.Normal,
                fontSize = 36.sp,
                color = Color(0xFF004D40) // Example: A slightly darker teal
            ),
            modifier = Modifier.padding(top = 4.dp) // Little space between "Hello" and "Hydrocalculator"
        )

        Spacer(modifier = Modifier.height(24.dp)) // Space between text and image

        Image(
            painter = painterResource(id = R.drawable.hidro_calculator_splash), // REPLACE with your image drawable
            contentDescription = "Descriptive text for the image", // For accessibility
            modifier = Modifier.size(180.dp) // Adjust size as needed
        )
    }
}

// Preview for your new Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF) // White background for preview
@Composable
fun FancyTextWithImageBelowPreview() {
    MaterialTheme { // Wrap in MaterialTheme for previews to get default styles
        FancyTextWithImageBelow()
    }
}


