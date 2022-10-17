package actividades;

import objetos.Actividades;
import objetos.EtiquetasXML;
import objetos.Idioma;
import objetos.Preferencias;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;
import es.uvigo.gti.mygamewordsearch.R;

public class ConfiguraActivity extends Activity implements OnItemSelectedListener {
	private static Spinner IdiomaSpinner;

	private static Spinner VisualSpinner;
	private static Context contexto;
	private static boolean primeraIteraccionIdioma = false;
	private static boolean primeraIteraccionVisual = false;

	private String tipoReforzador;

	private boolean reforzadorSonoro;
	private CheckBox boxSonoro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
				contexto = this;
		Idioma.CambiaIdioma(this);

			cargaValoresDefinidos();

			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.activity_configura);

			IdiomaSpinner = (Spinner) findViewById(R.id.spinner_idioma);

			ArrayAdapter<CharSequence> adapterIdioma = ArrayAdapter.createFromResource(this, R.array.idioma, android.R.layout.simple_spinner_item);
			adapterIdioma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			IdiomaSpinner.setAdapter(adapterIdioma);

			IdiomaSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> av, View v, int posicion, long id) {
					opcionesIdioma(av.getItemAtPosition(posicion).toString());
				}

				@Override
				public void onNothingSelected(AdapterView<?> av) {

				}
			});

			VisualSpinner = (Spinner) findViewById(R.id.spinner_visual);
			ArrayAdapter<CharSequence> adapterVisual = ArrayAdapter.createFromResource(this, R.array.imagen, android.R.layout.simple_spinner_item);
			adapterVisual.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			VisualSpinner.setAdapter(adapterVisual);
			if (tipoReforzador.equals(EtiquetasXML.getReforzadorSinReforzador()))
				VisualSpinner.setSelection(0);
			else if (tipoReforzador.equals(EtiquetasXML.getReforzadorColor()))
				VisualSpinner.setSelection(1);
			else if (tipoReforzador.equals(EtiquetasXML.getReforzadorBlancoNegro()))
				VisualSpinner.setSelection(2);
			else if (tipoReforzador.equals(EtiquetasXML.getReforzadorAltoContraste()))
				VisualSpinner.setSelection(3);

			VisualSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> av, View v, int posicion, long id) {
					if (!primeraIteraccionVisual) {

						String reforzador = av.getItemAtPosition(posicion).toString();
						if (reforzador.equals("No picture") || reforzador.equals("Sin imñgenes") || reforzador.equals("Pas d'image") || reforzador.equals("Sen imaxe") || reforzador.equals("kein Bild") || reforzador.equals("Senza foto")) {
							Preferencias.setReforzadorVisual(EtiquetasXML.getReforzadorSinReforzador(), contexto);
						} else if (reforzador.equals("En color") || reforzador.equals("En cor") || reforzador.equals("Color") || reforzador.equals("Farbe") || reforzador.equals("Colore") || reforzador.equals("couleur")) {
							Preferencias.setReforzadorVisual(EtiquetasXML.getReforzadorColor(), contexto);
						} else if (reforzador.equals("Black and white") || reforzador.equals("En blanco y negro") || reforzador.equals("Branco e negro") || reforzador.equals("Noir et blanc") || reforzador.equals("Schwarz und Weiñ") || reforzador.equals("In bianco e nero")) {
							Preferencias.setReforzadorVisual(EtiquetasXML.getReforzadorBlancoNegro(), contexto);
						} else if (reforzador.equals("High contrast") || reforzador.equals("En alto contraste") || reforzador.equals("Alto contraste") || reforzador.equals("hoher Kontrast") || reforzador.equals("Contraste ñlevñ") || reforzador.equals("Alto contrasto")) {
							Preferencias.setReforzadorVisual(EtiquetasXML.getReforzadorAltoContraste(), contexto);
						}
					} else
						primeraIteraccionVisual = false;
				}

				@Override
				public void onNothingSelected(AdapterView<?> av) {

				}

			});

		} catch (Exception e) {
			Log.e("ConfiguraActivity -> onCreate", "Error");
			Toast.makeText(contexto, "error", Toast.LENGTH_LONG).show();
		}
	}

	private void opcionesIdioma(String idiomaCambio) {
		if (!primeraIteraccionIdioma) {

			if (idiomaCambio.equals("Castellano") || idiomaCambio.equals("Castelñn") || idiomaCambio.equals("Espagnol") || idiomaCambio.equals("Spagnolo") || idiomaCambio.equals("Spanish") || idiomaCambio.equals("Spanisch")) {
				Preferencias.setIdioma(EtiquetasXML.getISOEspañol(), contexto);
			} else if (idiomaCambio.equals("Gallego") || idiomaCambio.equals("Galego") || idiomaCambio.equals("Galizischen") || idiomaCambio.equals("Galicien") || idiomaCambio.equals("Galiziano") || idiomaCambio.equals("Galician")) {
				Preferencias.setIdioma(EtiquetasXML.getISOGallego(), contexto);
			} else if (idiomaCambio.equals("Inglñs") || idiomaCambio.equals("Inglñs") || idiomaCambio.equals("Anglais") || idiomaCambio.equals("Inglese") || idiomaCambio.equals("English") || idiomaCambio.equals("Englisch")) {
				Preferencias.setIdioma(EtiquetasXML.getISOIngles(), contexto);
			} else if (idiomaCambio.equals("Francñs") || idiomaCambio.equals("Francñs") || idiomaCambio.equals("Franñais") || idiomaCambio.equals("Francese") || idiomaCambio.equals("French") || idiomaCambio.equals("Franzñsisch")) {
				Preferencias.setIdioma(EtiquetasXML.getISOFrances(), contexto);
			} else if (idiomaCambio.equals("Italiano") || idiomaCambio.equals("Italian") || idiomaCambio.equals("Italienisch") || idiomaCambio.equals("Italiano") || idiomaCambio.equals("Italiano") || idiomaCambio.equals("Italien")) {
				Preferencias.setIdioma(EtiquetasXML.getISOItaliano(), contexto);
			} else if (idiomaCambio.equals("Alemñn") || idiomaCambio.equals("Deutsch") || idiomaCambio.equals("Alemñn") || idiomaCambio.equals("Allemand") || idiomaCambio.equals("Tedesco") || idiomaCambio.equals("German")) {
				Preferencias.setIdioma(EtiquetasXML.getISOAleman(), contexto);
			} else
				Preferencias.setIdioma(EtiquetasXML.getISOIngles(), contexto);
		//	Intents();
		} else
			primeraIteraccionIdioma = false;

		boxSonoro = (CheckBox) findViewById(R.id.checkBoxSonoro);
		boxSonoro.setChecked(reforzadorSonoro);
		boxSonoro.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// is chkIos checked?
				if (((CheckBox) v).isChecked()) { // CheckBox Seleccionado
					Preferencias.setSonidosActivados(true, contexto);
				}
				if (!((CheckBox) v).isChecked()) {// CheckBox NO Seleccionado
					Preferencias.setSonidosActivados(false, contexto);

				}

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Actividades.iniciarActividad(MainActivity.class, this);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemSelected(AdapterView<?> av, View v, int posicion, long id) {

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	private void cargaValoresDefinidos() {
		try {

			tipoReforzador = Preferencias.getReforzadorVisual(contexto);	

			reforzadorSonoro = Preferencias.isSonidosActivados(contexto);
		} catch (Exception e) {
			tipoReforzador = EtiquetasXML.getReforzadorColor();
			reforzadorSonoro = true;
			Toast.makeText(contexto, "error", Toast.LENGTH_LONG).show();
			Log.e("ConfiguraActivity -> cargaValoresDefinidos", "Error");

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.configura, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
