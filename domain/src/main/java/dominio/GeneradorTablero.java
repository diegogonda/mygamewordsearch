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
	 *            palabras de la partida, con su pista
	 * @return la partida con el tablero y las palabras colocadas
	 * @throws TableroException
	 *             si no se ha podido generar un tablero con esas palabras
	 */
	public Partida genera(ConfiguracionJuego configuracion, List<Palabra> palabras) throws TableroException {
		if (palabras == null) {
			throw new IllegalArgumentException("Las palabras de la partida no pueden ser null");
		}
		int tamano = configuracion.getTamanoTablero();
		ReglasDeTablero reglas = ReglasDeTablero.paraTamano(tamano);
		List<Palabra> ordenadas = ordenaPorLongitud(palabras);
		List<Orientacion> permitidas = configuracion.getModoOrientacion().orientacionesPermitidas();

		Partida mejor = null;
		int mejoresColocadas = -1;

		for (int intento = 0; intento < TABLEROS_MAXIMOS; intento++) {
			Tablero tablero = new Tablero(tamano);
			List<PalabraColocada> colocadas = new ArrayList<PalabraColocada>();

			int numero = colocaPalabras(ordenadas, permitidas, configuracion.isPalabrasInvertidas(), tablero, colocadas);

			boolean todasColocadas = numero == ordenadas.size();
			boolean numeroRazonable = reglas.esJugable(numero);
			if (todasColocadas || numeroRazonable) {
				completa(tablero);
				return creaPartida(tablero, colocadas);
			}

			/*
			 * El tablero no sirve, pero se guarda el que más palabras ha
			 * colocado por si no sale ninguno bueno: es mejor jugar con un
			 * tablero incompleto que dejar la aplicación colgada.
			 */
			if (numero > mejoresColocadas) {
				mejoresColocadas = numero;
				mejor = creaPartida(tablero, colocadas);
			}
		}

		if (mejor == null) {
			mejor = creaPartida(new Tablero(tamano), new ArrayList<PalabraColocada>());
		}
		completa(mejor.getTablero());

		throw new TableroException("No se ha podido generar un tablero de " + tamano + " con " + ordenadas.size() + " palabras", mejor);
	}

	/**
	 * Crea la partida a partir del tablero generado.
	 *
	 * El motor se encarga de colocar las palabras, así que la partida sale
	 * coherente. Si no lo fuera, es un fallo del motor y no del juego.
	 */
	private Partida creaPartida(Tablero tablero, List<PalabraColocada> palabras) throws TableroException {
		try {
			return Partida.crea(tablero, palabras);
		} catch (PartidaIncoherenteException e) {
			throw new TableroException("El motor ha generado un tablero incoherente: " + e.getMessage(), null, e);
		}
	}

	/**
	 * Coloca las palabras en el tablero. Si una palabra no cabe, se vuelve a
	 * intentarlo; si tampoco cabe después de varios intentos, se abandona el
	 * tablero y se devuelve cuántas palabras se han podido colocar.
	 */
	private int colocaPalabras(List<Palabra> palabras, List<Orientacion> permitidas, boolean invertidas, Tablero tablero, List<PalabraColocada> colocadas) {
		int intentos = 0;

		for (int i = 0; i < palabras.size(); i++) {
			Palabra palabra = palabras.get(i);
			String aColocar = palabra.getTextoEnTablero();
			boolean palabraInvertida = false;

			if (invertidas && aleatorio.nextBoolean()) {
				aColocar = palabra.getTextoEnTableroInvertido();
				palabraInvertida = true;
			}

			Orientacion orientacion = permitidas.get(aleatorio.nextInt(permitidas.size()));
			List<Celda> celdas = colocador.coloca(aColocar, orientacion, tablero);

			if (celdas == null) {
				intentos++;
				if (intentos >= INTENTOS_MAXIMOS) {
					return colocadas.size();
				}
				i--;
			} else {
				intentos = 0;
				/*
				 * Se guarda la palabra tal y como aparece en la lista de
				 * palabras. Si va del revés, lo indica la marca de invertida.
				 */
				colocadas.add(new PalabraColocada(palabra, orientacion, celdas, palabraInvertida));
			}
		}

		return colocadas.size();
	}

	/**
	 * Ordena las palabras de la más larga a la más corta, manteniendo el orden
	 * relativo de las que tienen la misma longitud.
	 */
	private List<Palabra> ordenaPorLongitud(List<Palabra> palabras) {
		List<Palabra> ordenadas = new ArrayList<Palabra>(palabras);
		Collections.sort(ordenadas, new Comparator<Palabra>() {
			public int compare(Palabra primera, Palabra segunda) {
				return segunda.getTexto().length() - primera.getTexto().length();
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
		return ReglasDeTablero.paraTamano(tamano).minimoPalabras();
	}

	/**
	 * Número de palabras máximo que se considera un tablero válido.
	 */
	public static int maximoPalabras(int tamano) {
		return ReglasDeTablero.paraTamano(tamano).maximoPalabras();
	}
}
