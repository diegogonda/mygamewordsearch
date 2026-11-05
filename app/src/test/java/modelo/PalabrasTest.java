package modelo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests de las utilidades sobre el texto de las palabras del tablero.
 */
public class PalabrasTest {

	@Test
	public void quitaLasTildes() {
		assertEquals("aeiou", Palabras.sinTildes("áéíóú"));
	}

	@Test
	public void quitaLaTildeDeLaO() {
		assertEquals("corazon", Palabras.sinTildes("corazón"));
	}

	@Test
	public void dejaLaEnnePorqueEsUnaLetraDelAbecedario() {
		assertEquals("ñandu", Palabras.sinTildes("ñandú"));
	}

	@Test
	public void dejaLasPalabrasSinTildeComoEstan() {
		assertEquals("marte", Palabras.sinTildes("marte"));
	}

	@Test
	public void invierteLaPalabra() {
		assertEquals("los", Palabras.invertida("sol"));
	}
}
