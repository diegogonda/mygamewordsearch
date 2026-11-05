package dominio;

/**
 * Las reglas de un tablero de un tamaño dado: cuántas palabras necesita una
 * partida para tener sentido y qué palabras caben en un tablero de ese tamaño.
 *
 * Antes de que existiera esta clase, estos números eran un {@code switch} sin
 * explicación dentro del motor. Ahora son una regla con nombre, y sus tests
 * dicen qué significa cada número.
 */
public final class ReglasDeTablero {

	/**
	 * El tamaño de tablero más pequeño que admite una partida.
	 */
	public static final int TAMANO_MINIMO = 4;

	/**
	 * El tamaño de tablero más grande que admite una partida.
	 */
	public static final int TAMANO_MAXIMO = 12;

	private final int tamano;

	private ReglasDeTablero(int tamano) {
		this.tamano = tamano;
	}

	/**
	 * @param tamano
	 *            el lado del tablero
	 * @return las reglas de un tablero de ese tamaño
	 */
	public static ReglasDeTablero paraTamano(int tamano) {
		return new ReglasDeTablero(tamano);
	}

	/**
	 * @return el lado del tablero al que pertainen estas reglas
	 */
	public int getTamano() {
		return tamano;
	}

	/**
	 * El número mínimo de palabras para que la partida tenga sentido.
	 *
	 * Un tablero de lado N necesita al menos N-1 palabras: con menos, el
	 * jugador tiene tan poco que buscar que la partida no es un juego. A
	 * partir de un tablero de 10x10 se admite algo más de margen, porque las
	 * palabras cortas dejan más sitio y hay más combinaciones posibles.
	 *
	 * @return el número mínimo de palabras
	 */
	public int minimoPalabras() {
		switch (tamano) {
		case 4:
			return 3;
		case 5:
			return 4;
		case 6:
			return 5;
		case 7:
			return 6;
		case 8:
			return 7;
		case 9:
			return 8;
		case 10:
			return 9;
		case 11:
			return 10;
		case 12:
			return 11;
		default:
			return 4;
		}
	}

	/**
	 * Indica si un tablero con este número de palabras da una partida
	 * jugable.
	 *
	 * Sólo hay un mínimo, no un máximo: un tablero no es malo por tener muchas
	 * palabras. El máximo que había antes estaba calculado para un motor que
	 * no dejaba cruzar palabras, y con los cruces un tablero puede tener
	 * muchas más de las que ese máximo permitía.
	 *
	 * @param numeroPalabras
	 *            las palabras colocadas en el tablero
	 * @return true si la partida es jugable
	 */
	public boolean esJugable(int numeroPalabras) {
		return numeroPalabras >= minimoPalabras();
	}

	/**
	 * Indica si una palabra cabe en un tablero de este tamaño.
	 *
	 * @param palabra
	 *            la palabra, con el texto que se escribe en el tablero
	 * @return true si la palabra cabe
	 */
	public boolean cabe(String palabra) {
		return palabra.length() <= tamano;
	}
}
