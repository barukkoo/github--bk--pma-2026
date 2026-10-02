package com.example.uhodnicislocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                GuessNumberGame(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun GuessNumberGame(modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current

    // Stavy aplikace (při změně hodnoty dochází k automatickému překreslení UI)
    var secretNumber by remember { mutableIntStateOf(Random.nextInt(1, 101)) }
    var inputGuess by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf("Zadej číslo a zkus štěstí!") }
    var messageColor by remember { mutableStateOf(Color.DarkGray) }
    var attempts by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    val onProcessGuess: () -> Unit = {
        focusManager.clearFocus()

        if (isGameOver) {
            secretNumber = Random.nextInt(1, 101)
            attempts = 0
            isGameOver = false
            resultMessage = "Nová hra! Zadej číslo od 1 do 100."
            messageColor = Color.DarkGray
            inputGuess = ""
        } else {
            val guess = inputGuess.toIntOrNull()
            if (guess == null || guess !in 1..100) {
                resultMessage = "Zadej platné číslo mezi 1 a 100!"
                messageColor = Color.Red
            } else {
                attempts++
                when {
                    guess < secretNumber -> {
                        resultMessage = "PŘIDEJ! Moje číslo je VĚTŠÍ ⬆️"
                        messageColor = Color(0xFF2196F3)
                    }
                    guess > secretNumber -> {
                        resultMessage = "UBER! Moje číslo je MENŠÍ ⬇️"
                        messageColor = Color(0xFFFF9800)
                    }
                    else -> {
                        resultMessage = "TREFIL JSI TO! 🎉 ($secretNumber)"
                        messageColor = Color(0xFF4CAF50)
                        isGameOver = true
                    }
                }
                inputGuess = ""
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Myslím si číslo od 1 do 100",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = inputGuess,
            onValueChange = { if (it.length <= 3) inputGuess = it },
            label = { Text("Tvůj tip") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onProcessGuess() }),
            modifier = Modifier.width(160.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onProcessGuess,
            modifier = Modifier.width(160.dp)
        ) {
            Text(
                text = if (isGameOver) "Hrát znovu" else "Tipnout",
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = resultMessage,
            fontSize = 20.sp,
            color = messageColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Počet pokusů: $attempts",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}