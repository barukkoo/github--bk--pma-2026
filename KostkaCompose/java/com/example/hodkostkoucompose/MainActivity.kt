package com.example.hodkostkoucompose

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.random.Random

// Symboly jednotlivých hodnot kostky (index 0 = hodnota 1).
val diceSymbols = listOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DiceScreen()
                }
            }
        }
    }
}

@Composable
fun DiceScreen() {
    // Stav: aktuálně zobrazený symbol a příznak běžící animace.
    var diceSymbol by remember { mutableStateOf(diceSymbols[0]) }
    var isRolling by remember { mutableStateOf(false) }

    // Animovatelné hodnoty: úhel otočení (0–360°) a měřítko kostky.
    val rotation = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // SoundPool se vytvoří jednou a zvuky se načtou z res/raw.
    val soundPool = remember {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(attributes)
            .build()
    }
    val clickSoundId = remember { soundPool.load(context, R.raw.dice_click, 1) }
    val landSoundId = remember { soundPool.load(context, R.raw.dice_land, 1) }

    // Uvolnění zvukových zdrojů při opuštění obrazovky.
    DisposableEffect(Unit) {
        onDispose { soundPool.release() }
    }

    // Přehraje zvuk s mírně náhodnou rychlostí.
    fun playSound(soundId: Int) {
        val rate = 0.9f + Random.nextFloat() * 0.2f
        soundPool.play(soundId, 1f, 1f, 1, 0, rate)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(), // odsazení od systémových lišt
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // vystředění obsahu
    ) {
        // Nadpis
        Text(text = "Hoď kostkou", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(24.dp))

        // Symbol kostky; otočení a měřítko se aplikují bez nového rozvržení.
        Text(
            text = diceSymbol,
            fontSize = 150.sp,
            modifier = Modifier.graphicsLayer {
                rotationZ = rotation.value
                scaleX = scale.value
                scaleY = scale.value
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tlačítko je během animace zakázané.
        Button(
            enabled = !isRolling,
            onClick = {
                scope.launch {
                    isRolling = true

                    // Deset kroků: nový symbol, cvaknutí a plný obrat za 250 ms.
                    // animateTo zároveň zajišťuje prodlevu 250 ms.
                    repeat(10) {
                        diceSymbol = diceSymbols.random()
                        playSound(clickSoundId)
                        rotation.snapTo(0f)
                        rotation.animateTo(
                            targetValue = 360f,
                            animationSpec = tween(250, easing = LinearEasing)
                        )
                    }

                    // Výsledný hod: zvuk dopadu a "poskočení" kostky.
                    rotation.snapTo(0f)
                    diceSymbol = diceSymbols.random()
                    playSound(landSoundId)
                    scale.animateTo(1.3f, tween(100))
                    scale.animateTo(
                        1f,
                        spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    )

                    isRolling = false
                }
            }
        ) {
            Text(text = "Hodit")
        }
    }
}