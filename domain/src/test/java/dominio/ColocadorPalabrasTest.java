package dominio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.Test;

/**
 * Tests del algoritmo de colocación de palabras.
 */
public class ColocadorPalabrasTest {

	private static final int TAMANO = 9;
	private static final String PALABRA = "planetas";

	private ColocadorPalabras colocador() {
		return new ColocadorPalabras(new Random(1));
	}

	private Tablero tableroVacio() {
		return new Tablero(TAMANO);
	}

	/**
	 * Tablero completamente ocupado, para comprobar que ninguna palabra cabe.
	 */
	private Tablero tableroLleno() {
		Tablero tablero = new Tablero(TAMANO);
		for (int y = 0; y < TAMANO; y++) {
			for (int x = 0; x < TAMANO; x++) {
				tablero.setCelda(x, y, 'z');
			}
		}
		return tablero;
	}

	/**
	 * Lee la palabra del tablero en el orden en el que avanza la orientación.
	 */
	private String leer(Tablero tablero, List<Celda> celdas) {
		StringBuilder palabra = new StringBuilder();
		for (Celda celda : celdas) {
			palabra.append(tablero.getCelda(celda.getX(), celda.getY()));
		}
		return palabra.toString();
	}

	@Test
	public void colocaLaPalabraEnFila() {
		Tablero tablero = tableroVacio();

		List<Celda> celdas = colocador().coloca(PALABRA, Orientacion.HORIZONTAL, tablero);

		assertEquals(PALABRA, leer(tablero, celdas));
		assertEquals(PALABRA.length(), celdas.size());
		assertEquals("la palabra no puede cambiar de fila", celdas.get(0).getY(), celdas.get(celdas.size() - 1).getY());
	}

	@Test
	public void colocaLaPalabraEnColumna() {
		Tablero tablero = tableroVacio();

		List<Celda> celdas = colocador().coloca(PALABRA, Orientacion.VERTICAL, tablero);

		assertEquals(PALABRA, leer(tablero, celdas));
		assertEquals("la palabra no puede cambiar de columna", celdas.get(0).getX(), celdas.get(celdas.size() - 1).getX());
	}

	@Test
	public void colocaLaPalabraEnDiagonalPrincipal() {
		Tablero tablero = tableroVacio();

		List<Celda> celdas = colocador().coloca(PALABRA, Orientacion.DIAGONAL_1, tablero);

		assertEquals(PALABRA, leer(tablero, celdas));
		assertEquals(celdas.get(0).getX() + 1, celdas.get(1).getX());
		assertEquals(celdas.get(0).getY() + 1, celdas.get(1).getY());
	}

	@Test
	public void colocaLaPalabraEnDiagonalSecundaria() {
		Tablero tablero = tableroVacio();

		List<Celda> celdas = colocador().coloca(PALABRA, Orientacion.DIAGONAL_2, tablero);

		assertEquals(PALABRA, leer(tablero, celdas));
		assertEquals(celdas.get(0).getX() - 1, celdas.get(1).getX());
		assertEquals(celdas.get(0).getY() + 1, celdas.get(1).getY());
	}

	@Test
	public void laPalabraSeCruzaConLaMismaLetra() {
		Tablero tablero = tableroVacio();
		// Una letra suelta que se puede cruzar con una palabra que la contiene.
		tablero.setCelda(4, 4, 'a');

		List<Celda> celdas = colocador().coloca(PALABRA, Orientacion.HORIZONTAL, tablero);

		assertTrue("la palabra debe poder cruzar con la misma letra", celdas != null);
	}

	@Test
	public void laPalabraNoSeCruzaConUnaLetraDistinta() {
		Tablero tablero = tableroLleno();

		List<Celda> celdas = colocador().coloca(PALABRA, Orientacion.HORIZONTAL, tablero);

		assertNull("no se puede colocar sobre una letra distinta", celdas);
	}

	@Test
	public void dejaElTableroIntactoSiLaColocacionFalla() {
		Tablero tablero = tableroLleno();

		colocador().coloca(PALABRA, Orientacion.HORIZONTAL, tablero);

		for (int y = 0; y < TAMANO; y++) {
			for (int x = 0; x < TAMANO; x++) {
				assertEquals("el tablero debe quedar intacto", 'z', tablero.getCelda(x, y));
			}
		}
	}

	@Test
	public void unaPalabraMasLargaQueElTableroNoCabe() {
		Tablero tablero = new Tablero(4);

		assertNull(colocador().coloca("demasiadapalabra", Orientacion.HORIZONTAL, tablero));
	}

	@Test
	public void verticalNoAdmiteCruceDePalabras() {
		/*
		 * Lavertical es la única orientación que no admite cruzar con otra
		 * palabra. Es el comportamiento que tenía el motor original y se
		 * mantiene aquí a propósito: cambiarlo es una decisión de producto,
		 * no un refactor.
		 */
		assertFalse(Orientacion.VERTICAL.permiteCruce());
		assertTrue(Orientacion.HORIZONTAL.permiteCruce());
		assertTrue(Orientacion.DIAGONAL_1.permiteCruce());
		assertTrue(Orientacion.DIAGONAL_2.permiteCruce());
	}
}
