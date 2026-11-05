package dominio;

/**
 * Utilidades sobre el texto de las palabras del tablero.
 */
public final class Palabras {

	private Palabras() {
	}

	/**
	 * Sustituye las vocales acentuadas por su equivalente sin tilde, porque el
	 * tablero solo contiene las 26 letras del abecedario.
	 *
	 * La eñe se deja como está: es una letra del abecedario y no un acento, y
	 * el tablero debe mostrar la palabra tal y como aparece en la lista.
	 *
	 * @param palabra
	 *            palabra con o sin tildes
	 * @return la palabra sin tildes
	 */
	public static String sinTildes(String palabra) {
		String resultado = palabra;
		resultado = resultado.replace('á', 'a').replace('à', 'a').replace('ä', 'a').replace('â', 'a');
		resultado = resultado.replace('é', 'e').replace('è', 'e').replace('ë', 'e').replace('ê', 'e');
		resultado = resultado.replace('í', 'i').replace('ì', 'i').replace('ï', 'i').replace('î', 'i');
		resultado = resultado.replace('ó', 'o').replace('ò', 'o').replace('ö', 'o').replace('ô', 'o');
		resultado = resultado.replace('ú', 'u').replace('ù', 'u').replace('ü', 'u').replace('û', 'u');
		return resultado;
	}

	/**
	 * Da la vuelta a una palabra.
	 *
	 * @param palabra
	 *            palabra a invertir
	 * @return la palabra al revés
	 */
	public static String invertida(String palabra) {
		return new StringBuilder(palabra).reverse().toString();
	}
}
