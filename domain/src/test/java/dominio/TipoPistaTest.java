package dominio;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests de los tipos de pista del dominio.
 *
 * Los valores son los que usan los ficheros de contenido de los niveles, así
 * que el enum es la traducción de un vocabulario que ya existía disperso por
 * la aplicación.
 */
public class TipoPistaTest {

	@Test
	public void cadaTipoTieneElValorDelContenido() {
		assertEquals("nombre", TipoPista.NOMBRE.getValor());
		assertEquals("pista_descripcion", TipoPista.DESCRIPCION.getValor());
		assertEquals("pista_imagen", TipoPista.IMAGEN.getValor());
		assertEquals("pista_audio", TipoPista.AUDIO.getValor());
		assertEquals("pista_marca", TipoPista.MARCA.getValor());
		assertEquals("pista_categoria", TipoPista.CATEGORIA.getValor());
		assertEquals("sin_pista", TipoPista.SIN_PISTA.getValor());
	}

	@Test
	public void desdeValorEncuentraCadaTipo() {
		assertEquals(TipoPista.NOMBRE, TipoPista.desdeValor("nombre"));
		assertEquals(TipoPista.DESCRIPCION, TipoPista.desdeValor("pista_descripcion"));
		assertEquals(TipoPista.IMAGEN, TipoPista.desdeValor("pista_imagen"));
		assertEquals(TipoPista.AUDIO, TipoPista.desdeValor("pista_audio"));
		assertEquals(TipoPista.MARCA, TipoPista.desdeValor("pista_marca"));
		assertEquals(TipoPista.CATEGORIA, TipoPista.desdeValor("pista_categoria"));
		assertEquals(TipoPista.SIN_PISTA, TipoPista.desdeValor("sin_pista"));
	}

	@Test
	public void unValorDesconocidoEsSinPista() {
		assertEquals(TipoPista.SIN_PISTA, TipoPista.desdeValor("lo que sea"));
	}

	@Test
	public void unValorNuloEsSinPista() {
		assertEquals(TipoPista.SIN_PISTA, TipoPista.desdeValor(null));
	}

	@Test
	public void cadaTipoSeSabeDevolverASiMismo() {
		for (TipoPista tipo : TipoPista.values()) {
			assertEquals(tipo, TipoPista.desdeValor(tipo.getValor()));
		}
	}
}
