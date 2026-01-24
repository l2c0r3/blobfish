# Performance Test

## Welche Performance Tests werden ausgeführt

### [PerformanceTest.java](../../src/test/java/ch/hslu/cas/msed/blobfish/PerformanceTest.java)

Der Code ist ein JUnit Test, der die Berechnungszeit verschiedener Schachbot-Implementierungen vergleicht. Die Variation
der Schachbot-Implementierung unterscheidet sich einerseits durch die Art wie der Spielbaum durchgeganen wird (e.g
[MiniMaxParallel.java](../../src/main/java/ch/hslu/cas/msed/blobfish/player/bot/minimax/MiniMaxParallel.java)), den Algorythmus
und der Bewertungsfunktion (
e.g [MateAwareEval.java](../../src/main/java/ch/hslu/cas/msed/blobfish/eval/MateAwareEval.java)).
Die Varianten werden zur Testzeit dynamisch zusammengestellt.

Pro Kombination aus Algorithmus, Bewertungsfunktion und Tiefe von 1 bis 4 wird der nächste beste Zug mehrfach berechnet und dabei die Ausführungszeit gemessen.
Aus 10 Wiederholungen der Messung wird der Median als Kennzahl für die Laufzeit verwendet, um Ausreisser zu reduzieren.


Der Test stellt sicher, dass die wiederholten Messungen sowie unterschiedliche Algorithmen unter gleichen
Bedingungen denselben Zug liefern, damit die Vergleiche fair und reproduzierbar bleiben. 

Die Rohmesswerte und die zusammengefassten Resultate werden pro Stellung in Dateien gespeichert, unter anderem als CSV. Zusätzlich wird aus den
Ergebnissen ein PlantUML-Balkendiagramm erzeugt und als SVG exportiert, um die Performance visuell auszuwerten.

Diese Ergebnisse, Rohdaten, CSV, PlantUML und SVG werden unter `/measurements/[AKTEULLES_DATUM_UND_ZEIT]` gespeichert

## Execute the Performance Test with your own PC

Um sämtliche Performance Tests auszuführen, kann folgender Befehl vom root Folder ausgeführt werden:

For Linux or macOS:

```bash
./mvnw test -Pperformance
```

or for Windows:

```powershell
.\mvnw.cmd test -Pperformance
```

## Messungen

### PC Setup

Sämtliche Messungen wurden mit folgendem PC druchgeführt:

- Betriebssystem: Microsoft Windows 11 Pro, Version 10.0.26200 (Build 26200)
- CPU: Intel Core i7-13700KF (13th Gen), 3.40 GHz, 16 Kerne / 24 Threads
- Mainboard: ASUS ROG STRIX Z790-F GAMING WIFI (ASUSTeK)
- RAM: 32GB
- GPU: AMD Radeon RX 9070

### Resultate

Die nachfolgenden Tests wurden mit dem Stand vom
Commit [71edae9](https://github.com/l2c0r3/blobfish/tree/71edae902790b45f286157de8d5b40bc6b19ee62)
durchgeführt. Die Rohdaten von den Ergebnissen findet ihr hier: [measurements/71edae9](measurements/71edae9)

**Complex Position With Many Options (FEN 1r4r1/5p1k/p2p1q1p/2b1nPQ1/p7/6RP/B1R2PPK/2B5 b - - 0 1)**

![ComplexPositionWithManyOptions.png](measurements/71edae9/complexPoistionWithManyOptions/ComplexPositionWithManyOptions.png)

**MidGame Promotion Mate in 2 (FEN 5r1k/1pqnbr1P/p2p1pQp/2p5/3PP2P/1PN5/1PP3R1/R5K1 w - - 0 24)**

![MidGame-Promotion-MateIn2-Short.png](measurements/71edae9/midGamePromotion/MidGame-Promotion-MateIn2-Short.png)

**MidGame Fork (FEN r3k2r/pNp1ppbp/2nq2pn/7P/2B3b1/3P1N2/PPP2PP1/R1BQK2R b KQkq - 0 11)**

![MidGame-Fork-Short.png](measurements/71edae9/midGameForkShort/MidGame-Fork-Short.png)

**MidGame EnPassant (FEN rn1q1r1k/pbpp1p1p/1p2p3/4P3/3P2Qp/P2B4/1PP2PPP/R4RK1 w - - 0 17)**

![MidGame-EnPassant-Long.png](measurements/71edae9/midGameEnPassant/MidGame-EnPassant-Long.png)

**MidGame Discovery (FEN r5k1/7p/1p1Qp1p1/p1np1r2/1q3P1P/1P6/2P3P1/RB3RK1 w - - 3 26)**

![MidGame-Discovery-Short.png](measurements/71edae9/midGameDiscoveryShort/MidGame-Discovery-Short.png)

**MidGame Discovery Mate in 2 (FEN Q7/p1pk3p/2p2qp1/3p1b2/8/1PN1P3/P1PP2PP/R4KNR b - - 4 15)**

![MidGame-Discovery-MateIn2-Short.png](measurements/71edae9/midGameDiscoveryMateIn2/MidGame-Discovery-MateIn2-Short.png)

**MidGame Deflection (FEN 5rk1/5q1p/p2PR1p1/4p1b1/1pP5/1P1Q4/P5PP/1K2R3 b - - 0 31)**

![MidGame-Deflection-Short.png](measurements/71edae9/midGameDeflection/MidGame-Deflection-Short.png)

**EndGame Deflection (FEN 8/5ppk/4p1p1/3pq3/3Q4/1B2r2P/P5P1/3R3K b - - 8 42)**

![EndGame-Deflection-Short.png](measurements/71edae9/endGameDeflection/EndGame-Deflection-Short.png)

**Vergleich aller Posistion mit einer tiefe von vier:**

![depth4_allPos.png](measurements/71edae9/depth4_allPos.png)

#### Intepretation

