package com.example.cinepass

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cinepass.ui.theme.CinePassTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CinePassTheme {
                SplashScreen(onSplashComplete = {
                    // Navigate to LoginActivity after splash screen completes
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish() // Close MainActivity to remove from back stack
                })
            }
        }
    }
}

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    // Launches the timeout effect for the splash screen
    LaunchedEffect(Unit) {
        delay(2000) // Display splash for 2 seconds
        onSplashComplete()
    }

    // Displays the splash screen content
    SplashContent()
}

@Composable
fun SplashContent() {
    // Background image for the splash screen
    val background: Painter = painterResource(id = R.drawable.background)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp), // Full screen without padding
        contentAlignment = Alignment.Center
    ) {
        // Background Image filling the screen
        Image(
            painter = background,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Crop to fill the screen proportionally
        )

        // Semi-transparent overlay to improve contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.4f)) // 40% opacity black overlay
        )

        // Logo in the center of the screen
        Image(
            painter = painterResource(id = R.drawable.logo), // Replace with your logo's resource ID
            contentDescription = "Logo",
            modifier = Modifier.size(350.dp), // Adjust size as desired
            contentScale = ContentScale.Fit // Fit the logo proportionally
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    CinePassTheme {
        SplashContent()
    }
}
