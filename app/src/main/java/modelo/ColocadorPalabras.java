package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Coloca palabras en un tablero.
 *
 * El algoritmo es puro: no depende de Android ni de ningún estado compartido.
 * Si la palabra no cabe, el tablero queda exactamente como estaba.
 */
public class ColocadorPalabras {

	private final Random aleatorio;

	public ColocadorPalabras() {
		this(new Random());
	}

	/**
	 * @param aleatorio
	 *            fuente de azar, que permite generar siempre el mismo tablero
	 *            en las pruebas
	 */
	public ColocadorPalabras(Random aleatorio) {
		this.aleatorio = aleatorio;
	}

	/**
	 * Intenta colocar una palabra en el tablero.
	 *
	 * @param palabra
	 *            palabra a colocar, sin tildes
	 * @param orientacion
	 *            orientación en la que se coloca
	 * @param tablero
	 *            tablero donde se coloca
	 * @return las celdas que ocupa la palabra, o null si no cabe
	 */
	public List<Celda> coloca(String palabra, Orientacion orientacion, Tablero tablero) {
		int longitud = palabra.length();
		if (longitud > tablero.getTamano()) {
			return null;
		}

		int[] caja = orientacion.cajaDeInicio(tablero.getTamano(), longitud);
		int x = caja[0] + aleatorio.nextInt(caja[1] - caja[0] + 1);
		int y = caja[2] + aleatorio.nextInt(caja[3] - caja[2] + 1);

		List<Celda> celdas = new ArrayList<Celda>(longitud);
		List<Character> letrasOriginales = new ArrayList<Character>(longitud);

		for (int i = 0; i < longitud; i++) {
			int celdaX = x + orientacion.getPasoX() * i;
			int celdaY = y + orientacion.getPasoY() * i;
			char letra = palabra.charAt(i);
			char existente = tablero.getCelda(celdaX, celdaY);

			boolean sePuedePoner = existente == Tablero.LETRA_RELLENO || (orientacion.permiteCruce() && existente == letra);
			if (!sePuedePoner) {
				restaurar(tablero, celdas, letrasOriginales);
				return null;
			}

			celdas.add(new Celda(celdaX, celdaY));
			letrasOriginales.add(existente);
			tablero.setCelda(celdaX, celdaY, letra);
		}

		return celdas;
	}

	/**
	 * Devuelve el tablero a su estado anterior, deshaciendo las letras
	 * escritas en este intento.
	 */
	private void restaurar(Tablero tablero, List<Celda> celdas, List<Character> letrasOriginales) {
		for (int i = 0; i < celdas.size(); i++) {
			Celda celda = celdas.get(i);
			tablero.setCelda(celda.getX(), celda.getY(), letrasOriginales.get(i));
		}
	}
}
