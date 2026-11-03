package modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Test;

/**
 * Tests de la ordenación de palabras por longitud, que decide el orden en el
 * que se intenta colocar cada palabra en el tablero.
 */
public class OrdenadorPalabrasTest {

	@Test
	public void ordenaLasPalabrasDeMayorAMenorLongitud() {
		Vector<String> palabras = new Vector<String>();
		palabras.add("lua");
		palabras.add("mercurio");
		palabras.add("tierra");
		palabras.add("marte");

		Vector<String> ordenadas = new OrdenadorPalabras(palabras).devuelveVectorOrdenado();

		assertEquals("mercurio", ordenadas.get(0));
		assertEquals("tierra", ordenadas.get(1));
		assertEquals("marte", ordenadas.get(2));
		assertEquals("lua", ordenadas.get(3));
	}

	@Test
	public void conservaTodasLasPalabras() {
		Vector<String> palabras = new Vector<String>();
		palabras.add("marte");
		palabras.add("tierra");
		palabras.add("venus");
		palabras.add("luna");
		palabras.add("sol");
		palabras.add("pluton");
		/*
		 * La ordenación modifica el vector recibido, así que se guarda una
		 * copia para poder comparar después.
		 */
		Vector<String> originales = new Vector<String>(palabras);

		Vector<String> ordenadas = new OrdenadorPalabras(palabras).devuelveVectorOrdenado();

		assertEquals("no se debe perder ninguna palabra", originales.size(), ordenadas.size());
		for (String palabra : originales) {
			assertTrue("falta la palabra " + palabra, ordenadas.contains(palabra));
		}
	}

	@Test
	public void conservaElOrdenRelativoDeLasPalabrasDeIgualLongitud() {
		Vector<String> palabras = new Vector<String>();
		palabras.add("marte");
		palabras.add("venus");
		palabras.add("tierra");
		palabras.add("sol");

		Vector<String> ordenadas = new OrdenadorPalabras(palabras).devuelveVectorOrdenado();

		/*
		 * "tierra" es la más larga, "marte" y "venus" tienen la misma longitud
		 * y deben mantener su orden relativo, y "sol" es la más corta. Es lo
		 * que hace que un nivel se genere siempre igual.
		 */
		assertEquals("tierra", ordenadas.get(0));
		assertEquals("marte", ordenadas.get(1));
		assertEquals("venus", ordenadas.get(2));
		assertEquals("sol", ordenadas.get(3));
	}

	@Test
	public void conservaElOrdenDeTresPalabrasDeLaMismaLongitud() {
		Vector<String> palabras = new Vector<String>();
		palabras.add("marte");
		palabras.add("venus");
		palabras.add("pluto");

		Vector<String> ordenadas = new OrdenadorPalabras(palabras).devuelveVectorOrdenado();

		assertEquals("marte", ordenadas.get(0));
		assertEquals("venus", ordenadas.get(1));
		assertEquals("pluto", ordenadas.get(2));
	}
}
