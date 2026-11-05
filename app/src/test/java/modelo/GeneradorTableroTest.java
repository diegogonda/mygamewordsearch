package modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;

/**
 * Tests del motor de generación de tableros. Es lógica pura, así que se
 * ejecuta en la JVM sin emulador.
 */
public class GeneradorTableroTest {

	private static final int TAMANO = 9;

	private GeneradorTablero generador(long semilla) {
		Random aleatorio = new Random(semilla);
		return new GeneradorTablero(new ColocadorPalabras(aleatorio), aleatorio);
	}

	private ConfiguracionJuego configuracion(ModoOrientacion modo) {
		return new ConfiguracionJuego(TAMANO, modo, false);
	}

	private List<String> palabrasPlanetas() {
		return new ArrayList<String>(Arrays.asList("mercurio", "venus", "tierra", "marte", "jupiter", "saturno", "urano", "neptuno"));
	}

	/**
	 * Lee la palabra del tablero siguiendo la orientación en la que está.
	 */
	private String leer(Tablero tablero, PosicionPalabra posicion) {
		StringBuilder palabra = new StringBuilder();
		for (Celda celda : posicion.getCeldas()) {
			palabra.append(tablero.getCelda(celda.getX(), celda.getY()));
		}
		return palabra.toString();
	}

	@Test
	public void colocaTodasLasPalabrasEnElTablero() throws TableroException {
		TableroGenerado generado = generador(42).genera(configuracion(ModoOrientacion.TODAS), palabrasPlanetas());

		assertEquals("todas las palabras deben caber en el tablero", 8, generado.getPosiciones().size());
	}

	@Test
	public void lasPalabrasQuedanEscritasEnElTablero() throws TableroException {
		TableroGenerado generado = generador(7).genera(configuracion(ModoOrientacion.TODAS), palabrasPlanetas());

		for (PosicionPalabra posicion : generado.getPosiciones()) {
			assertEquals("la palabra debe estar escrita en el tablero", posicion.getPalabra(), leer(generado.getTablero(), posicion));
		}
	}

	@Test
	public void respetaElModoDeOrientacion() throws TableroException {
		TableroGenerado horizontal = generador(3).genera(configuracion(ModoOrientacion.HORIZONTAL), palabrasPlanetas());
		for (PosicionPalabra posicion : horizontal.getPosiciones()) {
			assertEquals(Orientacion.HORIZONTAL, posicion.getOrientacion());
		}

		TableroGenerado vertical = generador(3).genera(configuracion(ModoOrientacion.VERTICAL), palabrasPlanetas());
		for (PosicionPalabra posicion : vertical.getPosiciones()) {
			assertEquals(Orientacion.VERTICAL, posicion.getOrientacion());
		}

		TableroGenerado diagonales = generador(3).genera(configuracion(ModoOrientacion.DIAGONALES), palabrasPlanetas());
		for (PosicionPalabra posicion : diagonales.getPosiciones()) {
			assertTrue(posicion.getOrientacion() == Orientacion.DIAGONAL_1 || posicion.getOrientacion() == Orientacion.DIAGONAL_2);
		}
	}

	@Test
	public void elTableroNoQuedanCeldasVacias() throws TableroException {
		TableroGenerado generado = generador(11).genera(configuracion(ModoOrientacion.TODAS), palabrasPlanetas());

		for (int y = 0; y < TAMANO; y++) {
			for (int x = 0; x < TAMANO; x++) {
				assertFalse("no puede quedar ninguna celda vacía", generado.getTablero().getCelda(x, y) == Tablero.LETRA_RELLENO);
			}
		}
	}

	@Test
	public void lasPalabrasInvertidasSeEscribenDelReves() throws TableroException {
		ConfiguracionJuego configuracion = new ConfiguracionJuego(TAMANO, ModoOrientacion.HORIZONTAL, true);

		boolean algunaInvertida = false;
		for (int semilla = 0; semilla < 20 && !algunaInvertida; semilla++) {
			TableroGenerado generado = generador(semilla).genera(configuracion, palabrasPlanetas());
			for (PosicionPalabra posicion : generado.getPosiciones()) {
				if (posicion.isInvertida()) {
					algunaInvertida = true;
					assertEquals("la palabra del tablero va del revés", Palabras.invertida(posicion.getPalabra()), leer(generado.getTablero(), posicion));
				}
			}
		}
		assertTrue("alguna palabra debe salir invertida", algunaInvertida);
	}

	@Test
	public void sinPalabrasInvertidasNingunaLoEsta() throws TableroException {
		TableroGenerado generado = generador(5).genera(configuracion(ModoOrientacion.TODAS), palabrasPlanetas());

		for (PosicionPalabra posicion : generado.getPosiciones()) {
			assertFalse(posicion.isInvertida());
		}
	}

	@Test
	public void fallaSiLasPalabrasNoCabenEnElTablero() {
		List<String> demasiadas = new ArrayList<String>();
		for (int i = 0; i < 40; i++) {
			demasiadas.add("palabra" + i);
		}

		try {
			generador(1).genera(new ConfiguracionJuego(4, ModoOrientacion.TODAS, false), demasiadas);
			assertTrue("se esperaba un error de dominio", false);
		} catch (TableroException e) {
			assertNotNull(e.getMessage());
		}
	}

	@Test
	public void elTableroEsElMismoConLaMismaSemilla() throws TableroException {
		TableroGenerado primero = generador(99).genera(configuracion(ModoOrientacion.TODAS), palabrasPlanetas());
		TableroGenerado segundo = generador(99).genera(configuracion(ModoOrientacion.TODAS), palabrasPlanetas());

		assertTrue("con la misma semilla el tablero debe ser el mismo", Arrays.deepEquals(primero.getTablero().getCeldas(), segundo.getTablero().getCeldas()));
	}

	@Test
	public void unaPalabraMasLargaQueElTableroNoCabe() {
		ColocadorPalabras colocador = new ColocadorPalabras(new Random(1));
		Tablero tablero = new Tablero(4);

		assertNull(colocador.coloca("demasiadapalabra", Orientacion.HORIZONTAL, tablero));
	}

	@Test
	public void elTableroQuedaIntactoSiUnaPalabraNoCabe() throws TableroException {
		/*
		 * En un tablero de 4x4 caben cuatro palabras de cuatro letras, así que
		 * la quinta se queda fuera. Las que no caben no deben aparecer en las
		 * posiciones: si apareciesen, el jugador buscaría una palabra que no
		 * está en el tablero.
		 */
		List<String> palabras = new ArrayList<String>(Arrays.asList("luna", "casa", "gato", "mesa", "paz"));

		TableroGenerado generado = generador(13).genera(new ConfiguracionJuego(4, ModoOrientacion.TODAS, false), palabras);

		for (PosicionPalabra posicion : generado.getPosiciones()) {
			assertEquals("la palabra debe estar en el tablero", posicion.getPalabra(), leer(generado.getTablero(), posicion));
		}
		assertTrue("debe haber al menos un par de palabras", generado.getPosiciones().size() >= 2);
		assertTrue("no pueden caber todas", generado.getPosiciones().size() <= 4);
	}

	@Test
	public void fallaSiUnaPalabraNoCabeEnElTablero() {
		List<String> palabras = new ArrayList<String>(Arrays.asList("tierra"));

		try {
			generador(1).genera(new ConfiguracionJuego(4, ModoOrientacion.TODAS, false), palabras);
			assertTrue("se esperaba un error de dominio", false);
		} catch (TableroException e) {
			assertTrue("el error debe explicar el motivo", e.getMessage().contains("4"));
		}
	}

	@Test
	public void conservaElOrdenDeLasPalabrasDeIgualLongitud() throws TableroException {
		/*
		 * Las palabras de la misma longitud se colocan en el mismo orden que
		 * vienen en la lista de la partida.
		 */
		List<String> palabras = new ArrayList<String>(Arrays.asList("luna", "gato", "casa", "mesa"));

		TableroGenerado generado = generador(21).genera(new ConfiguracionJuego(TAMANO, ModoOrientacion.TODAS, false), palabras);

		List<String> colocadas = new ArrayList<String>();
		for (PosicionPalabra posicion : generado.getPosiciones()) {
			colocadas.add(posicion.getPalabra());
		}
		assertEquals("el orden de las palabras debe respetarse", palabras, colocadas);
	}

	@Test
	public void lasPalabrasConTildeSeEscribenSinTilde() throws TableroException {
		/*
		 * El tablero sólo tiene las 26 letras del abecedario, así que una
		 * palabra con tilde se escribe sin ella.
		 */
		List<String> palabras = new ArrayList<String>(Arrays.asList("corazón", "árbol", "miel"));

		TableroGenerado generado = generador(11).genera(new ConfiguracionJuego(TAMANO, ModoOrientacion.TODAS, false), palabras);

		for (PosicionPalabra posicion : generado.getPosiciones()) {
			assertEquals("la palabra debe estar escrita sin tildes", Palabras.sinTildes(posicion.getPalabra()), leer(generado.getTablero(), posicion));
		}
		for (int y = 0; y < generado.getTablero().getTamano(); y++) {
			for (int x = 0; x < generado.getTablero().getTamano(); x++) {
				char letra = generado.getTablero().getCelda(x, y);
				assertTrue("el tablero sólo puede contener las 26 letras del abecedario", letra >= 'a' && letra <= 'z');
			}
		}
	}

	@Test
	public void elErrorLlevaElMejorTableroParcial() {
		/*
		 * En un tablero de 4x4 sólo caben dos diagonales, así que de estas
		 * cuatro palabras sólo se pueden colocar dos: la tercera y la cuarta
		 * chocan con las letras que ya están escritas. El número de palabras
		 * queda por debajo del mínimo y el motor da el tablero con más
		 * palabras que ha encontrado, para que la partida siga siendo jugable.
		 */
		List<String> palabras = new ArrayList<String>(Arrays.asList("abcd", "wxyz", "pqrs", "tuvw"));

		try {
			generador(3).genera(new ConfiguracionJuego(4, ModoOrientacion.DIAGONALES, false), palabras);
			assertTrue("se esperaba un error de dominio", false);
		} catch (TableroException e) {
			TableroGenerado parcial = e.getTableroParcial();
			assertNotNull("el error debe llevar el mejor tablero parcial", parcial);
			assertEquals("el tablero parcial debe tener las dos palabras colocadas", 2, parcial.getPosiciones().size());
			for (PosicionPalabra posicion : parcial.getPosiciones()) {
				assertEquals("la palabra debe estar en el tablero parcial", posicion.getPalabra(), leer(parcial.getTablero(), posicion));
			}
			for (int y = 0; y < parcial.getTablero().getTamano(); y++) {
				for (int x = 0; x < parcial.getTablero().getTamano(); x++) {
					assertTrue("el tablero parcial no puede tener celdas vacías", parcial.getTablero().getCelda(x, y) != Tablero.LETRA_RELLENO);
				}
			}
		}
	}
}
