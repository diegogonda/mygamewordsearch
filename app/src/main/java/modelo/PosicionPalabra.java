package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Palabra colocada en el tablero: dónde está y en qué orientación.
 *
 * Solo contiene información del dominio. Las coordenadas en píxeles y el
 * color los calcula la capa de presentación.
 */
public final class PosicionPalabra {

	private final String palabra;
	private final Orientacion orientacion;
	private final List<Celda> celdas;
	private final boolean invertida;

	public PosicionPalabra(String palabra, Orientacion orientacion, List<Celda> celdas, boolean invertida) {
		this.palabra = palabra;
		this.orientacion = orientacion;
		this.celdas = Collections.unmodifiableList(new ArrayList<Celda>(celdas));
		this.invertida = invertida;
	}

	public String getPalabra() {
		return palabra;
	}

	public Orientacion getOrientacion() {
		return orientacion;
	}

	/**
	 * Celdas que ocupa la palabra, en orden de lectura.
	 *
	 * @return las celdas de la palabra
	 */
	public List<Celda> getCeldas() {
		return celdas;
	}

	/**
	 * @return true si la palabra está escrita del revés en el tablero
	 */
	public boolean isInvertida() {
		return invertida;
	}
}
