package com.example.hodkostkou

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    // Symboly jednotlivých hodnot kostky (index 0 = hodnota 1).
    private val diceSymbols = listOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")

    // Handler na hlavním vlákně slouží k odložení kroků animace o 250 ms.
    private val handler = Handler(Looper.getMainLooper())

    // Počet náhodných změn kostky během animace.
    private val animationSteps = 10
    private val delayMs = 250L

    // SoundPool je určený pro krátké zvuky; ID zvuků získáme při načtení.
    private lateinit var soundPool: SoundPool
    private var clickSoundId = 0
    private var landSoundId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Odsazení obsahu od systémových lišt.
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.llMain)
        ) { view, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        setupSounds()

        // Přístup k prvkům layoutu pomocí findViewById.
        val tvDice = findViewById<TextView>(R.id.tvDice)
        val btnRoll = findViewById<Button>(R.id.btnRoll)

        // Kliknutí na tlačítko spustí hod.
        btnRoll.setOnClickListener {
            rollDice(tvDice, btnRoll)
        }
    }

    // Vytvoří SoundPool a načte zvuky ze složky res/raw.
    private fun setupSounds() {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(attributes)
            .build()

        clickSoundId = soundPool.load(this, R.raw.dice_click, 1)
        landSoundId = soundPool.load(this, R.raw.dice_land, 1)
    }

    // Přehraje zvuk s mírně náhodnou rychlostí, aby neznělo pořád stejně.
    private fun playSound(soundId: Int) {
        val rate = 0.9f + Random.nextFloat() * 0.2f
        soundPool.play(soundId, 1f, 1f, 1, 0, rate)
    }

    // Spustí animaci hodu: tlačítko se zakáže a začne první krok.
    private fun rollDice(tvDice: TextView, btnRoll: Button) {
        btnRoll.isEnabled = false
        animateStep(tvDice, btnRoll, 0)
    }

    // Jeden krok animace: nový symbol, cvaknutí a plný obrat kostky
    // za 250 ms. Poté se zavolá další krok.
    private fun animateStep(tvDice: TextView, btnRoll: Button, step: Int) {
        if (step < animationSteps) {
            tvDice.text = diceSymbols.random()
            playSound(clickSoundId)

            // Otočení o 360° konstantní rychlostí (LinearInterpolator),
            // aby na sebe jednotlivé obraty plynule navazovaly.
            tvDice.rotation = 0f
            tvDice.animate()
                .rotation(360f)
                .setDuration(delayMs)
                .setInterpolator(LinearInterpolator())
                .start()

            handler.postDelayed({
                animateStep(tvDice, btnRoll, step + 1)
            }, delayMs)
        } else {
            showResult(tvDice, btnRoll)
        }
    }

    // Výsledný hod: zvuk dopadu a "poskočení" kostky (zvětšení a zpět).
    private fun showResult(tvDice: TextView, btnRoll: Button) {
        tvDice.rotation = 0f
        tvDice.text = diceSymbols.random()
        playSound(landSoundId)

        tvDice.animate()
            .scaleX(1.3f).scaleY(1.3f)
            .setDuration(100)
            .withEndAction {
                tvDice.animate()
                    .scaleX(1f).scaleY(1f)
                    .setDuration(200)
                    .setInterpolator(OvershootInterpolator())
                    // Tlačítko povolíme až po dokončení animace.
                    .withEndAction { btnRoll.isEnabled = true }
                    .start()
            }
            .start()
    }

    // Úklid: zrušení čekajících kroků a uvolnění zvukových zdrojů.
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        soundPool.release()
    }
}