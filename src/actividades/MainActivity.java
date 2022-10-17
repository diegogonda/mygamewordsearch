package actividades;

import objetos.Idioma;
import objetos.Preferencias;
import objetos.Rutas;
import objetos.UnZIP;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import es.uvigo.gti.mygamewordsearch.R;

public class MainActivity extends Activity {
	int tamañoImagenes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Preferencias.isCreaPartidasDefecto(this)){
			UnZIP.unzipFromAssets(this, "Planetas.zip", Rutas.getRutaArchivos());
			UnZIP.unzipFromAssets(this, "Planets.zip", Rutas.getRutaArchivos());
			Preferencias.setCreaPartidasDefecto(this, false);
		}
		Idioma.CambiaIdioma(this); //Cambiamos el idioma de la aplicación

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);		
		
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.finish();
	}

	/**
	 * Eliminamos el botón settings de las pantallas
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return false;
	}

}
