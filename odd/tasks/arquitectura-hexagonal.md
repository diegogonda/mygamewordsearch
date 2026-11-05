# Arquitectura hexagonal: dominio, aplicación e infraestructura

Objetivo: que el juego tenga un dominio con vocabulario propio, una capa de aplicación con los casos de uso y los puertos, y una infraestructura con los adaptadores. La frontera entre las capas pasa a estar garantizada por el compilador y no por una convención.
Rama: `redo`. Entrega: commits por unidad de trabajo; el push y el PR los decide el usuario.
Runner: `./gradlew test lintDebug` (los tests de todos los módulos) y el emulador API 37 para la parte de pantalla.

## Specs

- S1: El dominio vive en el módulo Gradle `:domain`, en Java puro, sin una sola dependencia de Android. El compilador lo garantiza: si alguien importa `android.*` en el dominio, el build falla.
- S2: La aplicación vive en el módulo Gradle `:application`: los casos de uso y los puertos que necesitan. También sin Android. Depende de `:domain` y nadie depende de ella salvo la app.
- S3: La infraestructura vive en la app: los adaptadores que implementan los puertos (XML de la partida, preferencias, contenido de los niveles).
- S4: `Partida` es un objeto del dominio: el tablero, las palabras que hay que encontrar, con su pista y su posición en el tablero.
- S5: `TipoPista` es un enum del dominio con los valores del contenido (`nombre`, `pista_descripcion`, `pista_imagen`, `pista_audio`, `pista_marca`, `pista_categoria`, `sin_pista`). Ningún sitio compara strings para decidir de qué tipo es la pista.
- S6: La partida se genera desde un caso de uso, no desde el constructor de una vista.
- S7: `PosicionesPalabras` deja de ser el objeto que la app usa para el juego. Pasa a ser, como mucho, el DTO del adaptador de XML.
- S8: El formato de `posiciones_palabras.xml`, de `partida_en_juego.xml` y de los ficheros de contenido no cambia.
- S9: La partida en curso se restaura tal cual se guardó: mismo tablero, mismas posiciones y mismo progreso. Hoy el tablero se vuelve a generar cada vez que se crea la vista, así que el progreso guardado deja de corresponder con el tablero.

## Fuera de alcance (y por qué)

- **La reescritura de la interfaz con Compose.** Sigue sin decidirse. Este trabajo no la presupone: la presentación consume el dominio, dé lo que dé la interfaz.
- **El progreso de la partida como dominio.** Qué palabras ha encontrado el jugador sigue en la capa de presentación, como hoy. Es el siguiente paso, no este.
- **Mover `objetos/` entero a `infraestructura/`.** Se mueve lo que toque cada unidad de trabajo; el resto se queda hasta que haya una razón para moverlo.
- **Reescribir el contenido de los niveles como modelo de dominio.** El contenido sigue siendo el que es; lo que cambia es que la app lo consume a través de un puerto.

## Tasks

- T1 (S1, S2) Los módulos Gradle `:domain` y `:application`, con el modelo del dominio movido a `:domain`. Route: inline. Estado: hecho en `d26a2bf`.
- T2 (S4, S5) El vocabulario del dominio: `Partida`, `PalabraColocada`, `Palabra`, `Pista`, `TipoPista`, con test primero. Route: inline. Estado: hecho en `8fbf26f`.
- T3 (S2, S6) El caso de uso "generar partida" y los puertos, con test primero. Route: inline. Estado: **aplazado** (ver L9).
- T4 (S3, S6, S7, S8, S9) Los adaptadores Android y el cambio de la app para usar el caso de uso. Route: inline. Estado: **aplazado** (ver L9).

## Log

- L1 (2026-11-05): "Estoy de acuerdo, sobre compose no me he decidido, por eso de momento optemos por obviar esa decisión y cuando toque valoraremos el coste"
- L2 (2026-11-05): Mapa del estado real antes de decidir: `modelo/` son 735 líneas puras sin un solo import de Android (el dominio ya existe); `objetos/` son 3167 líneas, el 43% del código, y no es una capa sino el cajón donde acaba lo que no tiene sitio; el tipo de pista son strings literales comparados en seis sitios; el contenido son 662 líneas de `XMLParser` comparando etiquetas.
- L3 (2026-11-05): Hallazgo: `partida_en_juego.xml` se escribe desde `TableroVista` pero **ningún sitio lo lee**. El tablero se regenera en el constructor de la vista a partir de la lista de palabras, mientras que el progreso (qué palabras ha encontrado el jugador) sí se guarda en `posiciones_palabras.xml`. El resultado es que el progreso guardado puede no corresponder con el tablero. Las actividades están fijadas en vertical (`android:screenOrientation="portrait"`), así que el defecto está latente: no se manifiesta al girar, pero sí si el proceso muere y la actividad se recrea. Es el fallo que arregla S9.
- L4 (2026-11-05): Decisión: los puertos se definen en la capa de aplicación, que es quien los necesita, y la infraestructura los implementa. En vez de un puerto por método, sólo hay puerto donde hay una frontera de verdad: el contenido de los niveles, las preferencias de la partida y el almacenamiento de la partida en curso.
- L5 (2026-11-05): La frontera entre las capas pasa a estar garantizada por el compilador, no por una convención. El módulo `:domain` no depende del plugin de Android, así que un `import android.*` en el dominio rompe el build. Comprobado: con un `import android.content.Context` en `dominio/Tablero.java` el build falla con "package android.content does not exist".
- L6 (2026-11-05): Decisión de vocabulario: una sola noción de "palabra en el juego". `PosicionPalabra` (la geometría que devolvía el motor) y `TableroGenerado` (el tablero y las posiciones) se sustituyen por `PalabraColocada` y `Partida`. El motor recibe `Palabra` (el texto y su pista) y devuelve `PalabraColocada`. La pista viaja con la palabra desde el contenido hasta la partida, así que no hace falta emparejarla por texto, que fallaría con palabras repetidas.
- L7 (2026-11-05): `Partida` sólo se puede crear si todas sus palabras se leen en el tablero y si sus celdas siguen la orientación que declaran. El invariante ha pagado: en desarrollo ha detectado datos de test incoherentes, y es el que habría pillado el fallo legacy en el que las posiciones registradas apuntaban a un tablero que ya no existía.
- L8 (2026-11-05): Lo que queda fuera de esta tanda, y por qué: S9 (restaurar la partida en curso tal cual se guardó) sigue pendiente porque depende de que la partida sea un objeto que la actividad pueda guardar y restaurar, y eso es T4. El progreso (qué palabras ha encontrado el jugador) sigue en la capa de presentación, como estaba.
- L9 (2026-11-05): T3 y T4 quedan aplazados por decisión del usuario: "los cambios en android todavía no los quiero abordar, me gustaría centrarme en el motor al 100%". La consecuencia técnica es que la API pública del motor queda congelada mientras tanto, porque la app la consume: si la firma cambia, `CreaTablero` tiene que cambiar con ella, y eso es un cambio en Android. El trabajo del motor sigue siendo posible por detrás de esa API. Lo que no se pierde: el módulo `:application` queda listo y con su esqueleto, y los specs S1, S2, S4 y S5 ya están cumplidos.

