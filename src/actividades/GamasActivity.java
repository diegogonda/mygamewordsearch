package actividades;

import objetos.MensajesEmergentes;
import objetos.Idioma;
import objetos.Preferencias;
import es.uvigo.gti.mygamewordsearch.R;
import xml.XMLParser;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.widget.TextView;

public class GamasActivity extends Activity {

	private XMLParser xml;
	private static TextView nombreGama;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_gamas);

		xml = new XMLParser(this);
		xml.guardaConfiguracionTableros();

		Idioma.CambiaIdioma(this); // Cambiamos el idioma de la aplicaciXn

		nombreGama = (TextView) findViewById(R.id.textView_Nombre_Gama);
		nombreGama.setText(Preferencias.getNombrePartida(this));
	}

	public void onSaveInstanceState(Bundle icicle) {
		super.onSaveInstanceState(icicle);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			MensajesEmergentes.AtrasSalir(this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Eliminamos el botXn settings de las pantallas
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return false;
	}
}
