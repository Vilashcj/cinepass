package com.example.cinepass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cinepass.ui.theme.CinePassTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CinePassTheme {
                HomeScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    var totalPrice by remember { mutableStateOf(0) }
    var selectedSeats by remember { mutableStateOf(emptyList<Int>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CinePass") }
            )
        },
        bottomBar = {
            BottomNavigationBar(selectedTab = selectedTab) { selectedTab = it }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> MoviesTab(onBookClick = { selectedTab = 1 })
                1 -> SeatSelectionTab(
                    onConfirmClick = { price, seats ->
                        totalPrice = price
                        selectedSeats = seats
                        selectedTab = 2
                    },
                    onBackClick = { selectedTab = 0 }
                )
                2 -> PaymentTab(
                    totalPrice = totalPrice,
                    selectedSeats = selectedSeats,
                    onBackClick = { selectedTab = 1 },
                    onPaymentSuccess = { selectedTab = 0 } // Navigates back to home screen
                )
            }
        }
    }
}





@Composable
fun BottomNavigationBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Movies") },
            label = { Text("Movies") }
        )
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = { Icon(Icons.Filled.EventSeat, contentDescription = "Seat Selection") },
            label = { Text("Seats") }
        )
        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = { Icon(Icons.Filled.Payment, contentDescription = "Payment") },
            label = { Text("Payment") }
        )
    }
}

@Composable
fun MoviesTab(onBookClick: () -> Unit) {
    val movies = listOf(
        Pair("Movie A", R.drawable.gaurd),
        Pair("Movie B", R.drawable.batman),
        Pair("Movie C", R.drawable.infinity_war),
        Pair("Movie D", R.drawable.spiderman)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Choose your favorite movie!", style = MaterialTheme.typography.headlineMedium)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            items(movies) { movie ->
                MoviePosterItem(movieName = movie.first, posterRes = movie.second, onBookClick)
            }
        }
    }
}

@Composable
fun MoviePosterItem(movieName: String, posterRes: Int, onBookClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onBookClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = posterRes),
                contentDescription = movieName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Button(
                onClick = { onBookClick() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            ) {
                Text("Book")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeatSelectionTab(onConfirmClick: (Int, List<Int>) -> Unit, onBackClick: () -> Unit) {
    val rows = 5
    val cols = 6
    val selectedSeats = remember { mutableStateListOf<Int>() }
    var selectedCategory by remember { mutableStateOf("Single") }
    val maxSeatsAllowed = when (selectedCategory) {
        "Single" -> 1
        "Double" -> 2
        "Triple" -> 3
        else -> 1
    }
    val ticketPrice = 8
    val totalPrice = selectedSeats.size * ticketPrice

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Seats") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Select your seats", style = MaterialTheme.typography.headlineMedium)

            // Seat Category Selection
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryButton("Single", selectedCategory) { selectedCategory = it; selectedSeats.clear() }
                CategoryButton("Double", selectedCategory) { selectedCategory = it; selectedSeats.clear() }
                CategoryButton("Triple", selectedCategory) { selectedCategory = it; selectedSeats.clear() }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(cols),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(rows * cols) { index ->
                    val isSelected = selectedSeats.contains(index + 1)

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp)
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable {
                                if (isSelected) {
                                    selectedSeats.remove(index + 1)
                                } else if (selectedSeats.size < maxSeatsAllowed) {
                                    selectedSeats.add(index + 1)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${index + 1}")
                    }
                }
            }

            Text("Total Price: £$totalPrice", style = MaterialTheme.typography.bodyLarge)

            Button(
                onClick = { onConfirmClick(totalPrice, selectedSeats) },
                enabled = selectedSeats.size == maxSeatsAllowed
            ) {
                Text("Confirm Seats")
            }
        }
    }
}





@Composable
fun CategoryButton(category: String, selectedCategory: String, onClick: (String) -> Unit) {
    Button(
        onClick = { onClick(category) },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (category == selectedCategory) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(category)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentTab(totalPrice: Int, selectedSeats: List<Int>, onBackClick: () -> Unit, onPaymentSuccess: () -> Unit) {
    var selectedPaymentMethod by remember { mutableStateOf("Credit Card") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var showPopup by remember { mutableStateOf(false) }

    val cardNumberRegex = Regex("^\\d{16}$")
    val expiryDateRegex = Regex("^(0[1-9]|1[0-2])/\\d{2}$")
    val cvvRegex = Regex("^\\d{3}$")
    val isCardValid = cardNumber.matches(cardNumberRegex)
    val isExpiryValid = expiryDate.matches(expiryDateRegex)
    val isCvvValid = cvv.matches(cvvRegex)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Complete your payment", style = MaterialTheme.typography.headlineMedium)

            Text("Total Price: £$totalPrice", style = MaterialTheme.typography.bodyLarge)

            Text(
                "Selected Seats: ${selectedSeats.joinToString(", ")}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text("Select Payment Method", style = MaterialTheme.typography.bodyMedium)

            Column {
                PaymentMethodOption("Credit Card", selectedPaymentMethod) { selectedPaymentMethod = it }
                PaymentMethodOption("PayPal", selectedPaymentMethod) { selectedPaymentMethod = it }
                PaymentMethodOption("Google Pay", selectedPaymentMethod) { selectedPaymentMethod = it }
            }

            if (selectedPaymentMethod == "Credit Card") {
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { if (it.length <= 16) cardNumber = it },
                    label = { Text("Card Number") },
                    placeholder = { Text("1234 5678 9012 3456") },
                    isError = !isCardValid && cardNumber.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = { if (it.length <= 5) expiryDate = it },
                    label = { Text("Expiry Date") },
                    placeholder = { Text("MM/YY") },
                    isError = !isExpiryValid && expiryDate.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = cvv,
                    onValueChange = { if (it.length <= 3) cvv = it },
                    label = { Text("CVV") },
                    placeholder = { Text("123") },
                    isError = !isCvvValid && cvv.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = { showPopup = true },
                enabled = selectedPaymentMethod != "Credit Card" || (isCardValid && isExpiryValid && isCvvValid),
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text("Confirm Payment")
            }

            if (showPopup) {
                AlertDialog(
                    onDismissRequest = { showPopup = false },
                    title = { Text("Payment Completed") },
                    text = { Text("You'll receive the ticket via email.") },
                    confirmButton = {
                        Button(onClick = {
                            showPopup = false
                            onPaymentSuccess() // Navigate to home
                        }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}








@Composable
fun PaymentMethodOption(
    method: String,
    selectedMethod: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(method) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = method == selectedMethod,
            onClick = { onSelect(method) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(method)
    }
}
