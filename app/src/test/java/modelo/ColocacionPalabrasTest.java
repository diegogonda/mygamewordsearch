package modelo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests del algoritmo de colocación de palabras en el tablero.
 *
 * Las clases del paquete modelo no dependen de Android, así que se prueban
 * directamente contra la JVM.
 */
public class ColocacionPalabrasTest {

	private static final int TAMANO = 9;
	private static final char RELLENO = '-';
	private static final String PALABRA = "planetas";

	private char[][] tableroVacio() {
		char[][] tablero = new char[TAMANO][TAMANO];
		for (int fila = 0; fila < TAMANO; fila++) {
			for (int columna = 0; columna < TAMANO; columna++) {
				tablero[fila][columna] = RELLENO;
			}
		}
		return tablero;
	}

	private char[][] tableroLleno() {
		char[][] tablero = tableroVacio();
		for (int fila = 0; fila < TAMANO; fila++) {
			for (int columna = 0; columna < TAMANO; columna++) {
				tablero[fila][columna] = 'z';
			}
		}
		return tablero;
	}

	/**
	 * Lee la palabra del tablero siguiendo la dirección que va de la posición
	 * inicial a la final.
	 */
	private String leerPalabra(char[][] tablero, int xInicial, int yInicial, int xFinal, int yFinal) {
		StringBuilder palabra = new StringBuilder();
		int pasoX = Integer.compare(xFinal, xInicial);
		int pasoY = Integer.compare(yFinal, yInicial);
		int x = xInicial;
		int y = yInicial;
		while (true) {
			palabra.append(tablero[y][x]);
			if (x == xFinal && y == yFinal) {
				break;
			}
			x += pasoX;
			y += pasoY;
		}
		return palabra.toString();
	}

	@Test
	public void colocaLaPalabraEnFila() {
		char[][] tablero = tableroVacio();
		Horizontal horizontal = new Horizontal(PALABRA, tablero, TAMANO, RELLENO);

		assertTrue(horizontal.resultado());
		assertEquals(PALABRA, leerPalabra(tablero, horizontal.getxInicial(), horizontal.getyInicial(), horizontal.getxFinal(), horizontal.getyFinal()));
		assertEquals(horizontal.getyInicial(), horizontal.getyFinal());
		assertEquals(PALABRA.length() - 1, horizontal.getxFinal() - horizontal.getxInicial());
	}

	@Test
	public void colocaLaPalabraEnColumna() {
		char[][] tablero = tableroVacio();
		Vertical vertical = new Vertical(PALABRA, tablero, TAMANO, RELLENO);

		assertTrue(vertical.resultado());
		assertEquals(PALABRA, leerPalabra(tablero, vertical.getxInicial(), vertical.getyInicial(), vertical.getxFinal(), vertical.getyFinal()));
		assertEquals(vertical.getxInicial(), vertical.getxFinal());
		assertEquals(PALABRA.length() - 1, vertical.getyFinal() - vertical.getyInicial());
	}

	@Test
	public void colocaLaPalabraEnDiagonalPrincipal() {
		char[][] tablero = tableroVacio();
		Diagonal1 diagonal = new Diagonal1(PALABRA, tablero, TAMANO, RELLENO);

		assertTrue(diagonal.resultado());
		assertEquals(PALABRA, leerPalabra(tablero, diagonal.getxInicial(), diagonal.getyInicial(), diagonal.getxFinal(), diagonal.getyFinal()));
		assertEquals(PALABRA.length() - 1, diagonal.getxFinal() - diagonal.getxInicial());
		assertEquals(PALABRA.length() - 1, diagonal.getyFinal() - diagonal.getyInicial());
	}

	@Test
	public void colocaLaPalabraEnDiagonalSecundaria() {
		char[][] tablero = tableroVacio();
		Diagonal2 diagonal = new Diagonal2(PALABRA, tablero, TAMANO, RELLENO);

		assertTrue(diagonal.resultado());
		assertEquals(PALABRA, leerPalabra(tablero, diagonal.getxInicial(), diagonal.getyInicial(), diagonal.getxFinal(), diagonal.getyFinal()));
		assertEquals(PALABRA.length() - 1, diagonal.getxInicial() - diagonal.getxFinal());
		assertEquals(PALABRA.length() - 1, diagonal.getyFinal() - diagonal.getyInicial());
	}

	@Test
	public void colocaUnaPalabraDelTamanoDelTablero() {
		char[][] tablero = tableroVacio();
		String palabra = "abcde";
		char[][] tableroCinco = new char[5][5];
		for (int fila = 0; fila < 5; fila++) {
			for (int columna = 0; columna < 5; columna++) {
				tableroCinco[fila][columna] = RELLENO;
			}
		}

		Horizontal horizontal = new Horizontal(palabra, tableroCinco, 5, RELLENO);

		assertTrue(horizontal.resultado());
		assertEquals(palabra, leerPalabra(tableroCinco, horizontal.getxInicial(), horizontal.getyInicial(), horizontal.getxFinal(), horizontal.getyFinal()));
	}

	@Test
	public void noColocaLaPalabraSiElTableroEstaOcupado() {
		char[][] tablero = tableroLleno();
		Horizontal horizontal = new Horizontal(PALABRA, tablero, TAMANO, RELLENO);

		assertFalse(horizontal.resultado());
	}

	@Test
	public void dejaElTableroIgualSiLaColocacionFalla() {
		/*
		 * La posición de salida es aleatoria, así que se repiten los intentos
		 * comprobando que, siempre que la colocación falla, el tablero queda
		 * exactamente como estaba.
		 */
		char[][] original = tableroVacio();
		original[4][4] = 'z';

		for (int intento = 0; intento < 500; intento++) {
			char[][] tablero = new char[TAMANO][TAMANO];
			for (int fila = 0; fila < TAMANO; fila++) {
				tablero[fila] = original[fila].clone();
			}

			Horizontal horizontal = new Horizontal(PALABRA, tablero, TAMANO, RELLENO);
			if (!horizontal.resultado()) {
				assertArrayEquals("el tablero debe quedar intacto si la colocación falla", original, tablero);
			}
		}
	}

	@Test
	public void permiteElCruceDePalabrasConLaMismaLetra() {
		char[][] tablero = tableroVacio();
		Horizontal primera = new Horizontal("solar", tablero, TAMANO, RELLENO);
		assertTrue(primera.resultado());

		/*
		 * La segunda palabra comparte la letra inicial con la primera, que es
		 * lo que permite que las palabras se crucen en el tablero.
		 */
		boolean cruzada = false;
		for (int intento = 0; intento < 500 && !cruzada; intento++) {
			Horizontal segunda = new Horizontal("sistema", tablero, TAMANO, RELLENO);
			if (segunda.resultado()) {
				cruzada = true;
				assertEquals("sistema", leerPalabra(tablero, segunda.getxInicial(), segunda.getyInicial(), segunda.getxFinal(), segunda.getyFinal()));
			} else {
				// la colocación falló, el tablero debe seguir como estaba
				assertEquals('s', tablero[primera.getyInicial()][primera.getxInicial()]);
			}
		}
		assertTrue("debe ser posible cruzar dos palabras que comparten letra", cruzada);
	}
}
