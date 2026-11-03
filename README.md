# mygamewordsearch

A word search game (sopa de letras) for Android. The board is generated from a
list of words, which can be organized in levels and categories, and the player
has to find them by dragging over the letters. Every level can provide visual
and audio hints for each word.

The game ships with two themes, *Planetas* and *Planets*, and the player can
create their own categories with their own words and images.

## Requirements

- JDK 21
- Android SDK with platform `android-37.0` and build tools `36.0.0`

```bash
sdkmanager "platforms;android-37.0" "build-tools;36.0.0"
```

## Build

```bash
./gradlew assembleDebug          # debug APK
./gradlew testDebugUnitTest      # unit tests
./gradlew lintDebug              # lint
./gradlew build                  # everything
```

The debug APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

## Project layout

| Path | Contents |
|------|----------|
| `app/src/main/java/actividades` | The activities, one per screen |
| `app/src/main/java/modelo` | Board generation: word placement and ordering. Pure Java, no Android dependencies |
| `app/src/main/java/objetos` | Shared helpers: preferences, storage paths, images, messages |
| `app/src/main/java/vistas` | Custom views, including the board canvas |
| `app/src/main/java/xml` | Reading and writing the game data files |
| `app/src/main/assets` | The default games, packed as zip files |
| `app/src/main/res` | Resources, with translations in Spanish, English, French, Galician and Italian |

The board generator lives in `modelo`, which does not depend on Android, so it
is covered by unit tests that run on the JVM.

## Storage

The games are unpacked from the assets into the application-specific external
directory (`Android/data/es.uvigo.gti.mygamewordsearch/files`), which needs no
permissions and survives a reinstall. The state of the game in progress is
kept in the internal storage of the application.

## Original project

This is the PFC (final degree project) *Xerador de xogos de estimulación
cognitiva para dispositivos Android (Sopas de Letras)*.

- [Presentation slides](https://prezi.com/jlyx1ovas0-m/presentation/)
- [Technical memory (PDF)](https://drive.google.com/file/d/1KjA3guHRaIaTImf8p5IimBtVGoT6vuoO/view?usp=sharing)
