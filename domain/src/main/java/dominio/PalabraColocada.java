package dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Una palabra colocada en el tablero: dónde está, en qué orientación y si va
 * del revés.
 *
 * Es la palabra del juego con su pista y su sitio. No hay dos conceptos
 * para lo mismo: el motor coloca palabras y devuelve esto.
 */
public final class PalabraColocada {

	private final Palabra palabra;
	private final Orientacion orientacion;
	private final List<Celda> celdas;
	private final boolean invertida;

	public PalabraColocada(Palabra palabra, Orientacion orientacion, List<Celda> celdas, boolean invertida) {
		this.palabra = palabra;
		this.orientacion = orientacion;
		this.celdas = Collections.unmodifiableList(new ArrayList<Celda>(celdas));
		this.invertida = invertida;
	}

	/**
	 * @return la palabra con su pista
	 */
	public Palabra getPalabra() {
		return palabra;
	}

	/**
	 * @return el texto de la palabra, tal y como lo ve el jugador
	 */
	public String getTexto() {
		return palabra.getTexto();
	}

	/**
	 * @return la pista que ayuda a encontrar la palabra
	 */
	public Pista getPista() {
		return palabra.getPista();
	}

	/**
	 * @return la orientación en la que está escrita
	 */
	public Orientacion getOrientacion() {
		return orientacion;
	}

	/**
	 * @return las celdas que ocupa, en orden de lectura
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
