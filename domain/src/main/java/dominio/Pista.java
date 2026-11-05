package dominio;

/**
 * La pista que ayuda al jugador a encontrar una palabra.
 *
 * Antes de que existiera este tipo, cada palabra llevaba cuatro campos
 * anulables (imagen, audio, categoría y descripción) y el tipo de pista era un
 * {@code String}. Ahora el tipo dice qué significa el valor, y los tipos que
 * no llevan valor no lo llevan.
 */
public final class Pista {

	private final TipoPista tipo;
	private final String valor;

	private Pista(TipoPista tipo, String valor) {
		this.tipo = tipo;
		this.valor = valor;
	}

	/**
	 * Crea la pista de un tipo cualquiera.
	 *
	 * Los tipos que no llevan valor (la marca y la ausencia de pista) se
	 * quedan sin él, aunque se les pase uno.
	 *
	 * @param tipo
	 *            el tipo de la pista
	 * @param valor
	 *            el valor de la pista, que puede estar vacío
	 * @return la pista
	 */
	public static Pista de(TipoPista tipo, String valor) {
		if (tipo == TipoPista.MARCA || tipo == TipoPista.SIN_PISTA) {
			return new Pista(tipo, "");
		}
		return new Pista(tipo, valor == null ? "" : valor);
	}

	/**
	 * @param palabra
	 *            el nombre de la palabra
	 * @return la pista que es el nombre de la palabra
	 */
	public static Pista nombre(String palabra) {
		return de(TipoPista.NOMBRE, palabra);
	}

	/**
	 * @param texto
	 *            la descripción de la palabra
	 * @return la pista que es una descripción
	 */
	public static Pista descripcion(String texto) {
		return de(TipoPista.DESCRIPCION, texto);
	}

	/**
	 * @param ruta
	 *            el nombre del fichero de la imagen
	 * @return la pista que es una imagen
	 */
	public static Pista imagen(String ruta) {
		return de(TipoPista.IMAGEN, ruta);
	}

	/**
	 * @param nombre
	 *            el nombre del fichero del audio
	 * @return la pista que es un audio
	 */
	public static Pista audio(String nombre) {
		return de(TipoPista.AUDIO, nombre);
	}

	/**
	 * @param categoria
	 *            la categoría a la que pertenece la palabra
	 * @return la pista que es la categoría
	 */
	public static Pista categoria(String categoria) {
		return de(TipoPista.CATEGORIA, categoria);
	}

	/**
	 * @return la pista que es una marca sobre la primera letra
	 */
	public static Pista marca() {
		return de(TipoPista.MARCA, "");
	}

	/**
	 * @return la ausencia de pista
	 */
	public static Pista sinPista() {
		return de(TipoPista.SIN_PISTA, "");
	}

	public TipoPista getTipo() {
		return tipo;
	}

	/**
	 * @return el valor de la pista, que está vacío en los tipos que no lo
	 *         tienen
	 */
	public String getValor() {
		return valor;
	}
}
