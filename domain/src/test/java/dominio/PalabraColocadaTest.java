package dominio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

/**
 * Tests de la palabra colocada en el tablero.
 */
public class PalabraColocadaTest {

	@Test
	public void laPalabraColocadaSabeSuTextoYPista() {
		Palabra palabra = new Palabra("mercurio", Pista.imagen("mercurio.jpg"));

		PalabraColocada colocada = new PalabraColocada(palabra, Orientacion.HORIZONTAL, Arrays.asList(new Celda(0, 0)), false);

		assertEquals("mercurio", colocada.getTexto());
		assertEquals(TipoPista.IMAGEN, colocada.getPista().getTipo());
		assertEquals("mercurio.jpg", colocada.getPista().getValor());
	}

	@Test
	public void laPalabraColocadaSabeSuOrientacionYSusCeldas() {
		Palabra palabra = new Palabra("mercurio", Pista.nombre("mercurio"));

		PalabraColocada colocada = new PalabraColocada(palabra, Orientacion.DIAGONAL_2, Arrays.asList(new Celda(4, 0), new Celda(3, 1)), false);

		assertEquals(Orientacion.DIAGONAL_2, colocada.getOrientacion());
		assertEquals(2, colocada.getCeldas().size());
		assertEquals(4, colocada.getCeldas().get(0).getX());
		assertEquals(1, colocada.getCeldas().get(1).getY());
	}

	@Test
	public void laPalabraColocadaSabeSiVaDelReves() {
		Palabra palabra = new Palabra("mercurio", Pista.nombre("mercurio"));

		PalabraColocada normal = new PalabraColocada(palabra, Orientacion.HORIZONTAL, Arrays.asList(new Celda(0, 0)), false);
		PalabraColocada invertida = new PalabraColocada(palabra, Orientacion.HORIZONTAL, Arrays.asList(new Celda(0, 0)), true);

		assertFalse(normal.isInvertida());
		assertTrue(invertida.isInvertida());
	}
}
