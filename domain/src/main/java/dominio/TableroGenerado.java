package dominio;

/**
 * Resultado de generar un tablero: las letras y dónde quedó cada palabra.
 */
public final class TableroGenerado {

	private final Tablero tablero;
	private final java.util.List<PosicionPalabra> posiciones;

	public TableroGenerado(Tablero tablero, java.util.List<PosicionPalabra> posiciones) {
		this.tablero = tablero;
		this.posiciones = java.util.Collections.unmodifiableList(new java.util.ArrayList<PosicionPalabra>(posiciones));
	}

	public Tablero getTablero() {
		return tablero;
	}

	/**
	 * Palabras colocadas en el tablero, con su posición.
	 *
	 * @return las posiciones de las palabras
	 */
	public java.util.List<PosicionPalabra> getPosiciones() {
		return posiciones;
	}
}
