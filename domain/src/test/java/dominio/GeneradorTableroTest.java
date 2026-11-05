package dominio;

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

	private List<Palabra> palabrasPlanetas() {
		List<Palabra> palabras = new ArrayList<Palabra>();
		for (String texto : Arrays.asList("mercurio", "venus", "tierra", "marte", "jupiter", "saturno", "urano", "neptuno")) {
			palabras.add(new Palabra(texto, Pista.nombre(texto)));
		}
		return palabras;
	}

	private List<Palabra> palabras(String... textos) {
		List<Palabra> palabras = new ArrayList<Palabra>();
		for (String texto : textos) {
			palabras.add(new Palabra(texto, Pista.nombre(texto)));
		}
		return palabras;
	}

	/**
	 * Lee la palabra del tablero siguiendo la orientación en la que está.
	 */
	private String leer(Tablero tablero, PalabraColocada posicion) {
		StringBuilder palabra = new StringBuilder();
		for (Celda celda : posicion.getCeldas()) {
			palabra.append(tablero.getCelda(celda.getX(), celda.getY()));
		}
		return palabra.toString();
	}

	@Test
	public void colocaTodasLasPalabrasEnElTablero() throws TableroException {
		Partida partida = generador(42).genera(configuracion(ModoOrientacion.TODAS), palabrasPlanetas());

		assertEquals("todas las palabras deben caber en el tablero", 8, partida.getPalabras().size());
	}

	@Test
	public void lasPalabrasQuedanEscritasEnElTablero() throws TableroException {
		Partida partida = generador(7).genera(configuracion(ModoOrientacion.TODAS), palabrasPlanetas());

		for (PalabraColocada posicion : partida.getPalabras()) {
			assertEquals("la palabra debe estar escrita en el tablero", posicion.getTexto(), leer(partida.getTablero(), posicion));
		}
	}

	@Test
	public void laPalabraConservaSuPista() throws TableroException {
		List<Palabra> palabras = new ArrayList<Palabra>();
		palabras.add(new Palabra("mercurio", Pista.descripcion("el más cercano al sol")));

		Partida partida = generador(5).genera(configuracion(ModoOrientacion.TODAS), palabras);

		assertEquals(TipoPista.DESCRIPCION, partida.getPalabras().get(0).getPista().getTipo());
		assertEquals("el más cercano al sol", partida.getPalabras().get(0).getPista().getValor());
	}

	@Test
	public void respetaElModoDeOrientacion() throws TableroException {
		Partida horizontal = generador(3).genera(configuracion(ModoOrientacion.HORIZONTAL), palabrasPlanetas());
		for (PalabraColocada posicion : horizontal.getPalabras()) {
			assertEquals(Orientacion.HORIZONTAL, posicion.getOrientacion());
		}

		Partida vertical = generador(3).genera(configuracion(ModoOrientacion.VERTICAL), palabrasPlanetas());
		for (PalabraColocada posicion : vertical.getPalabras()) {
			assertEquals(Orientacion.VERTICAL, posicion.getOrientacion());
		}

		Partida diagonales = generador(3).genera(configuracion(ModoOrientacion.DIAGONALES), palabrasPlanetas());
		for (PalabraColocada posicion : diagonales.getPalabras()) {
			assertTrue(posicion.getOrientacion() == Orientacion.DIAGONAL_1 || posicion.getOrientacion() == Orientacion.DIAGONAL_2);
		}
	}

	@Test
	public void elTableroNoQuedanCeldasVacias() throws TableroException {
		Partida partida = generador(11).genera(configuracion(ModoOrientacion.TODAS), palabrasPlanetas());

		for (int y = 0; y < TAMANO; y++) {
			for (int x = 0; x < TAMANO; x++) {
				assertFalse("no puede quedar ninguna celda vacía", partida.getTablero().getCelda(x, y) == Tablero.LETRA_RELLENO);
			}
		}
	}

	@Test
	public void lasPalabrasInvertidasSeEscribenDelReves() throws TableroException {
		ConfiguracionJuego configuracion = new ConfiguracionJuego(TAMANO, ModoOrientacion.HORIZONTAL, true);

		boolean algunaInvertida = false;
		for (int semilla = 0; semilla < 20 && !algunaInvertida; semilla++) {
			Partida partida = generador(semilla).genera(configuracion, palabrasPlanetas());
			for (PalabraColocada posicion : partida.getPalabras()) {
				if (posicion.isInvertida()) {
					algunaInvertida = true;
					assertEquals("la palabra del tablero va del revés", posicion.getPalabra().getTextoEnTableroInvertido(), leer(partida.getTablero(), posicion));
				}
			}
		}
		assertTrue("alguna palabra debe salir invertida", algunaInvertida);
	}

	@Test
	public void sinPalabrasInvertidasNingunaLoEsta() throws TableroException {
		Partida partida = generador(5).genera(configuracion(ModoOrientacion.TODAS), palabrasPlanetas());

		for (PalabraColocada posicion : partida.getPalabras()) {
			assertFalse(posicion.isInvertida());
		}
	}

	@Test
	public void fallaSiLasPalabrasNoCabenEnElTablero() {
		List<Palabra> demasiadas = new ArrayList<Palabra>();
		for (int i = 0; i < 40; i++) {
			demasiadas.add(new Palabra("palabra" + i, Pista.nombre("palabra" + i)));
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
		Partida primera = generador(99).genera(configuracion(ModoOrientacion.TODAS), palabrasPlanetas());
		Partida segunda = generador(99).genera(configuracion(ModoOrientacion.TODAS), palabrasPlanetas());

		assertTrue("con la misma semilla el tablero debe ser el mismo", Arrays.deepEquals(primera.getTablero().getCeldas(), segunda.getTablero().getCeldas()));
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
		Partida partida = generador(13).genera(new ConfiguracionJuego(4, ModoOrientacion.TODAS, false), palabras("luna", "casa", "gato", "mesa", "paz"));

		for (PalabraColocada posicion : partida.getPalabras()) {
			assertEquals("la palabra debe estar en el tablero", posicion.getTexto(), leer(partida.getTablero(), posicion));
		}
		assertTrue("debe haber al menos un par de palabras", partida.getPalabras().size() >= 2);
		assertTrue("no pueden caber todas", partida.getPalabras().size() <= 4);
	}

	@Test
	public void fallaSiUnaPalabraNoCabeEnElTablero() {
		try {
			generador(1).genera(new ConfiguracionJuego(4, ModoOrientacion.TODAS, false), palabras("tierra"));
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
		List<Palabra> palabras = palabras("luna", "gato", "casa", "mesa");

		Partida partida = generador(21).genera(new ConfiguracionJuego(TAMANO, ModoOrientacion.TODAS, false), palabras);

		List<String> colocadas = new ArrayList<String>();
		for (PalabraColocada posicion : partida.getPalabras()) {
			colocadas.add(posicion.getTexto());
		}
		assertEquals("el orden de las palabras debe respetarse", Arrays.asList("luna", "gato", "casa", "mesa"), colocadas);
	}

	@Test
	public void lasPalabrasConTildeSeEscribenSinTilde() throws TableroException {
		/*
		 * El tablero sólo tiene las 26 letras del abecedario, así que una
		 * palabra con tilde se escribe sin ella.
		 */
		Partida partida = generador(11).genera(new ConfiguracionJuego(TAMANO, ModoOrientacion.TODAS, false), palabras("corazón", "árbol", "miel"));

		for (PalabraColocada posicion : partida.getPalabras()) {
			assertEquals("la palabra debe estar escrita sin tildes", posicion.getPalabra().getTextoEnTablero(), leer(partida.getTablero(), posicion));
		}
		for (int y = 0; y < partida.getTablero().getTamano(); y++) {
			for (int x = 0; x < partida.getTablero().getTamano(); x++) {
				char letra = partida.getTablero().getCelda(x, y);
				assertTrue("el tablero sólo puede contener las 26 letras del abecedario", letra >= 'a' && letra <= 'z');
			}
		}
	}

	@Test
	public void unaListaDePalabrasNullNoEsUnaPartida() {
		try {
			generador(1).genera(configuracion(ModoOrientacion.TODAS), null);
			assertTrue("se esperaba un error de dominio", false);
		} catch (IllegalArgumentException e) {
			assertTrue("el error debe explicar el motivo", e.getMessage().contains("null"));
		} catch (TableroException e) {
			assertTrue("se esperaba un error de dominio", false);
		}
	}

	@Test
	public void elErrorLlevaLaMejorPartidaParcial() {
		/*
		 * En un tablero de 4x4 sólo caben dos diagonales, así que de estas
		 * cuatro palabras sólo se pueden colocar dos: la tercera y la cuarta
		 * chocan con las letras que ya están escritas. El número de palabras
		 * queda por debajo del mínimo y el motor da la partida con más
		 * palabras que ha encontrado, para que la partida siga siendo jugable.
		 */
		List<Palabra> palabras = palabras("abcd", "wxyz", "pqrs", "tuvw");

		try {
			generador(3).genera(new ConfiguracionJuego(4, ModoOrientacion.DIAGONALES, false), palabras);
			assertTrue("se esperaba un error de dominio", false);
		} catch (TableroException e) {
			Partida parcial = e.getPartidaParcial();
			assertNotNull("el error debe llevar la mejor partida parcial", parcial);
			assertEquals("la partida parcial debe tener las dos palabras colocadas", 2, parcial.getPalabras().size());
			for (PalabraColocada posicion : parcial.getPalabras()) {
				assertEquals("la palabra debe estar en el tablero parcial", posicion.getTexto(), leer(parcial.getTablero(), posicion));
			}
			for (int y = 0; y < parcial.getTablero().getTamano(); y++) {
				for (int x = 0; x < parcial.getTablero().getTamano(); x++) {
					assertTrue("el tablero parcial no puede tener celdas vacías", parcial.getTablero().getCelda(x, y) != Tablero.LETRA_RELLENO);
				}
			}
		}
	}
}
