package objetos;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferencias {

	private static String tipoPista;
	private static boolean sonidosActivados;
	private static String reforzadorVisual;
	private static boolean botonAtras;
	private static boolean letrasRelleno;
	private static boolean palabrasInvertidas;
	private static String idioma;
	private static String orientacionPalabras;
	private static String tamanoTablero;
	private static String tipoTablero;
	
	public Preferencias(Context contexto) {

	}

	/**
	 * @return the tipoPista
	 */
	public static String getTipoPista(Context contexto) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
		tipoPista = pref.getString(EtiquetasXML.getNodoTipoPista(), EtiquetasXML.getNodoNombre()); // your language

		return tipoPista;
	}

	/**
	 * @param tipoPista
	 *            the tipoPista to set
	 */
	public static void setTipoPista(String tipoPista, Context contexto) {
		SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(contexto).edit();
		spe.putString(EtiquetasXML.getNodoTipoPista(), tipoPista);
		spe.commit();
		Preferencias.tipoPista = tipoPista;
	}

	/**
	 * @return the sonidosActivados
	 */
	public static boolean isSonidosActivados(Context contexto) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
		sonidosActivados = pref.getBoolean (EtiquetasXML.getReforzadorSonido(), true); // your language
		
		return sonidosActivados;
	}

	/**
	 * @param sonidosActivados
	 *            the sonidosActivados to set
	 */
	public static void setSonidosActivados(boolean sonidosActivados, Context contexto) {
		SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(contexto).edit();
		spe.putBoolean(EtiquetasXML.getReforzadorSonido(), sonidosActivados);
		spe.commit();

		Preferencias.sonidosActivados = sonidosActivados;
	}

	/**
	 * Metodo que obtiene la configuracion del tipo de reforzador visual que vamos a utilizar
	 * @return the reforzadorVisual
	 */
	public static String getReforzadorVisual(Context contexto) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
		reforzadorVisual = pref.getString(EtiquetasXML.getNODO_TIPO_REFORZADOR_VISUAL(), EtiquetasXML.getReforzadorColor()); // your language
		return reforzadorVisual;
	}

	/**
	 * Metodo que modifica la configuracion del tipo de reforzador visual que vamos a utilizar
	 * @param reforzadorVisual
	 *            the reforzadorVisual to set
	 */
	public static void setReforzadorVisual(String reforzadorVisual, Context contexto) {
		SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(contexto).edit();
		spe.putString(EtiquetasXML.getNODO_TIPO_REFORZADOR_VISUAL(), reforzadorVisual);
		spe.commit();
		Preferencias.reforzadorVisual = reforzadorVisual;
	}

	/**
	 * @return the botonAtras
	 */
	public static boolean isBotonAtras(Context contexto) {
		return botonAtras;
	}

	/**
	 * @param botonAtras
	 *            the botonAtras to set
	 */
	public static void setBotonAtras(boolean botonAtras, Context contexto) {
		Preferencias.botonAtras = botonAtras;
	}

	/**
	 * @return the letrasRelleno
	 */
	public static boolean isLetrasRelleno(Context contexto) {
		return letrasRelleno;
	}

	/**
	 * @param letrasRelleno
	 *            the letrasRelleno to set
	 */
	public static void setLetrasRelleno(boolean letrasRelleno, Context contexto) {
		SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(contexto).edit();
		spe.putBoolean(EtiquetasXML.getNODO_LETRAS_RELLENO(), letrasRelleno);
		spe.commit();
		Preferencias.letrasRelleno = letrasRelleno;
	}

	/**
	 * Metodo que permite conocer si el tablero tiene o no palabras invertidas
	 * @return the palabrasInvertidas
	 */
	public static boolean isPalabrasInvertidas(Context contexto) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
		palabrasInvertidas = pref.getBoolean(EtiquetasXML.getNodoInvertida(), true); // your language
		return palabrasInvertidas;
	}

	/**
	 * Metodo que permite modificar para que el tablero tenga o no palabras invertidas
	 * @param palabrasInvertidas
	 *            the palabrasInvertidas to set
	 */
	public static void setPalabrasInvertidas(boolean palabrasInvertidas, Context contexto) {
		SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(contexto).edit();
		spe.putBoolean(EtiquetasXML.getNodoInvertida(), palabrasInvertidas);
		spe.commit();
		Preferencias.palabrasInvertidas = palabrasInvertidas;
	}

	/**
	 * Metodo que recupera la informacion del idioma del archivo SharedPreferences
	 * @return the idioma
	 */
	public static String getIdioma(Context contexto) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
		idioma = pref.getString(EtiquetasXML.getIdioma(), EtiquetasXML.getISOEspanol()); // your language
		return idioma;
	}

	/**
	 * Metodo que cambia la configuraci�n del idioma en el archivo SharedPreferences
	 * @param idioma
	 *            the idioma to set
	 */
	public static void setIdioma(String idioma, Context contexto) {
		SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(contexto).edit();
		spe.putString(EtiquetasXML.getIdioma(), idioma);
		spe.commit();
		Preferencias.idioma = idioma;
	}

	/**
	 * Metodo que recupera la informacion de la orientaci�n de las palabras en el archivo SharedPreferences
	 * @return the orientacionPalabras
	 */
	public static String getOrientacionPalabras(Context contexto) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
		orientacionPalabras = pref.getString(EtiquetasXML.getNodoOrientacionPalabras(), "10"); // your language

		return orientacionPalabras;
	}

	/**
	 * Metodo que cambia la configuraci�n de la orientaci�n de las palabras en el archivo SharedPreferences
	 * @param orientacionPalabras
	 *            the orientacionPalabras to set
	 */
	public static void setOrientacionPalabras(String orientacionPalabras, Context contexto) {
		SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(contexto).edit();
		spe.putString(EtiquetasXML.getNodoOrientacionPalabras(), orientacionPalabras);
		spe.commit();
		Preferencias.orientacionPalabras = orientacionPalabras;
	}

	/**
	 * Obtiene el tamano del tablero en el archivo SharedPreferences
	 * @return the tamanoTablero
	 */
	public static String getTamanoTablero(Context contexto) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
		tamanoTablero = pref.getString(EtiquetasXML.getNodoTamanoTablero(), "10"); // your language
	
		return tamanoTablero;
	}

	/**
	 * Modifica el tamano del tablero en el archivo SharedPreferences
	 * @param tamanoTablero
	 *            the tamanoTablero to set
	 */
	public static void settamanoTablero(String tamanoTablero, Context contexto) {
		SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(contexto).edit();
		spe.putString(EtiquetasXML.getNodoTamanoTablero(), tamanoTablero);
		spe.commit();
		Preferencias.tamanoTablero = tamanoTablero;
	}

	/**
	 * Metodo que obtiene el tipo de tablero (aleatorio o predefinido)
	 * En desuso
	 * @return tipo de tablero
	 */
	public static String getTipoTablero(Context contexto) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
		tipoTablero = pref.getString(EtiquetasXML.getNODO_TIPO_TABLERO(), "aleatorios"); // your language
		return tipoTablero;
	}
	/**
	 * Metodo que cambiar el tipo de tablero (aleatorio o predefinido)
	 * En desuso
	 */
	public static void setTipoTablero(String tipoTablero, Context contexto) {
		Preferencias.tipoTablero = tipoTablero;
	}

	/**
	 * @return the tipoPartida
	 */
	public static String getTipoPartida(Context contexto) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
		return pref.getString(EtiquetasXML.getNodoPartidaActual(), EtiquetasXML.getPartidaRapida()); // your language
	}

	/**
	 * @param tipoPartida the tipoPartida to set
	 */
	public static void setTipoPartida(String tipoPartida, Context contexto) {
		SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(contexto).edit();
		spe.putString(EtiquetasXML.getNodoPartidaActual(), tipoPartida);
		spe.commit();
	}

	/**
	 * @return the cantNivelesGama
	 */
	public static int getCantNivelesGama(Context contexto) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
		return pref.getInt(EtiquetasXML.getCantNivelesGama(), 1); // your language
	}

	/**
	 * @param cantNivelesGama the cantNivelesGama to set
	 */
	public static void setCantNivelesGama(int cantNivelesGama, Context contexto) {
		SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(contexto).edit();
		spe.putInt(EtiquetasXML.getCantNivelesGama(), cantNivelesGama);
		spe.commit();
	}

	/**
	 * @return the ultimoNivelSuperado
	 */
	public static int getUltimoNivelSuperado(Context contexto) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
		return pref.getInt(EtiquetasXML.getUltimoNivelSuperado(), 1); // your language
	}

	/**
	 * @param ultimoNivelSuperado the ultimoNivelSuperado to set
	 */
	public static void setUltimoNivelSuperado(int ultimoNivelSuperado, Context contexto) {
		SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(contexto).edit();
		spe.putInt(EtiquetasXML.getUltimoNivelSuperado(), ultimoNivelSuperado);
		spe.commit();
	}

	/**
	 * @return the nivelActual
	 */
	public static int getNivelActual(Context contexto) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
		return pref.getInt(EtiquetasXML.getNivelEnJuego(), 1); // your language
	}

	/**
	 * @param nivelActual the nivelActual to set
	 */
	public static void setNivelActual(int nivelActual, Context contexto) {
		SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(contexto).edit();
		spe.putInt(EtiquetasXML.getNivelEnJuego(), nivelActual);
		spe.commit();
	}
	/**
	 * @return the nivelActual
	 */
	public static String getNombrePartida (Context contexto) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
		return pref.getString(EtiquetasXML.getTemaActual(), "sin_nombre"); // your language
	}

	/**
	 * @param nivelActual the nivelActual to set
	 */
	public static void setNombrePartidal(String NombrePartida, Context contexto) {
		SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(contexto).edit();
		spe.putString(EtiquetasXML.getTemaActual(), NombrePartida);
		spe.commit();
	}

	/**
	 * @return the recienInstalado
	 */
	public static boolean isCreaPartidasDefecto(Context contexto) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
		return 	pref.getBoolean (EtiquetasXML.getCrearCarpetasInstalacion(), true); // your language
	}

	/**
	 * @param recienInstalado the recienInstalado to set
	 */
	public static void setCreaPartidasDefecto(Context contexto, boolean recienInstalado) {
		SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(contexto).edit();
		spe.putBoolean(EtiquetasXML.getCrearCarpetasInstalacion(), recienInstalado);
		spe.commit();
	}
	
	
	
	
}
