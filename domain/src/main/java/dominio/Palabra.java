package dominio;

/**
 * Una palabra de la partida: el texto que ve el jugador y la pista que le
 * ayuda a encontrarla.
 *
 * El texto del jugador es el de la lista, con sus tildes. El texto del
 * tablero es el mismo, sin tildes, porque el tablero sólo tiene las 26
 * letras del abecedario.
 */
public final class Palabra {

	private final String texto;
	private final Pista pista;

	public Palabra(String texto, Pista pista) {
		if (texto == null || texto.trim().isEmpty()) {
			throw new IllegalArgumentException("Una palabra no puede estar vacía");
		}
		if (pista == null) {
			throw new IllegalArgumentException("Una palabra necesita una pista");
		}
		this.texto = texto;
		this.pista = pista;
	}

	/**
	 * @return el texto de la palabra, tal y como lo ve el jugador
	 */
	public String getTexto() {
		return texto;
	}

	/**
	 * @return la pista que ayuda a encontrar la palabra
	 */
	public Pista getPista() {
		return pista;
	}

	/**
	 * @return el texto tal y como está escrito en el tablero
	 */
	public String getTextoEnTablero() {
		return sinTildes(texto);
	}

	/**
	 * @return el texto tal y como está escrito en el tablero cuando la
	 *         palabra va del revés
	 */
	public String getTextoEnTableroInvertido() {
		return invertida(getTextoEnTablero());
	}

	/**
	 * Sustituye las vocales acentuadas por su equivalente sin tilde, porque el
	 * tablero solo contiene las 26 letras del abecedario.
	 *
	 * La eñe se deja como está: es una letra del abecedario y no un acento, y
	 * el tablero debe mostrar la palabra tal y como aparece en la lista.
	 *
	 * @param texto
	 *            texto con o sin tildes
	 * @return el texto sin tildes
	 */
	public static String sinTildes(String texto) {
		String resultado = texto;
		resultado = resultado.replace('á', 'a').replace('à', 'a').replace('ä', 'a').replace('â', 'a');
		resultado = resultado.replace('é', 'e').replace('è', 'e').replace('ë', 'e').replace('ê', 'e');
		resultado = resultado.replace('í', 'i').replace('ì', 'i').replace('ï', 'i').replace('î', 'i');
		resultado = resultado.replace('ó', 'o').replace('ò', 'o').replace('ö', 'o').replace('ô', 'o');
		resultado = resultado.replace('ú', 'u').replace('ù', 'u').replace('ü', 'u').replace('û', 'u');
		return resultado;
	}

	/**
	 * Da la vuelta a un texto.
	 *
	 * @param texto
	 *            el texto a invertir
	 * @return el texto al revés
	 */
	public static String invertida(String texto) {
		return new StringBuilder(texto).reverse().toString();
	}
}
