package com.example.cinepass

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cinepass.ui.theme.CinePassTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CinePassTheme {
                LoginScreen(
                    onRegisterClick = { navigateToRegister() }
                )
            }
        }
    }

    // Navigate to RegisterActivity
    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun LoginScreen(onRegisterClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Background Image and Overlay
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val background: Painter = painterResource(id = R.drawable.background)

        // Background image
        Image(
            painter = background,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Semi-transparent overlay to reduce contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.2f)) // Light overlay for low contrast
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo image at the top
            Image(
                painter = painterResource(id = R.drawable.logo), // Replace with actual logo resource ID
                contentDescription = "Logo",
                modifier = Modifier
                    .size(300.dp) // Adjust size as needed
                    .padding(bottom = 24.dp)
            )

            // Email Input Field
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.small // Optional: shape with no round corners
            )

            // Continue Button
            Button(
                onClick = {
                    if (email.isNotEmpty()) {
                        Toast.makeText(context, "Email entered: $email", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Continue",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // "Or login with" separator
            Text(
                text = "or login with",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Mobile and Google login options as styled buttons
            LoginOption(
                text = "Mobile",
                backgroundColor = Color.Gray,
                onClick = { /* Handle login with mobile action */ }
            )
            Spacer(modifier = Modifier.height(8.dp))
            LoginOption(
                text = "Google",
                backgroundColor = Color(0xFF4285F4), // Google blue color
                onClick = { /* Handle login with Google action */ }
            )

            // Register Option
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Don't have an account? Register",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onRegisterClick() }
            )
        }
    }
}

// LoginOption composable for reusable button style
@Composable
fun LoginOption(text: String, backgroundColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
