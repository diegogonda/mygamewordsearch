package objetos;

import java.util.ArrayList;

import actividades.AgregarPartidasActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import es.uvigo.gti.mygamewordsearch.R;

public class Actividades {

	public Actividades() {

	}
	/**
	 * Método que nos permite abrir una nueva actividad. No permite pasar valores a través de Bundle
	 * @param clase Actividad que queremos abrir
	 * @param contexto Contexto de la actividad actual
	 */
	public static void iniciarActividad (Class<?> clase, Context contexto) {
		Intent i = new Intent(contexto, clase);
		contexto.startActivity(i);
		((Activity) contexto).finish();
		((Activity) contexto).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	/**
	 * Método que permite abrir una nueva actividad a la que se pasarán valores a través de Bundle 
	 * @param clase Actividad que queremos abrir
	 * @param contexto Contexto de la actividad actual
	 * @param clave Etiqueta asociada a un valor
	 * @param valor Valor asociado a una etiquta
	 */
	public static void iniciarActividadParametros (Class<?> clase, Context contexto, ArrayList<Object> clave, ArrayList<Object> valor) {
		Intent i = new Intent(contexto, clase);
		int cont = 0;
		if (clave != null && valor != null)
			for (Object aux : clave)
				i.putExtra(aux.toString(), valor.get(cont++).toString());
		contexto.startActivity(i);
		((Activity) contexto).finish();
		((Activity) contexto).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		
	}
	/**
	 * Método que permite abrir la actividad NuevaPartidaActivity a la que se pasarán valores a través de Bundle 
	 * @param clase Actividad que queremos abrir
	 * @param contexto Contexto de la actividad actual
	 * @param clave Etiqueta se va a pasar por Bundle
	 * @param valor Valor asociado a esa etiqueta
	 */
	public static void iniciarActividadNuevaPartida (Class<?> clase, Context contexto, String clave, boolean valor){
		Intent i = new Intent(contexto, clase);
		i.putExtra(clave, valor);
		contexto.startActivity(i);
		((Activity) contexto).finish();
		((Activity) contexto).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	public static void iniciarActividadAgregarPartidasDosParametros (Class<?> clase, Context contexto, String categoria, boolean gama ){
		Intent i = new Intent(contexto, clase);
		if (clase == AgregarPartidasActivity.class){
			i.putExtra(EtiquetasXML.getNombrePartida(), categoria);
			i.putExtra(EtiquetasXML.getAgregarGama(), gama);
		}
		contexto.startActivity(i);
		((Activity) contexto).finish();
		((Activity) contexto).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}


}
