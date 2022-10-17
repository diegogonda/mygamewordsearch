package objetos;

import java.io.File;
import java.util.ArrayList;
import android.os.Environment;
import android.util.Log;

public class TarjetaSD {
	private static String rutaSD = Environment.getExternalStorageDirectory().toString();
	private static String estado = Environment.getExternalStorageState();

	public TarjetaSD() {

	}

	/**
	 * Método que nos informa del estado de la tarjeta SD
	 * 
	 * @return true si está conectada
	 * @return false si está desconectada
	 */
	public static boolean isSDConectada() {
		return (estado.equals(Environment.MEDIA_MOUNTED)) ? true : false;
	}

	/**
	 * Método que nos informa si se puede escribir en la tarjeta SD
	 * 
	 * @return true si se puede
	 * @return false si no
	 */
	public static boolean isPermisoEscritura() {
		return (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) ? false : true;
	}

	/**
	 * Ruta en que está montada la tarjeta SD
	 * 
	 * @return the rutaSD
	 */
	public static String getRutaSD() {
		return rutaSD;
	}

	/**
	 * Esta funcion nos permite saber si existe o no una carpeta en una ruta determinada
	 * 
	 * @param ruta
	 *            es el ruta de la carpeta
	 * @param nombreCarpeta
	 *            nombre de la carpeta que queremos saber si existe
	 * @return true si existe
	 * @return false si no existe
	 */
	public static boolean existeCarpetaSD(String ruta, String nombreCarpeta) {
		File[] files = (new File(ruta)).listFiles();
		for (File aux : files)
			if (aux.getName().equals(nombreCarpeta) && aux.isDirectory())
				return true;
		return false;
	}

	/**
	 * Este método nos permite saber si existe o no un archivo en un ruta determinada
	 * 
	 * @param ruta
	 *            es el ruta de la archivo
	 * @param nombreArchivo
	 *            nombre de la archivo que queremos saber si existe
	 * @return true si existe
	 * @return false si no existe
	 */
	public static boolean existeFicheroSD(String ruta, String nombreArchivo) {
		File[] files = (new File(ruta)).listFiles();
		for (File aux : files)
			if (aux.getName().equals(nombreArchivo) && aux.isFile())
				return true;
		return false;
	}

	/**
	 * Esta función nos permite crear una ruta dentro de la tarjeta SD, si la ruta existe la sobreescribe.
	 * Si no existe la crea
	 * 
	 * @param ruta
	 *            ruta en la que queremos escribir
	 */
	public static void crearRutaSD(String ruta) {
		File f = new File(ruta);
		if (!f.exists() && !f.isDirectory())
			f.mkdirs();
	}

	/**
	 * Método que devuelve el número de archivos que tiene una ruta determinada
	 * 
	 * @return cantida de archivos de una ruta
	 */
	public static int cantidadElementosEnCarpeta(String ruta) {
		return new File(ruta).listFiles().length;
	}

	/**
	 * Método que elimina el contenido completo de una ruta dada de la tarjeta SD
	 * 
	 * @param ruta
	 *            ruta la cual quiere eliminarse el contenido
	 * @return false si ocurrió algún error eliminando
	 * @return true si todo fue correcto
	 */
	public static boolean eliminaContenidoCarpeta(String ruta) {
		try {
			File file = new File(ruta);
			if (file.isFile())
				file.delete();
			else if (file.isDirectory()) {
				for (File aux : file.listFiles())
					eliminaContenidoCarpeta(aux.getAbsolutePath());
				file.delete();
			}
		} catch (Exception e) {
			Log.e("Error -> TarjetaSD -> eliminaContenidoCarpeta", " " + e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Este método sirve para saber qué carpetas hay guardados en una determinada tarjeta SD de un terminal
	 * 
	 * @param ruta
	 *            Ruta de la que queremos obtener el contenido
	 * @return ArrayList con los nombres de las carpetas que hay en la ruta indicada
	 * @return null en los casos en que la tarjeta SD no esté conectada o no haya partidas creadas para jugar
	 */
	public static ArrayList<String> leerCarpetasSD(String ruta) {
		try {
			crearRutaSD(ruta);
			File f[] = (new File(ruta)).listFiles();
			ArrayList<String> carpetas = new ArrayList<String>();
			for (File aux : f)
				if (aux.isDirectory())
					carpetas.add(aux.getName());
			return carpetas;
		} catch (Exception e) {
			Log.e("EstadoSD -> leerArchivosSD", "Error: " + e);
			return null;
		}
	}
}
