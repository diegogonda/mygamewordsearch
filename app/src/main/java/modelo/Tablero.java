package modelo;

/**
 * Tablero de juego: una cuadrícula de letras.
 */
public final class Tablero {

	/**
	 * Carácter con el que se marca una celda todavía vacía.
	 */
	public static final char LETRA_RELLENO = '/';

	private final char[][] celdas;
	private final int tamano;

	public Tablero(int tamano) {
		this.tamano = tamano;
		this.celdas = new char[tamano][tamano];
		rellena();
	}

	/**
	 * Deja el tablero sin ninguna letra colocada.
	 */
	public void rellena() {
		for (int y = 0; y < tamano; y++) {
			for (int x = 0; x < tamano; x++) {
				celdas[y][x] = LETRA_RELLENO;
			}
		}
	}

	public int getTamano() {
		return tamano;
	}

	public char getCelda(int x, int y) {
		return celdas[y][x];
	}

	public void setCelda(int x, int y, char letra) {
		celdas[y][x] = letra;
	}

	/**
	 * Copia de las letras del tablero, con las filas en el primer índice y las
	 * columnas en el segundo.
	 *
	 * @return las letras del tablero
	 */
	public char[][] getCeldas() {
		char[][] copia = new char[tamano][tamano];
		for (int y = 0; y < tamano; y++) {
			System.arraycopy(celdas[y], 0, copia[y], 0, tamano);
		}
		return copia;
	}
}
