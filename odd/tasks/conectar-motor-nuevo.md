# Conectar el motor de tableros nuevo y retirar el motor legacy

Objetivo: que la partida se genere con el motor puro de `modelo/` (`GeneradorTablero`) y eliminar el motor legacy (`Horizontal`, `Vertical`, `Diagonal1`, `Diagonal2`, `OrdenadorPalabras`), que hoy convive con el nuevo sin que nadie lo use.
Rama: `redo`. Entrega: commits por unidad de trabajo en la rama; el push y el PR los decide el usuario.
Runner: `./gradlew testDebugUnitTest` (JUnit en la JVM) y `./gradlew lintDebug`.

## Specs

- S1: El tablero de juego se genera con el motor puro `modelo.GeneradorTablero`. No queda ninguna clase de colocación legacy en `modelo/`: `Horizontal`, `Vertical`, `Diagonal1`, `Diagonal2` y `OrdenadorPalabras` desaparecen, con sus tests.
- S2: Cada palabra colocada queda registrada en su `PosicionesPalabras` con las coordenadas de inicio y de fin, la orientación, el vector de letras y la marca de invertida. Los códigos de orientación que consume la presentación se mantienen: `0` horizontal, `1` vertical, `2` diagonal principal, `3` diagonal secundaria.
- S3: Las palabras que el motor no ha podido colocar se retiran de la lista de la partida y del fichero `posiciones_palabras.xml`. Las que sí quedan conservan el orden de la lista original.
- S4: Si el motor no consigue un tablero con un número razonable de palabras, la partida sigue siendo jugable con el mejor tablero parcial encontrado. El motor lanza `TableroException`, que lleva consigo ese tablero parcial.
- S5: Las palabras invertidas se escriben del revés en el tablero cuando la preferencia `palabras_invertidas` está activa, y la pista marca la primera letra de la palabra.
- S6: La preferencia de orientación mantiene su significado: `1` sólo horizontal, `2` sólo vertical, `3` horizontal y vertical, `4` diagonales, `5` todas. Cualquier valor desconocido cae en horizontal, como hasta ahora.
- S7: Las palabras con tildes se escriben en el tablero sin tildes: `á é í ó ú` se convierten en `a e i o u`.
- S8: No cambia el formato de `posiciones_palabras.xml` ni de `partida_en_juego.xml`.

## Tasks

- T1 (S2, S3) Adaptador `modelo` → presentación, con test RED primero. Route: inline. Estado: pendiente.
- T2 (S4) Tablero parcial en el motor y en `TableroException`, con test RED primero. Route: inline. Estado: pendiente.
- T3 (S1, S2, S3) `CreaTablero` delega en el motor y en el adaptador. Route: inline. Estado: pendiente.
- T4 (S5, S6, S7) Tests de caracterización de preferencias y tildes. Route: inline. Estado: pendiente.
- T5 (S1) Borrar el motor legacy y sus tests. Route: inline. Estado: pendiente.
- T6 (S8) Verificación: suite, lint, differential contra el motor legacy del commit base y emulador. Route: inline. Estado: pendiente.

## Log

- L1 (2026-11-05): "por donde ibamos"
- L2 (2026-11-05): "adoptamos" — se adopta la semántica del motor nuevo: "cruces permitidos, invertidas operativas".
- L3 (2026-11-05): El análisis del código legacy corrige una afirmación anterior: el motor legacy **sí** permite cruces en horizontal, diagonal principal y diagonal secundaria; sólo la vertical los prohíbe. `modelo.Orientacion.permiteCruce` reproduce esa misma regla. Lo que realmente cambia el juego es que las palabras invertidas pasan a funcionar (en el legacy el flag se reiniciaba a `false` en cada vuelta del bucle) y que `ó` se convierte en `o` (en el legacy no había bloque para `ó`).
- L4 (2026-11-05): Decisión tomada sin consultar: si el motor no consigue un tablero razonable, se usa el mejor tablero parcial en lugar de dejar la aplicación colgada. El legacy regeneraba el tablero en bucle infinito cuando el número de palabras quedaba fuera del rango válido.
