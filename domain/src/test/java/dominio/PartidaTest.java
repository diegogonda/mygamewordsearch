package dominio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Tests de la partida en juego.
 *
 * Lo importante de una partida es que sea coherente: que todas sus palabras
 * se puedan leer en el tablero. Si no lo es, no se puede jugar con ella.
 */
public class PartidaTest {

	/**
	 * Escribe un texto en el tablero empezando en la posición dada, hacia la
	 * derecha.
	 */
	private Tablero tableroCon(String texto, int x, int y, int tamano) {
		Tablero tablero = new Tablero(tamano);
		for (int i = 0; i < tamano; i++) {
			for (int j = 0; j < tamano; j++) {
				tablero.setCelda(j, i, 'a');
			}
		}
		for (int i = 0; i < texto.length(); i++) {
			tablero.setCelda(x + i, y, texto.charAt(i));
		}
		return tablero;
	}

	private List<Celda> celdas(int desde, int cantidad) {
		List<Celda> celdas = new ArrayList<Celda>();
		for (int i = 0; i < cantidad; i++) {
			celdas.add(new Celda(desde + i, 0));
		}
		return celdas;
	}

	@Test
	public void laPartidaGuardaElTableroYLasPalabras() throws PartidaIncoherenteException {
		Tablero tablero = tableroCon("perro", 0, 0, 5);
		PalabraColocada colocada = new PalabraColocada(new Palabra("perro", Pista.nombre("perro")), Orientacion.HORIZONTAL, celdas(0, 5), false);

		Partida partida = Partida.crea(tablero, Arrays.asList(colocada));

		assertEquals("el tablero de la partida", tablero, partida.getTablero());
		assertEquals(1, partida.getPalabras().size());
		assertEquals(1, partida.getCantidadPalabras());
		assertEquals("perro", partida.getPalabras().get(0).getTexto());
	}

	@Test
	public void unaPalabraQueNoEstaEnElTableroNoDejaCrearLaPartida() {
		Tablero tablero = tableroCon("aaaaa", 0, 0, 5);
		PalabraColocada colocada = new PalabraColocada(new Palabra("perro", Pista.nombre("perro")), Orientacion.HORIZONTAL, celdas(0, 5), false);

		try {
			Partida.crea(tablero, Arrays.asList(colocada));
			assertTrue("se esperaba un error de dominio", false);
		} catch (PartidaIncoherenteException e) {
			assertTrue("el error debe nombrar la palabra", e.getMessage().contains("perro"));
		}
	}

	@Test
	public void unaPalabraDelRevesSeLeeDelRevesEnElTablero() throws PartidaIncoherenteException {
		Tablero tablero = tableroCon("orrep", 0, 0, 5);
		PalabraColocada colocada = new PalabraColocada(new Palabra("perro", Pista.nombre("perro")), Orientacion.HORIZONTAL, celdas(0, 5), true);

		Partida partida = Partida.crea(tablero, Arrays.asList(colocada));

		assertEquals(1, partida.getCantidadPalabras());
		assertTrue("la palabra va del revés", partida.getPalabras().get(0).isInvertida());
	}

	@Test
	public void unaCeldaFueraDelTableroNoDejaCrearLaPartida() {
		Tablero tablero = tableroCon("perro", 0, 0, 5);
		List<Celda> celdas = new ArrayList<Celda>();
		celdas.add(new Celda(0, 0));
		celdas.add(new Celda(9, 0));
		PalabraColocada colocada = new PalabraColocada(new Palabra("perro", Pista.nombre("perro")), Orientacion.HORIZONTAL, celdas, false);

		try {
			Partida.crea(tablero, Arrays.asList(colocada));
			assertTrue("se esperaba un error de dominio", false);
		} catch (PartidaIncoherenteException e) {
			assertTrue("el error debe explicar el motivo", e.getMessage().contains("perro"));
		}
	}

	@Test
	public void unaPartidaSinPalabrasEsValida() throws PartidaIncoherenteException {
		Partida partida = Partida.crea(tableroCon("aaaaa", 0, 0, 5), new ArrayList<PalabraColocada>());

		assertEquals(0, partida.getCantidadPalabras());
	}
}
