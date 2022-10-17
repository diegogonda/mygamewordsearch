package objetos;

import java.util.Locale;
import android.content.Context;
import android.content.res.Configuration;

public class Idioma {

	public Idioma () {
		
	}

	/**
	 * Método que nos permite cambiar el idioma de una actividad
	 * @param contexto
	 */
	public static void CambiaIdioma (Context contexto){	
		String languageToLoad = Preferencias.getIdioma (contexto);
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		contexto.getResources().updateConfiguration(config, contexto.getResources().getDisplayMetrics());
	}
	
	/**
	 * Método que devuelve el idioma actualmente activado 
	 * @param contexto
	 * @return idioma activado
	 */
	public static String getIdiomaActual (Context contexto){
		return Preferencias.getIdioma (contexto); // your language
	}
}
