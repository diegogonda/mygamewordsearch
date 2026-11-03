package modelo;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/**
 * Ordena las palabras de un nivel de mayor a menor longitud, que es el orden
 * en el que se intenta colocarlas en el tablero: primero las más largas, que
 * son las que más restringen las posiciones disponibles.
 */
public class OrdenadorPalabras {

	private static Vector<String> Palabras;

	public OrdenadorPalabras(Vector<String> palabras) {
		Palabras = palabras;
		ordenaMayorMenor();
	}

	public Vector<String> devuelveVectorOrdenado() {
		return Palabras;
	}

	/**
	 * La ordenación es estable, de manera que las palabras de la misma
	 * longitud mantienen su orden relativo y un mismo nivel se genera siempre
	 * igual.
	 */
	private void ordenaMayorMenor() {
		Collections.sort(Palabras, new Comparator<String>() {
			public int compare(String primera, String segunda) {
				return segunda.length() - primera.length();
			}
		});
	}
}
