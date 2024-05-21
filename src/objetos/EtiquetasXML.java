package objetos;

public class EtiquetasXML {
	/*************************************************************************************************************************************************************************************************/
	/* VARIABLES DE LAS ETIQUETAS XML */
	/*************************************************************************************************************************************************************************************************/
	/**
	 * Etiquetas de la configuracion de la partida
	 * */
	private static String NODO_TEMA = "tema";
	private static String NODO_PALABRA = "palabra";
	private static String NODO_NOMBRE = "nombre";
	private static String NODO_PISTA_AUDIO = "pista_audio";
	private static String NODO_PISTA_IMAGEN = "pista_imagen";
	private static String NODO_PISTA_CATEGORIA = "pista_categoria";
	private static String NODO_PISTA_DESCRIPCION = "pista_descripcion";
	private static String NODO_PISTA_MARCA ="pista_marca";
	private static String NODO_PISTA_SIN_PISTA ="sin_pista";
	private static String NODO_CATEGORIA = "categoria";
	private static String NODO_CATEGORIAS = "categorias";
	/**
	 * NODO PARA GENERAR EL ARCHIVO POSICIONESPALABRAS
	 */
	private static String NODO_PALABRAS = "palabras";
	private static String NODO_ID = "id";
	private static String NODO_ELEGIDA = "elegida";
	private static String NODO_MOSTRADA_PANTALLA = "mostrada_pantalla";
	private static String NODO_INVERTIDA = "palabras_invertidas";
	private static String NODO_ORIENTACION = "orientacion";
	private static String NODO_X_PIXEL_INICIAL = "x_pixel_inicial";
	private static String NODO_Y_PIXEL_INICIAL = "y_pixel_inicial";
	private static String NODO_X_PIXEL_FINAL = "x_pixel_final";
	private static String NODO_Y_PIXEL_FINAL = "y_pixel_final";
	private static String NODO_X_TABLERO_INICIAL = "x_tablero_inicial";
	private static String NODO_Y_TABLERO_INICIAL = "y_tablero_inicial";
	private static String NODO_X_TABLERO_FINAL = "x_tablero_final";
	private static String NODO_Y_TABLERO_FINAL = "y_tablero_final";
	private static String NODO_COLOR = "color";
	private static String NODO_TAMANO_TABLERO = "tamano_tablero";
	private static String NODO_ORIENTACION_PALABRAS = "orientacion_palabras";
	/**
	 * Variables del archivo partidas_definidas.xml
	 * 
	 * @param context
	 */
	private static String NODO_TIPO = "tipo";
	private static String NODO_TIPO_PISTA = "tipo_pista";
	private static String NODO_PARTIDAS_DEFINIDAS = "partidas_definidas";
	/**
	 * NODOS PARA GENERAR EL ARCHIVO DE TABLERO EN JUEGO
	 */
	private static String NODO_FILA = "fila";
	private static String NODO_TABLERO = "tablero";
	private static String NODO_LETRA = "letra";
	private static String NODO_NUMERO_FICHERO = "numero_fichero";
	private static int cantidadPalabras;
	private static boolean primeraIteraccion = true;
	private static String rutaSharedPreferences = Rutas.getRutaArchivoPreferencias() + "_preferencias";
	private static String xmlTexto = ".xml";
	private static String copia = "copia";
	/**
	 * Variables del archivos gamas.xml
	 * 
	 * @param contexto
	 */
	private static String NODO_GAMAS = "gamas";
	private static String NODO_NIVEL = "nivel";
	// private static Niveles[] niveles;
	/**
	 * Variables del archivo partida_actual.xml
	 * 
	 * @param contexto
	 */
	private static String NODO_PARTIDA_ACTUAL = "partida_actual";
	private static String NODO_PARTIDA = "partida";
	private static String partidaActual = "tipo_partida.xml";
	private static String temaActual;
	private static int tamanoTablero;
	private static String NODO_TAMANO = "tamano";
	private static int cantidadCategorias;
	/*************************************************************************************************************************************************************************************************/
	/* VARIABLES DE LAS ATRIBUTOS XML */
	/*************************************************************************************************************************************************************************************************/
	private static String ATRIBUTO_FILA = "fila";
	private static String ATRIBUTO_COLUMNA = "columna";
	/*************************************************************************************************************************************************************************************************/
	/* VARIABLES DE LOS CONTENIDOS XML */
	/*************************************************************************************************************************************************************************************************/
	private static String no = "no";
	private static String si = "si";
	private static String xml = "xml";
	private static String NODO_STRING = "string";
	private static String NODO_BOOLEAN = "boolean";
	private static String NODO_MAP = "map";
	private static String tipoTablero;
	/*************************************************************************************************************************************************************************************************/
	/* VARIABLES ISO IDIOMAS */
	/*************************************************************************************************************************************************************************************************/
	private static String ISOEspanol = "es";
	private static String ISOIngles = "en";
	private static String ISOGallego = "gl";
	private static String ISOAleman = "de";
	private static String ISOItaliano = "it";
	private static String ISOFrances = "fr";
	private static String idioma = "idioma";
	/*************************************************************************************************************************************************************************************************/
	/* VARIABLES TIPO DE PARTIDA */
	/*************************************************************************************************************************************************************************************************/
	private static String partidaRapida = "rapida";
	private static String gama = "Gama";
	private static String NODO_LETRAS_RELLENO = "letras_relleno";
	private static String NODO_TIPO_TABLERO = "tipo_tablero";
	private static String NODO_TIPO_REFORZADOR_VISUAL = "imagen";
	private static String reforzadorColor = "color";
	private static String reforzadorSinReforzador = "sin_reforzador";
	private static String reforzadorBlancoNegro = "blanco_negro";
	private static String reforzadorAltoContraste = "alto_contraste";
	private static String reforzadorSonido = "sonidos_activados";
	private static String crearCarpetasInstalacion = "crear_carpetas";
	/*************************************************************************************************************************************************************************************************/
	/* VARIABLES ARCHIVO opciones_juego.xml */
	/*************************************************************************************************************************************************************************************************/
	private static String opcionesJuego = "opciones_juego";
	private static String numeroNiveles = "numero_niveles";
	private static String gamificacion = "gamificacion";
	private static String ultimoNivelSuperado = "ultimo_nivel_superado";
	/*************************************************************************************************************************************************************************************************/
	/* VARIABLES TIPO BUNDLE PARA PASAR ENTRE ACTIVIDADES */
	/*************************************************************************************************************************************************************************************************/
	private static String agregarGama = "agregar_gama";
	private static String nombrePartida = "nombre_partida";
	private static String cantNivelesGama = "niveles_totales";
	private static String nivelEnJuego = "nivel_juego";
	/*************************************************************************************************************************************************************************************************/
	/*
	 * ETIQUETAS DE LOS RESOURCES
	 * /************************************************************************************************************************************************************************************************
	 */
	private static String recursoDrawable = "drawable";
	private static String imagenCorrecto = "correcto";

	public EtiquetasXML() {
	}

	/**
	 * @return the nodoTema
	 */
	public static String getNodoTema() {
		return NODO_TEMA;
	}

	/**
	 * @return the nodoPalabra
	 */
	public static String getNodoPalabra() {
		return NODO_PALABRA;
	}

	/**
	 * @return the nodoNombre
	 */
	public static String getNodoNombre() {
		return NODO_NOMBRE;
	}

	/**
	 * @return the nodoPistaAudio
	 */
	public static String getNodoPistaAudio() {
		return NODO_PISTA_AUDIO;
	}

	/**
	 * @return the nodoPistaImagen
	 */
	public static String getNodoPistaImagen() {
		return NODO_PISTA_IMAGEN;
	}

	/**
	 * @return the nODO_PISTA_MARCA
	 */
	public static String getNodoPistaMarca() {
		return NODO_PISTA_MARCA;
	}

	/**
	 * @return the nODO_PISTA_SIN_PISTA
	 */
	public static String getNodoPistaSinPista() {
		return NODO_PISTA_SIN_PISTA;
	}

	/**
	 * @return the nodoPistaCategoria
	 */
	public static String getNodoPistaCategoria() {
		return NODO_PISTA_CATEGORIA;
	}

	/**
	 * @return the nodoPistaDescripcion
	 */
	public static String getNodoPistaDescripcion() {
		return NODO_PISTA_DESCRIPCION;
	}

	/**
	 * @return the nodoCategoria
	 */
	public static String getNodoCategoria() {
		return NODO_CATEGORIA;
	}

	/**
	 * @return the nodoCategorias
	 */
	public static String getNodoCategorias() {
		return NODO_CATEGORIAS;
	}

	/**
	 * @return the nodoPalabras
	 */
	public static String getNodoPalabras() {
		return NODO_PALABRAS;
	}

	/**
	 * @return the nodoId
	 */
	public static String getNodoId() {
		return NODO_ID;
	}

	/**
	 * @return the nodoElegida
	 */
	public static String getNodoElegida() {
		return NODO_ELEGIDA;
	}

	/**
	 * @return the nodoMostradaPantalla
	 */
	public static String getNodoMostradaPantalla() {
		return NODO_MOSTRADA_PANTALLA;
	}

	/**
	 * @return the nodoInvertida
	 */
	public static String getNodoInvertida() {
		return NODO_INVERTIDA;
	}

	/**
	 * @return the nodoOrientacion
	 */
	public static String getNodoOrientacion() {
		return NODO_ORIENTACION;
	}

	/**
	 * @return the nodoXPixelInicial
	 */
	public static String getNodoXPixelInicial() {
		return NODO_X_PIXEL_INICIAL;
	}

	/**
	 * @return the nodoYPixelInicial
	 */
	public static String getNodoYPixelInicial() {
		return NODO_Y_PIXEL_INICIAL;
	}

	/**
	 * @return the nodoXPixelFinal
	 */
	public static String getNodoXPixelFinal() {
		return NODO_X_PIXEL_FINAL;
	}

	/**
	 * @return the nodoYPixelFinal
	 */
	public static String getNodoYPixelFinal() {
		return NODO_Y_PIXEL_FINAL;
	}

	/**
	 * @return the nodoXTableroInicial
	 */
	public static String getNodoXTableroInicial() {
		return NODO_X_TABLERO_INICIAL;
	}

	/**
	 * @return the nodoYTableroInicial
	 */
	public static String getNodoYTableroInicial() {
		return NODO_Y_TABLERO_INICIAL;
	}

	/**
	 * @return the nodoXTableroFinal
	 */
	public static String getNodoXTableroFinal() {
		return NODO_X_TABLERO_FINAL;
	}

	/**
	 * @return the nodoYTableroFinal
	 */
	public static String getNodoYTableroFinal() {
		return NODO_Y_TABLERO_FINAL;
	}

	/**
	 * @return the nodoColor
	 */
	public static String getNodoColor() {
		return NODO_COLOR;
	}

	/**
	 * @return the nodoTamanoTablero
	 */
	public static String getNodoTamanoTablero() {
		return NODO_TAMANO_TABLERO;
	}

	/**
	 * @return the nodoOrientacionPalabras
	 */
	public static String getNodoOrientacionPalabras() {
		return NODO_ORIENTACION_PALABRAS;
	}

	/**
	 * @return the nodoTipo
	 */
	public static String getNodoTipo() {
		return NODO_TIPO;
	}

	/**
	 * @return the nodoTipoPista
	 */
	public static String getNodoTipoPista() {
		return NODO_TIPO_PISTA;
	}

	/**
	 * @return the nODO_PARTIDAS_DEFINIDAS
	 */
	public static String getNODO_PARTIDAS_DEFINIDAS() {
		return NODO_PARTIDAS_DEFINIDAS;
	}

	/**
	 * @return the nodoFila
	 */
	public static String getNodoFila() {
		return NODO_FILA;
	}

	/**
	 * @return the nodoTablero
	 */
	public static String getNodoTablero() {
		return NODO_TABLERO;
	}

	/**
	 * @return the nodoLetra
	 */
	public static String getNodoLetra() {
		return NODO_LETRA;
	}

	/**
	 * @return the nodoNumeroFichero
	 */
	public static String getNodoNumeroFichero() {
		return NODO_NUMERO_FICHERO;
	}

	/**
	 * @return the cantidadPalabras
	 */
	public static int getCantidadPalabras() {
		return cantidadPalabras;
	}

	/**
	 * @return the primeraIteraccion
	 */
	public static boolean isPrimeraIteraccion() {
		return primeraIteraccion;
	}

	/**
	 * @return the rutaSharedPreferences
	 */
	public static String getRutaSharedPreferences() {
		return rutaSharedPreferences;
	}

	/**
	 * @return the xmlTexto
	 */
	public static String getXmlTexto() {
		return xmlTexto;
	}

	/**
	 * @return the copia
	 */
	public static String getCopia() {
		return copia;
	}

	/**
	 * @return the nodoGamas
	 */
	public static String getNodoGamas() {
		return NODO_GAMAS;
	}

	/**
	 * @return the nodoNivel
	 */
	public static String getNodoNivel() {
		return NODO_NIVEL;
	}

	/**
	 * @return the nodoPartidaActual
	 */
	public static String getNodoPartidaActual() {
		return NODO_PARTIDA_ACTUAL;
	}

	/**
	 * @return the nodoPartida
	 */
	public static String getNodoPartida() {
		return NODO_PARTIDA;
	}

	/**
	 * @return the partidaactual
	 */
	public static String getPartidaactual() {
		return partidaActual;
	}

	/**
	 * @return the temaActual
	 */
	public static String getTemaActual() {
		return temaActual;
	}

	/**
	 * @return the tamanoTablero
	 */
	public static int getTamanoTablero() {
		return tamanoTablero;
	}

	/**
	 * @return the nODO_Tamano
	 */
	public static String getNODO_TAMANO() {
		return NODO_TAMANO;
	}

	/**
	 * @return the cantidadCategorias
	 */
	public static int getCantidadCategorias() {
		return cantidadCategorias;
	}

	/**
	 * @return the aTRIBUTO_FILA
	 */
	public static String getATRIBUTO_FILA() {
		return ATRIBUTO_FILA;
	}

	/**
	 * @return the aTRIBUTO_COLUMNA
	 */
	public static String getATRIBUTO_COLUMNA() {
		return ATRIBUTO_COLUMNA;
	}

	/**
	 * @return the no
	 */
	public static String getNo() {
		return no;
	}

	/**
	 * @return the si
	 */
	public static String getSi() {
		return si;
	}

	/**
	 * @return the xml
	 */
	public static String getXml() {
		return xml;
	}

	/**
	 * @return the nODO_STRING
	 */
	public static String getNODO_STRING() {
		return NODO_STRING;
	}

	/**
	 * @return the nODO_BOOLEAN
	 */
	public static String getNODO_BOOLEAN() {
		return NODO_BOOLEAN;
	}

	/**
	 * @return the nODO_MAP
	 */
	public static String getNODO_MAP() {
		return NODO_MAP;
	}

	/**
	 * @return the tipoTablero
	 */
	public static String getTipoTablero() {
		return tipoTablero;
	}

	/**
	 * @return the iSOEspa�ol
	 */
	public static String getISOEspanol() {
		return ISOEspanol;
	}

	/**
	 * @return the iSOIngles
	 */
	public static String getISOIngles() {
		return ISOIngles;
	}

	/**
	 * @return the iSOGallego
	 */
	public static String getISOGallego() {
		return ISOGallego;
	}

	/**
	 * @return the iSOAleman
	 */
	public static String getISOAleman() {
		return ISOAleman;
	}

	/**
	 * @return the iSOItaliano
	 */
	public static String getISOItaliano() {
		return ISOItaliano;
	}

	/**
	 * @return the iSOFrances
	 */
	public static String getISOFrances() {
		return ISOFrances;
	}

	/**
	 * @return the idioma
	 */
	public static String getIdioma() {
		return idioma;
	}

	/**
	 * @return the partidaRapida
	 */
	public static String getPartidaRapida() {
		return partidaRapida;
	}

	/**
	 * @return the gama
	 */
	public static String getGama() {
		return gama;
	}

	/**
	 * @return the nODO_LETRAS_RELLENO
	 */
	public static String getNODO_LETRAS_RELLENO() {
		return NODO_LETRAS_RELLENO;
	}

	/**
	 * @return the nODO_TIPO_TABLERO
	 */
	public static String getNODO_TIPO_TABLERO() {
		return NODO_TIPO_TABLERO;
	}

	/**
	 * @return the nODO_TIPO_REFORZADOR_VISUAL
	 */
	public static String getNODO_TIPO_REFORZADOR_VISUAL() {
		return NODO_TIPO_REFORZADOR_VISUAL;
	}

	/**
	 * @return the reforzadorColor
	 */
	public static String getReforzadorColor() {
		return reforzadorColor;
	}

	/**
	 * @return the reforzadorSinReforzador
	 */
	public static String getReforzadorSinReforzador() {
		return reforzadorSinReforzador;
	}

	/**
	 * @return the reforzadorBlancoNegro
	 */
	public static String getReforzadorBlancoNegro() {
		return reforzadorBlancoNegro;
	}

	/**
	 * @return the reforzadorAltoContraste
	 */
	public static String getReforzadorAltoContraste() {
		return reforzadorAltoContraste;
	}

	/**
	 * @return the reforzadorSonido
	 */
	public static String getReforzadorSonido() {
		return reforzadorSonido;
	}

	/**
	 * @return the opcionesJuego
	 */
	public static String getOpcionesJuego() {
		return opcionesJuego;
	}

	/**
	 * @return the numeroNiveles
	 */
	public static String getNumeroNiveles() {
		return numeroNiveles;
	}

	/**
	 * @return the gamificacion
	 */
	public static String getGamificacion() {
		return gamificacion;
	}

	/**
	 * @return the ultimoNivelSuperado
	 */
	public static String getUltimoNivelSuperado() {
		return ultimoNivelSuperado;
	}

	/**
	 * @return the agregarGama
	 */
	public static String getAgregarGama() {
		return agregarGama;
	}

	/**
	 * @return the nombrePartida
	 */
	public static String getNombrePartida() {
		return nombrePartida;
	}

	/**
	 * @return the cantNivelesGama
	 */
	public static String getCantNivelesGama() {
		return cantNivelesGama;
	}

	/**
	 * @return the nivelEnJuego
	 */
	public static String getNivelEnJuego() {
		return nivelEnJuego;
	}

	/**
	 * @return the recursoDrawable
	 */
	public static String getRecursoDrawable() {
		return recursoDrawable;
	}

	/**
	 * @return the imagenCorrecto
	 */
	public static String getImagenCorrecto() {
		return imagenCorrecto;
	}

	/**
	 * @return the crearCarpetasInstalacion
	 */
	public static String getCrearCarpetasInstalacion() {
		return crearCarpetasInstalacion;
	}

}
