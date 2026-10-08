package com.example.myapp004objednavka

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp004objednavka.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

// 1. bindig - deklarace binding objektu s odloženou inicializací
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()

        //2. Binding - nafouknutí (inflate) layoutu do binding instance
        binding = ActivityMainBinding.inflate(layoutInflater)

        //3. Nastavení kořenového pohledu (root) do okna aktivity
        setContentView(binding.root)

        // Ošetření systémových lišt – použije se přímo binding.main nebo binding.root
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Změna obrázku podle vybraného druhu sushi
        //Změna obrázku v závislosti na vybraném radiobuttonu
        binding.rbSushi1.setOnClickListener {
            binding.ivSushi.setImageResource(R.drawable.maki_set)
        }
        binding.rbSushi2.setOnClickListener {
            binding.ivSushi.setImageResource(R.drawable.nigiri_set)
        }
        binding.rbSushi3.setOnClickListener {
            binding.ivSushi.setImageResource(R.drawable.california_set)
        }
        binding.btnOrder.setOnClickListener {
            val sushi = when(binding.rgSushi.checkedRadioButtonId) {
                binding.rbSushi1.id -> binding.rbSushi1
                binding.rbSushi2.id -> binding.rbSushi2
                binding.rbSushi3.id -> binding.rbSushi3

                else -> binding.rbSushi1 //Záložní možnost (fallback)
            }
        val wasabi = binding.cbWasabi.isChecked
            val ginger  = binding.cbGinger.isChecked
            val soy  = binding.cbSoy.isChecked

            val orderText = buildString {
                append(getString(R.string.order_summary_prefix))
                append(" ")
                append(sushi.text)
                if (wasabi) append("; " + getString(R.string.extra_wasabi))
                if (ginger) append("; " + getString(R.string.extra_ginger))
                if (soy) append("; " + getString(R.string.soy_sauce))
            }
        binding.tvOrder.text = orderText
            var total = when (binding.rgSushi.checkedRadioButtonId) {
                binding.rbSushi1.id -> 180
                binding.rbSushi2.id -> 220
                binding.rbSushi3.id -> 200
                else -> 180
            }
            if (wasabi) total += 10
            if (ginger) total += 10
            if (soy) total += 5

            binding.tvTotalPrice.text = getString(R.string.total_price, total)
        }
    }
}