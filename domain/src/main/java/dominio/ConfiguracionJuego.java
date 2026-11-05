package dominio;

/**
 * Configuración de una partida: cómo se genera el tablero.
 */
public final class ConfiguracionJuego {

	private final int tamanoTablero;
	private final ModoOrientacion modoOrientacion;
	private final boolean palabrasInvertidas;

	public ConfiguracionJuego(int tamanoTablero, ModoOrientacion modoOrientacion, boolean palabrasInvertidas) {
		this.tamanoTablero = tamanoTablero;
		this.modoOrientacion = modoOrientacion;
		this.palabrasInvertidas = palabrasInvertidas;
	}

	public int getTamanoTablero() {
		return tamanoTablero;
	}

	public ModoOrientacion getModoOrientacion() {
		return modoOrientacion;
	}

	/**
	 * @return true si algunas palabras se escriben del revés en el tablero
	 */
	public boolean isPalabrasInvertidas() {
		return palabrasInvertidas;
	}
}
