package dominio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests de la palabra de la partida: el texto que ve el jugador y la pista
 * que le ayuda a encontrarla.
 */
public class PalabraTest {

	@Test
	public void laPalabraGuardaElTextoYLaPista() {
		Palabra palabra = new Palabra("júpiter", Pista.nombre("júpiter"));

		assertEquals("júpiter", palabra.getTexto());
		assertEquals(TipoPista.NOMBRE, palabra.getPista().getTipo());
	}

	@Test
	public void elTextoEnElTableroNoTieneTildes() {
		assertEquals("jupiter", new Palabra("júpiter", Pista.nombre("júpiter")).getTextoEnTablero());
	}

	@Test
	public void elTextoEnElTableroInvertidoEsElAlReves() {
		assertEquals("retipuj", new Palabra("júpiter", Pista.nombre("júpiter")).getTextoEnTableroInvertido());
	}

	@Test
	public void quitaLasTildes() {
		assertEquals("aeiou", Palabra.sinTildes("áéíóú"));
	}

	@Test
	public void quitaLaTildeDeLaO() {
		assertEquals("corazon", Palabra.sinTildes("corazón"));
	}

	@Test
	public void dejaLaEnnePorqueEsUnaLetraDelAbecedario() {
		assertEquals("ñandu", Palabra.sinTildes("ñandú"));
	}

	@Test
	public void dejaLasPalabrasSinTildeComoEstan() {
		assertEquals("marte", Palabra.sinTildes("marte"));
	}

	@Test
	public void invierteLaPalabra() {
		assertEquals("los", Palabra.invertida("sol"));
	}

	@Test
	public void unaPalabraVaciaNoEsUnaPalabra() {
		try {
			new Palabra("", Pista.nombre(""));
			assertTrue("se esperaba un error de dominio", false);
		} catch (IllegalArgumentException e) {
			assertTrue("el error debe explicar el motivo", e.getMessage().contains("vacía"));
		}
	}

	@Test
	public void unaPalabraSinPistaNoEsUnaPalabra() {
		try {
			new Palabra("marte", null);
			assertTrue("se esperaba un error de dominio", false);
		} catch (IllegalArgumentException e) {
			assertTrue("el error debe explicar el motivo", e.getMessage().contains("pista"));
		}
	}
}
