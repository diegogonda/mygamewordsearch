package modelo;

/**
 * Error del dominio: no se ha podido generar un tablero con la configuración
 * y las palabras dadas.
 *
 * Puede llevar consigo el mejor tablero parcial encontrado, para que la
 * aplicación pueda seguir jugando con él en lugar de quedarse colgada.
 */
public class TableroException extends Exception {

	private static final long serialVersionUID = 1L;

	private final TableroGenerado tableroParcial;

	public TableroException(String mensaje) {
		this(mensaje, null);
	}

	public TableroException(String mensaje, TableroGenerado tableroParcial) {
		super(mensaje);
		this.tableroParcial = tableroParcial;
	}

	/**
	 * @return el mejor tablero parcial encontrado, o null si no se ha podido
	 *         colocar ninguna palabra
	 */
	public TableroGenerado getTableroParcial() {
		return tableroParcial;
	}
}
