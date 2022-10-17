package listViewPersonalizado;

import android.graphics.drawable.Drawable;

public class ConfiguracionPartida {

	protected long id;
	protected String tipoPartida;
	protected Drawable foto;
	protected String categoria;
	protected String tamaño;
	protected String orientacion;
	protected String invertida;
	protected String tipoPista;

	public ConfiguracionPartida() {
		super();
	}
	
	/**
	 * Devuelve el tipo de pista utilizado en el tablero
	 * @return the tipoPista
	 */
	public String getTipoPista() {
		return tipoPista;
	}

	/**
	 * Inserta el tipo de pista utilizado en el tablero
	 * @param tipoPista the tipoPista to set
	 */
	public void setTipoPista(String tipoPista) {
		this.tipoPista = tipoPista;
	}

	/**
	 * Devuelve el tipo de partida (Gama o Rápida) utilizada en el tablero
	 * @return the tipo
	 */
	public String getTipoPartida() {
		return tipoPartida;
	}

	/**
	 * Inserta el tipo de partida (Gama o Rápida) utilizada en el tablero
	 * @param tipo the tipo to set
	 */
	public void setTipoPartida(String tipoPartida) {
		this.tipoPartida = tipoPartida;
	}

	/**
	 * Obtener el identificador de la partida
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Inserta identificador a la partida
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Obtener la imagen del juego
	 * @return the foto
	 */
	public Drawable getFoto() {
		return foto;
	}

	/**
	 * Incluir una imagen para el juego
	 * @param foto the foto to set
	 */
	public void setFoto(Drawable foto) {
		this.foto = foto;
	}

	/**
	 * Obtener la categoria de juego
	 * @return the categoria
	 */
	public String getCategoria() {
		return categoria;
	}

	/**
	 * Asignar una categoria al juego
	 * @param categoria the categoria to set
	 */
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	/**
	 * Obtener el tamaño del tablero en juego
	 * @return the tamaño
	 */
	public String getTamaño() {
		return tamaño;
	}

	/**
	 * Insertar el tamaño del tablero en juego
	 * @param tamaño the tamaño to set
	 */
	public void setTamaño(String tamaño) {
		this.tamaño = tamaño;
	}

	/**
	 * Obtener la orientación de las palabras en el tablero de juego
	 * @return the orientacion
	 */
	public String getOrientacion() {
		return orientacion;
	}

	/**
	 * Insertar la orientación de las palabras en el tablero de juego
	 * @param orientacion the orientacion to set
	 */
	public void setOrientacion(String orientacion) {
		this.orientacion = orientacion;
	}

	/**
	 * Conocer si las palabras pueden o no estar invertidas
	 * @return the invertida
	 */
	public String getInvertida() {
		return invertida;
	}

	/**
	 * Informar si las palabras pueden o no estar invertidas
	 * @param invertida the invertida to set
	 */
	public void setInvertida(String invertida) {
		this.invertida = invertida;
	}
}
