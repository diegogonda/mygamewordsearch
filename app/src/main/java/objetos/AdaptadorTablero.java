package objetos;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dominio.Celda;
import dominio.Orientacion;
import dominio.PalabraColocada;
import dominio.Partida;

/**
 * Traduce la partida del dominio a los datos que necesita la presentación: las
 * letras del tablero y la posición de cada palabra.
 *
 * No depende de Android, así que se puede probar en la JVM.
 */
public final class AdaptadorTablero {

	private AdaptadorTablero() {
	}

	/**
	 * Resultado de adaptar una partida: las letras del tablero y las palabras
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
	 * Aplica las posiciones de una partida sobre la lista de palabras de la
	 * partida en curso.
	 *
	 * Las palabras que el motor no ha podido colocar se quedan fuera del
	 * resultado. Las que sí quedan conservan el orden de la lista original, y
	 * si una palabra está repetida cada copia va a su propio sitio.
	 *
	 * @param partida
	 *            la partida generada por el motor
	 * @param palabras
	 *            palabras de la partida en curso
	 * @return las letras del tablero y las palabras colocadas
	 */
	public static TableroPreparado adapta(Partida partida, List<PosicionesPalabras> palabras) {
		Map<String, Deque<PosicionesPalabras>> porPalabra = indexa(palabras);

		for (PalabraColocada colocada : partida.getPalabras()) {
			Deque<PosicionesPalabras> candidatas = porPalabra.get(colocada.getTexto());
			if (candidatas == null || candidatas.isEmpty()) {
				continue;
			}
			coloca(candidatas.remove(), colocada);
		}

		List<PosicionesPalabras> colocadas = new ArrayList<PosicionesPalabras>();
		for (PosicionesPalabras palabra : palabras) {
			if (palabra.getMostradaPantalla()) {
				colocadas.add(palabra);
			}
		}

		return new TableroPreparado(partida.getTablero().getCeldas(), colocadas);
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
	private static void coloca(PosicionesPalabras destino, PalabraColocada colocada) {
		List<Celda> celdas = colocada.getCeldas();
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
		destino.setOrientacion(codigoOrientacion(colocada.getOrientacion()));
		destino.setInvertida(colocada.isInvertida());
		destino.setElegida(false);
		destino.setMostradaPantalla(true);
		copiaPista(destino, colocada);
	}

	/**
	 * Copia la pista de la palabra al formato que guarda el fichero de la
	 * partida. Los tipos de pista que no tienen valor no tocan nada, para no
	 * perder lo que ya había.
	 */
	private static void copiaPista(PosicionesPalabras destino, PalabraColocada colocada) {
		String valor = colocada.getPista().getValor();
		if (valor.isEmpty()) {
			return;
		}
		switch (colocada.getPista().getTipo()) {
		case DESCRIPCION:
			destino.setPistaDescripcion(valor);
			break;
		case IMAGEN:
			destino.setPistaImagen(valor);
			break;
		case AUDIO:
			destino.setPistaAudio(valor);
			break;
		case CATEGORIA:
			destino.setPistaCategoria(valor);
			break;
		default:
			break;
		}
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
