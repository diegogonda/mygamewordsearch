package dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Genera el tablero de una partida.
 *
 * Coloca las palabras de la más larga a la más corta, porque las palabras
 * largas son las que más restringen las posiciones disponibles. Si no se
 * consigue un tablero con un número de palabras razonable, se vuelve a
 * intentarlo desde cero.
 *
 * Es lógica pura: no depende de Android ni de ningún estado compartido.
 */
public class GeneradorTablero {

	/**
	 * Intentos seguidos de colocar una palabra antes de abandonar el tablero.
	 */
	public static final int INTENTOS_MAXIMOS = 100;

	/**
	 * Tableros distintos que se generan como máximo antes de rendirse.
	 */
	public static final int TABLEROS_MAXIMOS = 50;

	private final ColocadorPalabras colocador;
	private final Random aleatorio;

	public GeneradorTablero() {
		this(new ColocadorPalabras(), new Random());
	}

	public GeneradorTablero(ColocadorPalabras colocador, Random aleatorio) {
		this.colocador = colocador;
		this.aleatorio = aleatorio;
	}

	/**
	 * Genera el tablero de una partida.
	 *
	 * @param configuracion
	 *            tamaño del tablero, orientaciones permitidas y si las palabras
	 *            van del revés
	 * @param palabras
	 *            palabras de la partida
	 * @return el tablero con las palabras colocadas
	 * @throws TableroException
	 *             si no se ha podido generar un tablero con esas palabras
	 */
	public TableroGenerado genera(ConfiguracionJuego configuracion, List<String> palabras) throws TableroException {
		int tamano = configuracion.getTamanoTablero();
		List<String> ordenadas = ordenaPorLongitud(palabras);
		List<Orientacion> permitidas = configuracion.getModoOrientacion().orientacionesPermitidas();

		TableroGenerado mejor = null;
		int mejoresColocadas = -1;

		for (int intento = 0; intento < TABLEROS_MAXIMOS; intento++) {
			Tablero tablero = new Tablero(tamano);
			List<PosicionPalabra> posiciones = new ArrayList<PosicionPalabra>();

			int colocadas = colocaPalabras(ordenadas, permitidas, configuracion.isPalabrasInvertidas(), tablero, posiciones);

			boolean todasColocadas = colocadas == ordenadas.size();
			boolean numeroRazonable = colocadas >= minimoPalabras(tamano) && colocadas <= maximoPalabras(tamano);
			if (todasColocadas || numeroRazonable) {
				completa(tablero);
				return new TableroGenerado(tablero, posiciones);
			}

			/*
			 * El tablero no sirve, pero se guarda el que más palabras ha
			 * colocado por si no sale ninguno bueno: es mejor jugar con un
			 * tablero incompleto que dejar la aplicación colgada.
			 */
			if (colocadas > mejoresColocadas) {
				mejoresColocadas = colocadas;
				mejor = new TableroGenerado(tablero, posiciones);
			}
		}

		if (mejor == null) {
			mejor = new TableroGenerado(new Tablero(tamano), new ArrayList<PosicionPalabra>());
		}
		completa(mejor.getTablero());

		throw new TableroException("No se ha podido generar un tablero de " + tamano + " con " + ordenadas.size() + " palabras", mejor);
	}

	/**
	 * Coloca las palabras en el tablero. Si una palabra no cabe, se vuelve a
	 * intentarlo; si tampoco cabe después de varios intentos, se abandona el
	 * tablero y se devuelve cuántas palabras se han podido colocar.
	 */
	private int colocaPalabras(List<String> palabras, List<Orientacion> permitidas, boolean invertidas, Tablero tablero, List<PosicionPalabra> posiciones) {
		int intentos = 0;

		for (int i = 0; i < palabras.size(); i++) {
			String palabra = palabras.get(i);
			/*
			 * El tablero sólo tiene las 26 letras del abecedario, así que la
			 * palabra se escribe sin tildes. La palabra que se informa es la
			 * original, con sus tildes.
			 */
			String aColocar = Palabras.sinTildes(palabra);
			boolean palabraInvertida = false;

			if (invertidas && aleatorio.nextBoolean()) {
				aColocar = Palabras.invertida(aColocar);
				palabraInvertida = true;
			}

			Orientacion orientacion = permitidas.get(aleatorio.nextInt(permitidas.size()));
			List<Celda> celdas = colocador.coloca(aColocar, orientacion, tablero);

			if (celdas == null) {
				intentos++;
				if (intentos >= INTENTOS_MAXIMOS) {
					return posiciones.size();
				}
				i--;
			} else {
				intentos = 0;
				/*
				 * Se guarda la palabra tal y como aparece en la lista de
				 * palabras. Si va del revés, lo indica la marca de invertida.
				 */
				posiciones.add(new PosicionPalabra(palabra, orientacion, celdas, palabraInvertida));
			}
		}

		return posiciones.size();
	}

	/**
	 * Ordena las palabras de la más larga a la más corta, manteniendo el orden
	 * relativo de las que tienen la misma longitud.
	 */
	private List<String> ordenaPorLongitud(List<String> palabras) {
		List<String> ordenadas = new ArrayList<String>(palabras);
		Collections.sort(ordenadas, new Comparator<String>() {
			public int compare(String primera, String segunda) {
				return segunda.length() - primera.length();
			}
		});
		return ordenadas;
	}

	/**
	 * Rellena las celdas que han quedado vacías con letras al azar.
	 */
	private void completa(Tablero tablero) {
		for (int y = 0; y < tablero.getTamano(); y++) {
			for (int x = 0; x < tablero.getTamano(); x++) {
				if (tablero.getCelda(x, y) == Tablero.LETRA_RELLENO) {
					tablero.setCelda(x, y, (char) (aleatorio.nextInt(26) + 'a'));
				}
			}
		}
	}

	/**
	 * Número de palabras mínimo que se considera un tablero válido.
	 */
	public static int minimoPalabras(int tamano) {
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
	 * Número de palabras máximo que se considera un tablero válido.
	 */
	public static int maximoPalabras(int tamano) {
		switch (tamano) {
		case 4:
			return 4;
		case 5:
			return 5;
		case 6:
			return 6;
		case 7:
			return 7;
		case 8:
			return 8;
		case 9:
			return 9;
		case 10:
			return 11;
		case 11:
			return 12;
		case 12:
			return 14;
		default:
			return 4;
		}
	}
}
