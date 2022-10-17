package actividades;

import objetos.Actividades;
import objetos.CreaTablero;
import objetos.MensajesEmergentes;
import objetos.Idioma;
import objetos.Preferencias;
import objetos.Rutas;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import es.uvigo.gti.mygamewordsearch.R;

public class JuegoActivity extends Activity {

	private TextView t;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//Creamos el tablero de juego
		crearTablero();
		
		setContentView(R.layout.activity_juego);

		t = (TextView) findViewById(R.id.textView_Nombre_Partida);

		try {
			t.setText(Preferencias.getNombrePartida(this));
			Idioma.CambiaIdioma(this); // Cambiamos el idioma de la aplicación
		} catch (Exception e) {
			t.setText("-");
			Log.e("JuegoActivity -> OnCreate", " " + e.getMessage());
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			MensajesEmergentes.AtrasFinalizarJuego(this);
			return true;
		}
		return false;
	}

	private void crearTablero (){
		try {
			CreaTablero ct = new CreaTablero(this);
			String ruta = Preferencias.getNombrePartida(this) + Rutas.getNivel() + Preferencias.getNivelActual(this);
			ct.generaPartidaRapida(ruta);
		} catch (Exception e) {
			Toast.makeText(this, R.string.error_creacion_partida, Toast.LENGTH_LONG).show();
			Log.e("JuegoActivity -> crearTablero", " " + e.getMessage());
			Actividades.iniciarActividad(MainActivity.class, this);
		}
	}
	/**
	 * Eliminamos el botón settings de las pantallas
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.juego, menu);
		return true;
	}

	public void onSaveInstanceState(Bundle icicle) {
		super.onSaveInstanceState(icicle);
	}

}
