package objetos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import actividades.MainActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class Imagenes {

	private static int tamañoPantalla;
	private static Context contexto;
	private static int SELECT_IMAGE = 237487;

	public Imagenes(Context c) {
		contexto = c;
	}

	/**
	 * @return the tamañoPantalla
	 */
	public int getTamañoPantalla() {
		int screenSize = getContexto().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		switch (screenSize) {
		case Configuration.SCREENLAYOUT_SIZE_XLARGE:
			tamañoPantalla = 4; // Pantalla Extra-Large
		case Configuration.SCREENLAYOUT_SIZE_LARGE:
			tamañoPantalla = 3; // Pantalla larga
			break;
		case Configuration.SCREENLAYOUT_SIZE_NORMAL:
			tamañoPantalla = 2; // Pantalla Normal
			break;
		case Configuration.SCREENLAYOUT_SIZE_SMALL:
			tamañoPantalla = 1; // Pantalla pequeña
			break;
		default:
			tamañoPantalla = 2; // si no es ninguna de las anteriores, asumimos que es normal
		}
		return tamañoPantalla;
	}

	/**
	 * @param tamañoPantalla
	 *            the tamañoPantalla to set
	 */
	public static void setTamañoPantalla(int tamañoPantalla) {
		Imagenes.tamañoPantalla = tamañoPantalla;
	}

	public static Context getContexto() {
		return contexto;
	}

	public static void setContexto(Context contexto) {
		Imagenes.contexto = contexto;
	}

	/**
	 * Método que nos devuelve una imagen adaptada a un nuevo alto y ancho
	 * 
	 * @param bm
	 *            bitmap que queremos modificar
	 * @param newHeight
	 *            nuevo alto
	 * @param newWidth
	 *            nuevo ancho
	 * @return
	 */
	public static Bitmap getResizedBitmap(Bitmap bm, int tamañoCuadrado, int borrar) {
		int base = bm.getWidth();
		int altura = bm.getHeight();
		if (base >= altura) {
			int x = altura * tamañoCuadrado / base;
			base = tamañoCuadrado;
			altura = x;
		} else {
			int x = base * tamañoCuadrado / base;
			altura = tamañoCuadrado;
			base = x;
		}
		return Bitmap.createScaledBitmap(bm, base, altura, false);
	}

	public static void dialogoGaleriaImagenes(Context contexto) {
		try {
			Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
			intent.setType("image/*");
			Activity activity = (Activity) contexto;
			activity.startActivityForResult(intent, SELECT_IMAGE);
		}
		catch (OutOfMemoryError e){
			Actividades.iniciarActividad(MainActivity.class, contexto);
			
		}
	}

	/**
	 * @return the sELECT_IMAGE
	 */
	public static int getSELECT_IMAGE() {
		return SELECT_IMAGE;
	}

	/**
	 * Método que nos permite verificar si existe o no un determinado fichero
	 * 
	 * @param rutaImagen
	 *            ruta del archivo que queremos verificar si existe
	 * @return true si el archivo existe
	 * @return false si el archivo no existe
	 */
	public static boolean existeArchivo(String rutaArchivo) {
		File file = new File(rutaArchivo);
		return (file.exists()) ? true : false;
	}

	/**
	 * Método que recibe la ruta de una imagen y la copia en la ruta especificada
	 * OJO! Pese a que si comprueba si el fichero original existe, esta función
	 * sobrescribe la imagen en el destino, por lo tanto hay que verificar antes si no se quiere
	 * sobreescribir
	 * 
	 * @param ruta
	 *            ruta de la imagen original
	 * @param rutaImagen
	 *            ruta destino donde queremos copiar la imagen
	 * @param contexto
	 *            contexto de la actividad desde la cual se llama al método
	 * 
	 * @return true si todas las operaciones se realizaron correctamente
	 * @return false si en el proceso hubo algún error
	 * */
	public static boolean guardaImagenes(String rutaOriginal, String rutaDestino) {
		try {

			File original = new File(rutaOriginal);
			if (!existeArchivo(rutaOriginal)) // Observamos que el original exista
				return false;

			File copia = new File(rutaDestino);

			// Si el usuario quiere cambiar una imagen debe sobreescribir, por lo tanto no verificamos si la imagen existe

			InputStream in = new FileInputStream(original);
			OutputStream out = new FileOutputStream(copia);

			byte[] buf = new byte[1024];
			int len;

			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			in.close();
			out.close();
			return true;

		} catch (Exception e) {
			Log.e("Error: Imagenes -> guardaImagenes", e.getMessage());
			return false;
		}
	}

	public static Drawable getIdImagenes(int i, Context contexto) {
		Resources res = contexto.getResources();
		if (i == 0)
			return res.getDrawable(contexto.getResources().getIdentifier("numeros_uno", EtiquetasXML.getRecursoDrawable(), Rutas.getNombrePaquete()));
		else if (i == 1)
			return res.getDrawable(contexto.getResources().getIdentifier("numeros_dos", EtiquetasXML.getRecursoDrawable(), Rutas.getNombrePaquete()));
		else if (i == 2)
			return res.getDrawable(contexto.getResources().getIdentifier("numeros_tres", EtiquetasXML.getRecursoDrawable(), Rutas.getNombrePaquete()));
		else if (i == 3)
			return res.getDrawable(contexto.getResources().getIdentifier("numeros_cuatro", EtiquetasXML.getRecursoDrawable(), Rutas.getNombrePaquete()));
		else if (i == 4)
			return res.getDrawable(contexto.getResources().getIdentifier("numeros_cinco", EtiquetasXML.getRecursoDrawable(), Rutas.getNombrePaquete()));
		else if (i == 5)
			return res.getDrawable(contexto.getResources().getIdentifier("numeros_seis", EtiquetasXML.getRecursoDrawable(), Rutas.getNombrePaquete()));
		else if (i == 6)
			return res.getDrawable(contexto.getResources().getIdentifier("numeros_siete", EtiquetasXML.getRecursoDrawable(), Rutas.getNombrePaquete()));
		else if (i == 7)
			return res.getDrawable(contexto.getResources().getIdentifier("numeros_ocho", EtiquetasXML.getRecursoDrawable(), Rutas.getNombrePaquete()));
		else if (i == 8)
			return res.getDrawable(contexto.getResources().getIdentifier("numeros_nueve", EtiquetasXML.getRecursoDrawable(), Rutas.getNombrePaquete()));
		return res.getDrawable(contexto.getResources().getIdentifier("numeros_candado", EtiquetasXML.getRecursoDrawable(), Rutas.getNombrePaquete()));
	}

}
