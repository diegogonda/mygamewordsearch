package modelo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Modos de orientación que puede elegir el jugador, con las orientaciones en
 * las que se permite colocar cada palabra.
 */
public enum ModoOrientacion {

	HORIZONTAL,
	VERTICAL,
	HORIZONTAL_Y_VERTICAL,
	DIAGONALES,
	TODAS;

	/**
	 * Traduce el valor guardado en las preferencias (1 a 5) al modo
	 * correspondiente.
	 *
	 * @param valor
	 *            valor de la preferencia
	 * @return el modo de orientación
	 */
	public static ModoOrientacion desdeValor(String valor) {
		try {
			int opcion = Integer.parseInt(valor);
			switch (opcion) {
			case 1:
				return HORIZONTAL;
			case 2:
				return VERTICAL;
			case 3:
				return HORIZONTAL_Y_VERTICAL;
			case 4:
				return DIAGONALES;
			case 5:
				return TODAS;
			default:
				return HORIZONTAL;
			}
		} catch (NumberFormatException e) {
			return HORIZONTAL;
		}
	}

	/**
	 * Orientaciones en las que se puede colocar una palabra con este modo.
	 *
	 * @return las orientaciones permitidas
	 */
	public List<Orientacion> orientacionesPermitidas() {
		switch (this) {
		case HORIZONTAL:
			return Collections.singletonList(Orientacion.HORIZONTAL);
		case VERTICAL:
			return Collections.singletonList(Orientacion.VERTICAL);
		case HORIZONTAL_Y_VERTICAL:
			return Arrays.asList(Orientacion.HORIZONTAL, Orientacion.VERTICAL);
		case DIAGONALES:
			return Arrays.asList(Orientacion.DIAGONAL_1, Orientacion.DIAGONAL_2);
		default:
			return Arrays.asList(Orientacion.HORIZONTAL, Orientacion.VERTICAL, Orientacion.DIAGONAL_1, Orientacion.DIAGONAL_2);
		}
	}
}
