package dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Una partida en juego: el tablero y las palabras que hay que encontrar.
 *
 * Una partida es coherente si todas sus palabras se leen en el tablero. No se
 * puede crear una partida que no lo sea: el constructor es privado y la
 * fábrica comprueba la coherencia. Así, quien tenga una partida delante sabe
 * que se puede jugar.
 */
public final class Partida {

	private final Tablero tablero;
	private final List<PalabraColocada> palabras;

	private Partida(Tablero tablero, List<PalabraColocada> palabras) {
		this.tablero = tablero;
		this.palabras = Collections.unmodifiableList(new ArrayList<PalabraColocada>(palabras));
	}

	/**
	 * Crea una partida, comprobando que todas las palabras se leen en el
	 * tablero.
	 *
	 * @param tablero
	 *            el tablero de la partida
	 * @param palabras
	 *            las palabras colocadas en él
	 * @return la partida
	 * @throws PartidaIncoherenteException
	 *             si alguna palabra no se puede leer en el tablero
	 */
	public static Partida crea(Tablero tablero, List<PalabraColocada> palabras) throws PartidaIncoherenteException {
		for (PalabraColocada palabra : palabras) {
			compruebaDireccion(palabra);
			String leido = leer(tablero, palabra);
			String esperado = palabra.isInvertida() ? palabra.getPalabra().getTextoEnTableroInvertido() : palabra.getPalabra().getTextoEnTablero();
			if (!esperado.equals(leido)) {
				throw new PartidaIncoherenteException("La palabra '" + palabra.getTexto() + "' no se puede leer en el tablero: se lee '" + leido + "'");
			}
		}
		return new Partida(tablero, palabras);
	}

	/**
	 * Comprueba que las celdas de la palabra siguen la orientación en la que
	 * dice estar.
	 */
	private static void compruebaDireccion(PalabraColocada palabra) throws PartidaIncoherenteException {
		List<Celda> celdas = palabra.getCeldas();
		for (int i = 1; i < celdas.size(); i++) {
			Celda anterior = celdas.get(i - 1);
			Celda actual = celdas.get(i);
			int esperadoX = anterior.getX() + palabra.getOrientacion().getPasoX();
			int esperadoY = anterior.getY() + palabra.getOrientacion().getPasoY();
			if (actual.getX() != esperadoX || actual.getY() != esperadoY) {
				throw new PartidaIncoherenteException("La palabra '" + palabra.getTexto() + "' no está en la orientación que dice: " + palabra.getOrientacion());
			}
		}
	}

	/**
	 * Crea una partida sin palabras, con el tablero dado.
	 *
	 * @param tablero
	 *            el tablero de la partida
	 * @return la partida
	 */
	public static Partida vacia(Tablero tablero) {
		return new Partida(tablero, new ArrayList<PalabraColocada>());
	}

	/**
	 * Lee una palabra del tablero, siguiendo sus celdas.
	 */
	private static String leer(Tablero tablero, PalabraColocada palabra) {
		StringBuilder texto = new StringBuilder();
		for (Celda celda : palabra.getCeldas()) {
			if (celda.getX() < 0 || celda.getX() >= tablero.getTamano() || celda.getY() < 0 || celda.getY() >= tablero.getTamano()) {
				return "";
			}
			texto.append(tablero.getCelda(celda.getX(), celda.getY()));
		}
		return texto.toString();
	}

	/**
	 * @return el tablero de la partida
	 */
	public Tablero getTablero() {
		return tablero;
	}

	/**
	 * @return las palabras que hay que encontrar, con su pista y su posición
	 */
	public List<PalabraColocada> getPalabras() {
		return palabras;
	}

	/**
	 * @return cuántas palabras hay que encontrar
	 */
	public int getCantidadPalabras() {
		return palabras.size();
	}

	/**
	 * Busca la palabra que el jugador ha encontrado al arrastrar de una celda
	 * a otra.
	 *
	 * Da igual la dirección del arrastre: se puede empezar por cualquiera de
	 * los dos extremos. Sólo cuenta si las dos celdas son los extremos de la
	 * palabra, porque una palabra que va del revés tiene su primera letra en
	 * el final.
	 *
	 * @param inicio
	 *            la celda donde empezó el arrastre
	 * @param fin
	 *            la celda donde terminó el arrastre
	 * @return la palabra encontrada, o vacío si el arrastre no ha encontrado
	 *         ninguna
	 */
	public Optional<PalabraColocada> buscaPalabra(Celda inicio, Celda fin) {
		for (PalabraColocada palabra : palabras) {
			List<Celda> celdas = palabra.getCeldas();
			if (celdas.size() < 2) {
				continue;
			}
			Celda primero = celdas.get(0);
			Celda ultimo = celdas.get(celdas.size() - 1);
			boolean mismoSentido = primero.equals(inicio) && ultimo.equals(fin);
			boolean sentidoInverso = primero.equals(fin) && ultimo.equals(inicio);
			if (mismoSentido || sentidoInverso) {
				return Optional.of(palabra);
			}
		}
		return Optional.empty();
	}
}
