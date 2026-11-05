package dominio;

/**
 * De qué tipo es la pista que ayuda a encontrar una palabra.
 *
 * Los valores son los que usan los ficheros de contenido de los niveles. Antes
 * de que existiera este enum, el tipo de pista era un {@code String} que se
 * comparaba a mano en seis sitios distintos de la aplicación.
 */
public enum TipoPista {

	/** La pista es el nombre de la palabra. */
	NOMBRE("nombre"),

	/** La pista es una descripción de la palabra. */
	DESCRIPCION("pista_descripcion"),

	/** La pista es una imagen. */
	IMAGEN("pista_imagen"),

	/** La pista es un audio. */
	AUDIO("pista_audio"),

	/** La pista es una marca en el tablero, sobre la primera letra. */
	MARCA("pista_marca"),

	/** La pista es la categoría a la que pertenece la palabra. */
	CATEGORIA("pista_categoria"),

	/** No hay pista: el jugador sólo ve la palabra. */
	SIN_PISTA("sin_pista");

	private final String valor;

	TipoPista(String valor) {
		this.valor = valor;
	}

	/**
	 * @return el valor con el que este tipo aparece en los ficheros de
	 *         contenido
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * Traduce el valor de un fichero de contenido al tipo de pista
	 * correspondiente.
	 *
	 * Un valor que no se conoce no es un error: la partida sigue siendo
	 * jugable, simplemente sin pista.
	 *
	 * @param valor
	 *            el valor del fichero de contenido
	 * @return el tipo de pista
	 */
	public static TipoPista desdeValor(String valor) {
		if (valor == null) {
			return SIN_PISTA;
		}
		String limpio = valor.trim();
		for (TipoPista tipo : values()) {
			if (tipo.valor.equals(limpio)) {
				return tipo;
			}
		}
		return SIN_PISTA;
	}
}
