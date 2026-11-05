package objetos;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import modelo.Celda;
import modelo.Orientacion;
import modelo.PosicionPalabra;
import modelo.TableroGenerado;

/**
 * Traduce el resultado del motor de tableros a los datos que necesita la
 * presentación: las letras del tablero y la posición de cada palabra.
 *
 * No depende de Android, así que se puede probar en la JVM.
 */
public final class AdaptadorTablero {

	private AdaptadorTablero() {
	}

	/**
	 * Resultado de adaptar un tablero: las letras del tablero y las palabras
	 * que han quedado colocadas, con su posición ya rellenada.
	 */
	public static final class TableroPreparado {

		private final char[][] letras;
		private final List<PosicionesPalabras> palabras;

		TableroPreparado(char[][] letras, List<PosicionesPalabras> palabras) {
			this.letras = letras;
			this.palabras = palabras;
		}

		/**
		 * @return las letras del tablero, con las filas en el primer índice
		 */
		public char[][] getLetras() {
			return letras;
		}

		/**
		 * @return las palabras colocadas, en el orden de la lista de la
		 *         partida
		 */
		public List<PosicionesPalabras> getPalabras() {
			return palabras;
		}
	}

	/**
	 * Aplica las posiciones de un tablero generado sobre la lista de palabras
	 * de la partida.
	 *
	 * Las palabras que el motor no ha podido colocar se quedan fuera del
	 * resultado. Las que sí quedan conservan el orden de la lista original, y
	 * si una palabra está repetida cada copia va a su propio sitio.
	 *
	 * @param generado
	 *            resultado del motor de tableros
	 * @param palabras
	 *            palabras de la partida
	 * @return las letras del tablero y las palabras colocadas
	 */
	public static TableroPreparado adapta(TableroGenerado generado, List<PosicionesPalabras> palabras) {
		Map<String, Deque<PosicionesPalabras>> porPalabra = indexa(palabras);

		for (PosicionPalabra posicion : generado.getPosiciones()) {
			Deque<PosicionesPalabras> candidatas = porPalabra.get(posicion.getPalabra());
			if (candidatas == null || candidatas.isEmpty()) {
				continue;
			}
			coloca(candidatas.remove(), posicion);
		}

		List<PosicionesPalabras> colocadas = new ArrayList<PosicionesPalabras>();
		for (PosicionesPalabras palabra : palabras) {
			if (palabra.getMostradaPantalla()) {
				colocadas.add(palabra);
			}
		}

		return new TableroPreparado(generado.getTablero().getCeldas(), colocadas);
	}

	/**
	 * Indexa las palabras de la partida por su texto, para poder asignar cada
	 * posición a la palabra a la que corresponde.
	 */
	private static Map<String, Deque<PosicionesPalabras>> indexa(List<PosicionesPalabras> palabras) {
		Map<String, Deque<PosicionesPalabras>> porPalabra = new LinkedHashMap<String, Deque<PosicionesPalabras>>();
		for (PosicionesPalabras palabra : palabras) {
			palabra.setMostradaPantalla(false);
			Deque<PosicionesPalabras> copias = porPalabra.get(palabra.getPalabra());
			if (copias == null) {
				copias = new ArrayDeque<PosicionesPalabras>();
				porPalabra.put(palabra.getPalabra(), copias);
			}
			copias.add(palabra);
		}
		return porPalabra;
	}

	/**
	 * Rellena la posición de una palabra colocada.
	 */
	private static void coloca(PosicionesPalabras destino, PosicionPalabra posicion) {
		List<Celda> celdas = posicion.getCeldas();
		Celda inicio = celdas.get(0);
		Celda fin = celdas.get(celdas.size() - 1);

		int[] xLetras = new int[celdas.size()];
		int[] yLetras = new int[celdas.size()];
		for (int i = 0; i < celdas.size(); i++) {
			xLetras[i] = celdas.get(i).getX();
			yLetras[i] = celdas.get(i).getY();
		}

		destino.setxTableroInicial(inicio.getX());
		destino.setyTableroInicial(inicio.getY());
		destino.setxTableroFinal(fin.getX());
		destino.setyTableroFinal(fin.getY());
		destino.setxTableroLetras(xLetras);
		destino.setyTableroLetras(yLetras);
		destino.setOrientacion(codigoOrientacion(posicion.getOrientacion()));
		destino.setInvertida(posicion.isInvertida());
		destino.setElegida(false);
		destino.setMostradaPantalla(true);
	}

	/**
	 * Traduce la orientación del motor al código que usa la presentación:
	 * 0 horizontal, 1 vertical, 2 diagonal principal, 3 diagonal secundaria.
	 */
	private static int codigoOrientacion(Orientacion orientacion) {
		switch (orientacion) {
		case HORIZONTAL:
			return 0;
		case VERTICAL:
			return 1;
		case DIAGONAL_1:
			return 2;
		default:
			return 3;
		}
	}
}
