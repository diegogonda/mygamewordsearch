package modelo;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

/**
 * Tests del mapeo de la preferencia de orientación. Los valores del 1 al 5 son
 * los que usan los ficheros de configuración de las partidas.
 */
public class ModoOrientacionTest {

	@Test
	public void laOpcionUnoEsSoloHorizontal() {
		assertEquals(ModoOrientacion.HORIZONTAL, ModoOrientacion.desdeValor("1"));
	}

	@Test
	public void laOpcionDosEsSoloVertical() {
		assertEquals(ModoOrientacion.VERTICAL, ModoOrientacion.desdeValor("2"));
	}

	@Test
	public void laOpcionTresPermiteHorizontalYVertical() {
		assertEquals(Arrays.asList(Orientacion.HORIZONTAL, Orientacion.VERTICAL), ModoOrientacion.desdeValor("3").orientacionesPermitidas());
	}

	@Test
	public void laOpcionCuatroPermiteLasDiagonales() {
		assertEquals(Arrays.asList(Orientacion.DIAGONAL_1, Orientacion.DIAGONAL_2), ModoOrientacion.desdeValor("4").orientacionesPermitidas());
	}

	@Test
	public void laOpcionCincoPermiteTodas() {
		assertEquals(4, ModoOrientacion.desdeValor("5").orientacionesPermitidas().size());
	}

	@Test
	public void unValorDesconocidoUsaHorizontal() {
		assertEquals(ModoOrientacion.HORIZONTAL, ModoOrientacion.desdeValor("10"));
		assertEquals(ModoOrientacion.HORIZONTAL, ModoOrientacion.desdeValor("no es un número"));
	}
}
