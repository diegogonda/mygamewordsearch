package dominio;

/**
 * Una partida en la que una palabra no se puede leer en el tablero.
 *
 * Es un error de los datos, no un fallo del juego: un fichero de partida
 * guardado puede estar estropeado o no corresponder con su tablero.
 */
public class PartidaIncoherenteException extends Exception {

	private static final long serialVersionUID = 1L;

	public PartidaIncoherenteException(String mensaje) {
		super(mensaje);
	}
}
