package dominio;

/**
 * Error del dominio: no se ha podido generar un tablero con la configuración
 * y las palabras dadas.
 *
 * Puede llevar consigo la mejor partida parcial encontrada, para que la
 * aplicación pueda seguir jugando con ella en lugar de quedarse colgada.
 */
public class TableroException extends Exception {

	private static final long serialVersionUID = 1L;

	private final Partida partidaParcial;

	public TableroException(String mensaje) {
		this(mensaje, null, null);
	}

	public TableroException(String mensaje, Partida partidaParcial) {
		this(mensaje, partidaParcial, null);
	}

	public TableroException(String mensaje, Partida partidaParcial, Throwable causa) {
		super(mensaje, causa);
		this.partidaParcial = partidaParcial;
	}

	/**
	 * @return la mejor partida parcial encontrada, o null si no se ha podido
	 *         colocar ninguna palabra
	 */
	public Partida getPartidaParcial() {
		return partidaParcial;
	}
}
