<img width="394" height="882" alt="xml" src="https://github.com/user-attachments/assets/407d3aa3-9197-4abd-b115-d5e24d196de9" />

# 1. XML + Kotlin (`KostkaXML`)
Rozhraní je definované v souboru `activity_main.xml`. Prvky mají ID s prefixy (`llMain`, `tvTitle`, `tvDice`, `btnRoll`) a v kódu se k nim přistupuje pomocí `findViewById`.

Odsazení od lišt: `enableEdgeToEdge()` a `ViewCompat.setOnApplyWindowInsetsListener`, který nastaví padding kořenového layoutu `llMain`.  

Animace: `Handler.postDelayed` volá po 250 ms další krok. Každý krok změní text kostky.  

Zakázání tlačítka: `btnRoll.isEnabled = false` na začátku a `true` po skončení animace.  

Porovnání: v XML variantě ručně měníme konkrétní prvky na obrazovce. V Compose měníme pouze stav a o aktualizaci zobrazení se postará framework.  
# Vylepšení (návazný úkol)

Aplikace byla rozšířena o:

Plynulé otáčení kostky – při každé změně se kostka otočí o 360° za 250 ms konstantní rychlostí, takže obraty na sebe navazují. V XML pomocí `ViewPropertyAnimator`, v Compose pomocí `Animatable` a `graphicsLayer`.  

Poskočení při dopadu – výsledná kostka se na chvíli zvětší a s mírným odskokem vrátí do původní velikosti.  

Zvukové efekty – cvaknutí při každé změně a hlubší úder při výsledném hodu (`SoundPool`, soubory `dice_click.wav` a `dice_land.wav` ve složce `res/raw`). Rychlost přehrání je mírně náhodná.
