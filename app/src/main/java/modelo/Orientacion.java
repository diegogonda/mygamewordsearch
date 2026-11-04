package modelo;

/**
 * Orientaciones en las que se puede colocar una palabra en el tablero.
 *
 * El vector (pasoX, pasoY) indica en qué dirección avanzan las letras de la
 * palabra, desde la celda inicial hasta la final.
 */
public enum Orientacion {

	HORIZONTAL(1, 0, true),
	VERTICAL(0, 1, false),
	DIAGONAL_1(1, 1, true),
	DIAGONAL_2(-1, 1, true);

	private final int pasoX;
	private final int pasoY;
	private final boolean permiteCruce;

	private Orientacion(int pasoX, int pasoY, boolean permiteCruce) {
		this.pasoX = pasoX;
		this.pasoY = pasoY;
		this.permiteCruce = permiteCruce;
	}

	public int getPasoX() {
		return pasoX;
	}

	public int getPasoY() {
		return pasoY;
	}

	/**
	 * Indica si una palabra puede cruzar con otra que ya ocupe la celda. Solo
	 * es posible cuando la letra que hay en la celda es la misma que la que
	 * toca colocar.
	 *
	 * @return true si se puede cruzar
	 */
	public boolean permiteCruce() {
		return permiteCruce;
	}

	/**
	 * Caja dentro de la que puede empezar la palabra, en coordenadas del
	 * tablero. Se devuelve como {minX, maxX, minY, maxY}.
	 *
	 * @param tamano
	 *            lado del tablero
	 * @param longitudPalabra
	 *            número de letras de la palabra
	 * @return la caja de inicio de la palabra
	 */
	public int[] cajaDeInicio(int tamano, int longitudPalabra) {
		switch (this) {
		case HORIZONTAL:
			return new int[] { 0, tamano - longitudPalabra, 0, tamano - 1 };
		case VERTICAL:
			return new int[] { 0, tamano - 1, 0, tamano - longitudPalabra };
		case DIAGONAL_1:
			return new int[] { 0, tamano - longitudPalabra, 0, tamano - longitudPalabra };
		default:
			return new int[] { longitudPalabra - 1, tamano - 1, 0, tamano - longitudPalabra };
		}
	}
}
