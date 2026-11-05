# El motor al 100%

Objetivo: que el motor de tableros sea la pieza más sólida del proyecto. Las reglas tienen nombre, las entradas inválidas se rechazan donde deben rechazarse, la última regla del juego que vivía en la interfaz pasa al dominio, y el motor se somete a una verificación exhaustiva por propiedades.
Restricción: **no se toca la capa Android**. La API pública del motor queda congelada, porque la app la consume: si la firma cambia, `CreaTablero` tiene que cambiar con ella.
Rama: `redo`. Entrega: commits por unidad de trabajo; el push y el PR los decide el usuario.
Runner: `./gradlew :domain:test` (el motor es Java puro, los tests corren en la JVM).

## Specs

- S1: Ningún fichero de `app/src/main/java/` se modifica en esta tanda.
- S2: La API pública del motor no cambia. La app sigue consumiendo `GeneradorTablero.genera(ConfiguracionJuego, List<Palabra>)` y recibiendo una `Partida`.
- S3: Las reglas del tablero son un concepto con nombre: cuántas palabras hacen falta para que una partida tenga sentido, y cuántas caben en un tablero de un tamaño dado. Los números dejan de ser un `switch` sin explicación.
- S4: El motor rechaza las entradas que no tienen sentido: un tablero de tamaño cero o negativo, una palabra sin texto y una lista de palabras nula. Es un error de dominio, no un `NullPointerException` ni un tablero de cero celdas.
- S5: La última regla del juego que vivía en la interfaz pasa al dominio: qué palabra ha encontrado el jugador cuando arrastra de una celda a otra. Hoy son comparaciones de píxeles dentro de una vista.
- S6: El motor se verifica por propiedades: muchas semillas, los cinco modos de orientación y varios tamaños de tablero, comprobando el contrato completo (tablero lleno, cada palabra legible en su orientación, orden estable, la misma semilla da el mismo tablero, las palabras repetidas van cada una a su sitio).
- S7: El motor no se cuelga y no lanza por un tablero jugable.

## Tasks

- T1 (S3) `ReglasDeTablero`: las reglas con nombre y con tests que expliquen cada número. Route: inline. Estado: hecho en `38d923d`.
- T2 (S4) Las entradas inválidas se rechazan en el dominio. Route: inline. Estado: hecho en `38d923d`.
- T3 (S5) La regla de selección en el dominio: `Partida.buscaPalabra`. Route: inline. Estado: hecho en `38d923d`.
- T4 (S6) Verificación por propiedades del motor. Route: inline. Estado: hecho en `38d923d`.
- T5 (S7) La regla de aceptación de un tablero: quitar el máximo de palabras. Route: inline. Estado: hecho en `296549d`.

## Log

- L1 (2026-11-05): "los cambios en android todavia no los quiero abordar, me gustaría centrarme en el motor al 100%"
- L2 (2026-11-05): El motor ya es puro y está en su propio módulo, así que "al 100%" es posible sin tocar Android: la única atadura es la API pública, que queda congelada (S2).
- L3 (2026-11-05): Pendiente de decidir, porque cambia el equilibrio del juego: la regla de aceptación de un tablero. Hoy el motor acepta un tablero si tiene entre un mínimo y un máximo de palabras, y el máximo está calculado para un motor que no dejaba cruzar palabras. Con los cruces, un tablero puede tener más palabras de las que el máximo permite, y entonces el motor lo rechaza, regenera hasta cincuenta veces y lanza una excepción: la aplicación acaba usando el tablero parcial, que es justamente el que el motor había rechazado. La regla que propongo es que el máximo desaparezca: un tablero no es malo por tener muchas palabras, y el mínimo sigue mandando para que la partida tenga sentido.
- L4 (2026-11-05): El enunciado del invariante del tablero estaba mal y los tests lo han detectado: el motor no garantiza que el tablero sólo tenga las 26 letras, porque las palabras se escriben tal cual y sólo las celdas de relleno son del abecedario. La razón por la que no se puede corregir en el dominio: la pantalla de palabras personalizadas sólo comprueba que el campo no esté vacío (`EditTextVacio`), así que el usuario puede meter "abc123" y el dominio no puede rechazarlo sin romper la app. El invariante correcto es "las celdas que no son de palabra se rellenan con el abecedario, y cada palabra se escribe tal cual, sin tildes". Corregir el filtrado de la app es trabajo de Android, para cuando toque.
- L5 (2026-11-05): Decidido: el máximo desaparece. Medido antes de tocar el código: con veinte palabras distintas en un tablero de 9, el motor colocaba entre 17 y 19 y las rechazaba, regeneraba cincuenta veces y lanzaba, y la aplicación acababa usando el parcial. Ahora las acepta. Los juegos que ya se reparten no cambian. La API pública no ha cambiado, así que la app no se ha tocado.
- L6 (2026-11-05): El test que pinsa el cambio se escribió primero y falló como debía: con el código viejo lanzaba `TableroException: No se ha podido generar un tablero de 9 con 20 palabras`. El primer intento del test usaba veinte palabras iguales, que se apilan en las mismas celdas y entraban todas, así que no daba ningún redness: hubo que medir con datos en vez de suponer. El segundo intento, con veinte palabras distintas, sí reproduce el defecto.

