package objetos;

public class Rutas {
	private static String carpetaMyGameWordSearch = "/MyGameWordSearch/";
	private static String rutaArchivos = TarjetaSD.getRutaSD() + carpetaMyGameWordSearch;
	private static String imagenes = "/imagenes/";
	private static String nombrePaquete = "es.uvigo.gti.mygamewordsearch";
	private static String dataData = "/data/data/";
	private static String rutaArchivoPreferencias = dataData + nombrePaquete + "/shared_prefs/";
	private static String archivoSharedPreferences = "preferencias.xml";
	private static String configuracionNiveles = "niveles.xml";
	private static String posicionesPalabras = "posiciones_palabras.xml";
	private static String partidaJuego = "partida_en_juego.xml";
	private static String categoriaPersonalizada = "categorias_personalizadas.xml";
	private static String palabrasPersonalizada = "palabras_personalizadas.xml";
	private static String partidaActual = "tipo_partida.xml";
	private static String jpg = ".jpg";
	private static String configuracionPartida = "/configuracion_partida.xml";
	private static String palabrasXml = "/palabras.xml";
	private static String nivel = "/Nivel ";
	private static String opcionesJuego = "/opciones_juego.xml";

	public Rutas() {

	}

	/**
	 * @return the nivel
	 */
	public static String getNivel() {
		return nivel;
	}

	/**
	 * @return the rutaArchivos
	 */
	public static String getRutaArchivos() {
		return rutaArchivos;
	}

	/**
	 * Devuelve la ruta de un archivo imagen guardado en la memoria SD. Sólo válido para palabras en juego, no para las imágenes de las categorías.
	 * La ruta genérica será:
	 * "/sdcar/MyGameSoupLetter/categoria/Nivel x/imagenes/palabra.jpg"
	 * 
	 * @return the imagenes
	 */
	public static String getImagenesPalabras(String categoria, String palabra) {
		return rutaArchivos + categoria + imagenes + palabra + jpg;
	}

	/**
	 * @return the nombrePaquete
	 */
	public static String getNombrePaquete() {
		return nombrePaquete;
	}

	public static String getRutaArchivoPreferencias() {
		return rutaArchivoPreferencias;
	}

	/**
	 * @return the imagenes
	 */
	public static String getImagenes() {
		return imagenes;
	}

	/**
	 * @return the archivoSharedPreferences
	 */
	public static String getArchivoSharedPreferences() {
		return archivoSharedPreferences;
	}

	/**
	 * @return the configuracionNiveles
	 */
	public static String getConfiguracionNiveles() {
		return configuracionNiveles;
	}

	/**
	 * @return the posicionesPalabras
	 */
	public static String getPosicionesPalabras() {
		return posicionesPalabras;
	}

	/**
	 * @return the partidaJuego
	 */
	public static String getPartidaJuego() {
		return partidaJuego;
	}

	/**
	 * @return the categoriaPersonalizada
	 */
	public static String getCategoriaPersonalizada() {
		return categoriaPersonalizada;
	}

	/**
	 * @return the palabrasPersonalizada
	 */
	public static String getPalabrasPersonalizada() {
		return palabrasPersonalizada;
	}

	/**
	 * @return the partidaactual
	 */
	public static String getPartidaactual() {
		return partidaActual;
	}

	/**
	 * @return the jpg
	 */
	public static String getJpg() {
		return jpg;
	}

	public static String getConfiguracionPartida() {
		return configuracionPartida;
	}

	/**
	 * @return Devuelve el nombre del fichero /palabras.xml
	 */
	public static String getPalabrasXml() {
		return palabrasXml;
	}

	/**
	 * Obtener el nombre del fichero /opciones_juego.xml
	 * 
	 * @return the opcionesJuego
	 */
	public static String getOpcionesJuego() {
		return opcionesJuego;
	}

	/**
	 * @param categoria
	 *            de juego de la cual queremos obtener el archivo opciones_juego.xml
	 * @return ruta completa al archivo opciones_juego.xml de una categoria determinada
	 */
	public static String rutaOpcionesJuego(String categoria) {
		return rutaArchivos + categoria + opcionesJuego;
	}

	/**
	 * @return the carpetaMyGameWordSearch
	 */
	public static String getCarpetaMyGameWordSearch() {
		return carpetaMyGameWordSearch;
	}
	
}
