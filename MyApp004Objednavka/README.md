# Jednoduchý objednávkový systém (Sushi) \- MyApp004Objednavka

Jednoduchá Android aplikace pro objednávku sushi.

## Implementované vylepšení (Úkol 2\)

K základní funkcionalitě objednávky bylo naimplementováno následující vlastní rozšíření:

**Výpočet celkové ceny v souhrnu objednávky**

Aplikace nyní dynamicky počítá celkovou cenu objednávky na základě toho, jaké položky uživatel vybere a potvrdí tlačítkem "Objednat".

* **Základní cena:** Určuje se podle vybraného sushi setu (přes `RadioButton`).  
* **Příplatky:** Zohledňuje se, zda uživatel zvolil doplňky (přes `CheckBox` \- např. Extra wasabi, Zázvor navíc, Sójová omáčka).

Výsledná celková cena se neukazuje u jednotlivých položek předem, ale zobrazí se uživateli **až jako součást souhrnu objednávky** v dolní části obrazovky po kliknutí na tlačítko "Objednat".  
