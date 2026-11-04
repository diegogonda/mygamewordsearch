package modelo;

/**
 * Error del dominio: no se ha podido generar un tablero con la configuración
 * y las palabras dadas.
 */
public class TableroException extends Exception {

	private static final long serialVersionUID = 1L;

	public TableroException(String mensaje) {
		super(mensaje);
	}
}
