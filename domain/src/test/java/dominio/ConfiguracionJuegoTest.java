package dominio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests de la configuración de una partida.
 */
public class ConfiguracionJuegoTest {

	@Test
	public void guardaLoQueLePiden() {
		ConfiguracionJuego configuracion = new ConfiguracionJuego(9, ModoOrientacion.TODAS, true);

		assertEquals(9, configuracion.getTamanoTablero());
		assertEquals(ModoOrientacion.TODAS, configuracion.getModoOrientacion());
		assertTrue(configuracion.isPalabrasInvertidas());
	}

	@Test
	public void unTableroDemasiadoPequenoNoEsUnaConfiguracion() {
		try {
			new ConfiguracionJuego(3, ModoOrientacion.TODAS, false);
			assertTrue("se esperaba un error de dominio", false);
		} catch (IllegalArgumentException e) {
			assertTrue("el error debe explicar el motivo", e.getMessage().contains("3"));
		}
	}

	@Test
	public void unTableroDemasiadoGrandeNoEsUnaConfiguracion() {
		try {
			new ConfiguracionJuego(13, ModoOrientacion.TODAS, false);
			assertTrue("se esperaba un error de dominio", false);
		} catch (IllegalArgumentException e) {
			assertTrue("el error debe explicar el motivo", e.getMessage().contains("13"));
		}
	}

	@Test
	public void unTableroDeMedidaCeroNoEsUnaConfiguracion() {
		try {
			new ConfiguracionJuego(0, ModoOrientacion.TODAS, false);
			assertTrue("se esperaba un error de dominio", false);
		} catch (IllegalArgumentException e) {
			assertTrue("el error debe explicar el motivo", e.getMessage().contains("0"));
		}
	}

	@Test
	public void sinModoDeOrientacionNoHayConfiguracion() {
		try {
			new ConfiguracionJuego(9, null, false);
			assertTrue("se esperaba un error de dominio", false);
		} catch (IllegalArgumentException e) {
			assertTrue("el error debe explicar el motivo", e.getMessage().contains("orientación"));
		}
	}
}
