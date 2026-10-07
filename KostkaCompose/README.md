<img width="387" height="859" alt="compose" src="https://github.com/user-attachments/assets/12bf8a91-8294-4e7a-9079-919d628c8354" />

# Jetpack Compose (`KostkaCompose`)
Celé rozhraní je napsané v Kotlinu pomocí composable funkce `DiceScreen()` (`Column`, `Text`, `Button`).  

Odsazení od lišt: `enableEdgeToEdge()` a modifikátor `systemBarsPadding()`.  

Animace: korutina spuštěná přes `rememberCoroutineScope()`, ve které cyklus `repeat(10)` mění stav a čeká pomocí `delay(250)`.  

Zakázání tlačítka: `Button(enabled = !isRolling)`, kde `isRolling` je stav.  
Porovnání: v XML variantě ručně měníme konkrétní prvky na obrazovce. V Compose měníme pouze stav a o aktualizaci zobrazení se postará framework.  
# Vylepšení (návazný úkol)
Aplikace byla rozšířena o:

Plynulé otáčení kostky – při každé změně se kostka otočí o 360° za 250 ms konstantní rychlostí, takže obraty na sebe navazují. V XML pomocí `ViewPropertyAnimator`, v Compose pomocí `Animatable` a `graphicsLayer`.  

Poskočení při dopadu – výsledná kostka se na chvíli zvětší a s mírným odskokem vrátí do původní velikosti.  

Zvukové efekty – cvaknutí při každé změně a hlubší úder při výsledném hodu (`SoundPool`, soubory `dice_click.wav` a `dice_land.wav` ve složce `res/raw`). Rychlost přehrání je mírně náhodná.
