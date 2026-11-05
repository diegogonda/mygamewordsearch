package dominio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests de las reglas de un tablero.
 *
 * Estos tests dicen qué significa cada número, que es justo lo que un
 * {@code switch} sin explicación no dice.
 */
public class ReglasDeTableroTest {

	@Test
	public void elMinimoEsUnaMenosQueElLadoDelTablero() {
		for (int tamano = ReglasDeTablero.TAMANO_MINIMO; tamano <= 9; tamano++) {
			assertEquals("el mínimo de un tablero de " + tamano, tamano - 1, ReglasDeTablero.paraTamano(tamano).minimoPalabras());
		}
	}

	@Test
	public void aPartirDeUnTableroDeDiezHayMasMargen() {
		assertEquals(9, ReglasDeTablero.paraTamano(10).minimoPalabras());
		assertEquals(10, ReglasDeTablero.paraTamano(11).minimoPalabras());
		assertEquals(11, ReglasDeTablero.paraTamano(12).minimoPalabras());
	}

	@Test
	public void elMaximoEsElLadoDelTableroHastaNueve() {
		for (int tamano = ReglasDeTablero.TAMANO_MINIMO; tamano <= 9; tamano++) {
			assertEquals("el máximo de un tablero de " + tamano, tamano, ReglasDeTablero.paraTamano(tamano).maximoPalabras());
		}
	}

	@Test
	public void aPartirDeUnTableroDeDiezElMaximoDejaMargen() {
		assertEquals(11, ReglasDeTablero.paraTamano(10).maximoPalabras());
		assertEquals(12, ReglasDeTablero.paraTamano(11).maximoPalabras());
		assertEquals(14, ReglasDeTablero.paraTamano(12).maximoPalabras());
	}

	@Test
	public void unTableroEsJugableConPalabrasEntreElMinimoYElMaximo() {
		ReglasDeTablero reglas = ReglasDeTablero.paraTamano(9);

		assertFalse("con menos del mínimo no hay juego", reglas.esJugable(reglas.minimoPalabras() - 1));
		assertTrue("con el mínimo hay juego", reglas.esJugable(reglas.minimoPalabras()));
		assertTrue("con el máximo hay juego", reglas.esJugable(reglas.maximoPalabras()));
		assertFalse("con más del máximo no hay juego", reglas.esJugable(reglas.maximoPalabras() + 1));
	}

	@Test
	public void unaPalabraCabeSiNoEsMasLargaQueElTablero() {
		ReglasDeTablero reglas = ReglasDeTablero.paraTamano(9);

		assertTrue("una palabra del tamaño del tablero cabe", reglas.cabe("marte"));
		assertTrue("una palabra más corta cabe", reglas.cabe("luna"));
		assertFalse("una palabra más larga no cabe", reglas.cabe("mercurioxx"));
	}

	@Test
	public void lasReglasSabenDeQueTableroHablan() {
		assertEquals(9, ReglasDeTablero.paraTamano(9).getTamano());
	}
}
