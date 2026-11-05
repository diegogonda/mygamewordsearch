package dominio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests de la pista de una palabra.
 */
public class PistaTest {

	@Test
	public void laPistaDeNombreGuardaLaPalabra() {
		Pista pista = Pista.nombre("mercurio");

		assertEquals(TipoPista.NOMBRE, pista.getTipo());
		assertEquals("mercurio", pista.getValor());
	}

	@Test
	public void laPistaDeDescripcionGuardaElTexto() {
		Pista pista = Pista.descripcion("el planeta más cercano al sol");

		assertEquals(TipoPista.DESCRIPCION, pista.getTipo());
		assertEquals("el planeta más cercano al sol", pista.getValor());
	}

	@Test
	public void laPistaDeImagenGuardaLaRuta() {
		Pista pista = Pista.imagen("mercurio.jpg");

		assertEquals(TipoPista.IMAGEN, pista.getTipo());
		assertEquals("mercurio.jpg", pista.getValor());
	}

	@Test
	public void laPistaDeAudioGuardaElNombre() {
		Pista pista = Pista.audio("mercurio.mp3");

		assertEquals(TipoPista.AUDIO, pista.getTipo());
		assertEquals("mercurio.mp3", pista.getValor());
	}

	@Test
	public void laPistaDeCategoriaGuardaLaCategoria() {
		Pista pista = Pista.categoria("Planetas");

		assertEquals(TipoPista.CATEGORIA, pista.getTipo());
		assertEquals("Planetas", pista.getValor());
	}

	@Test
	public void laPistaDeMarcaNoTieneValor() {
		Pista pista = Pista.marca();

		assertEquals(TipoPista.MARCA, pista.getTipo());
		assertTrue("la marca no tiene valor", pista.getValor().isEmpty());
	}

	@Test
	public void laPistaSinPistaNoTieneValor() {
		Pista pista = Pista.sinPista();

		assertEquals(TipoPista.SIN_PISTA, pista.getTipo());
		assertTrue("sin pista no hay valor", pista.getValor().isEmpty());
	}
}
