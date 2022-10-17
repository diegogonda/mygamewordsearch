package actividades;

import java.util.ArrayList;

import listViewPersonalizado.ConfiguracionPartida;
import objetos.Actividades;
import objetos.EtiquetasXML;
import objetos.Idioma;
import objetos.Imagenes;
import objetos.Rutas;
import objetos.TarjetaSD;
import xml.XMLParser;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import es.uvigo.gti.mygamewordsearch.R;

public class AgregarPartidasActivity extends Activity implements OnClickListener {
	/**
	 * Variables de los ficheros de Imñgenes
	 */
	private String rutaPrimaria = Rutas.getRutaArchivos();
	private String rutaImagenes = Rutas.getImagenes();
	private ImageView eligeImageView;
	private int SELECT_IMAGE = Imagenes.getSELECT_IMAGE();
	private Button bImagen;
	private String nombre = new String();
	private String imagenStr = new String();
	private String direccionImagen = new String();
	private boolean imagenSubida = false;
	/**
	 * Variables relacionadas con el botñn agregar partida
	 */
	private Button bAgregar;
	/**
	 * Variables relacionadas con las palabras invertidas
	 */
	private RadioGroup palabrasInvertidas;
	private int palabrasInvertidasId = 0;
	private int opcionInvertida = R.id.radio_boton_si;
	/**
	 * Variables relacionadas con el campo de texto que guarda el nombre del tema del juego
	 */
	private EditText categoria;
	/**
	 * Variables relacionadas con el tipo de pista
	 */
	private Spinner spTipoPista;
	private ArrayAdapter<?> aaTipoPista;
	private int tipoPistaId = -1;
	private String[] eTipoPista = new String[] { EtiquetasXML.getNodoNombre(), EtiquetasXML.getNodoPistaImagen(), EtiquetasXML.getNodoPistaAudio(), EtiquetasXML.getNodoPistaDescripcion(),
			EtiquetasXML.getNodoPistaMarca(), EtiquetasXML.getNodoPistaSinPista() };

	/**
	 * Variables relacionadas con la orientacion
	 */
	private Spinner spOrientacion;
	private ArrayAdapter<?> aaOrientacion;
	private int orientacionId = -1;
	private String[] eOrientaciones = new String[] { "1", "2", "3", "4", "5" };
	/**
	 * Variables relacionadas con el tamaño del tablero
	 */
	private Spinner spTamaño;
	private ArrayAdapter<?> aaTamaño;
	private int posicionId = -1;
	private String[] eTamaños = new String[] { "4", "5", "6", "7", "8", "9", "10", "11", "12" };
	private boolean GamificarPartida = false;
	private String rutaArchivoPalabras;
	private String rutaArchivoConfiguracion;
	private String rutaArchivoOpciones;
	private String categoriaStr = null;
	private String rutaArchivoImagenes;
	private int nivel;

	/**
	 * Variables relacionadas con la tarjeta SD
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Idioma.CambiaIdioma(this); // Cambiamos el idioma de la aplicaciñn
		Bundle bundle = getIntent().getExtras();

		GamificarPartida = bundle.getBoolean(EtiquetasXML.getAgregarGama());
		if (GamificarPartida) {
			categoriaStr = bundle.getString(EtiquetasXML.getNombrePartida());
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_agregar_partidas);

		eligeImageView = (ImageView) this.findViewById(R.id.agregar_partidas_imagen);
		bAgregar = (Button) findViewById(R.id.boton_agregar_partida);
		bImagen = (Button) findViewById(R.id.boton_imagen);
		if (GamificarPartida) {
			Bitmap myBitmap = BitmapFactory.decodeFile(rutaPrimaria + categoriaStr + Rutas.getNivel() + 1 + rutaImagenes + categoriaStr + Rutas.getJpg());
			eligeImageView.setImageBitmap(myBitmap);
			bImagen.setEnabled(false);
			imagenSubida = true;
		}

		if (TarjetaSD.isSDConectada() && TarjetaSD.isPermisoEscritura()) {
			bAgregar.setOnClickListener(this);
			bImagen.setOnClickListener(this);
		} else {
			bImagen.setEnabled(false);
			Toast.makeText(this, R.string.error_introduce_sd, Toast.LENGTH_LONG).show();
		}

		/*
		 * Escuchador del radio grupo de Palabras Invertidas
		 */
		palabrasInvertidas = (RadioGroup) findViewById(R.id.palabras_invertidas);
		palabrasInvertidas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup rg, int id) {
				palabrasInvertidasId = id;// id es el valor que tiene el recurso en el archivo R.java
			}
		});
		/*
		 * Escuchador del menu tamaño del tablero
		 */
		spTamaño = (Spinner) findViewById(R.id.spinner_tamano_tablero);
		aaTamaño = ArrayAdapter.createFromResource(this, R.array.tamanos_tablero, android.R.layout.simple_spinner_item);
		spTamaño.setAdapter(aaTamaño);
		spTamaño.setSelection(5); // Por defecto los tablero son de 9x9
		spTamaño.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> av, View v, int posicion, long id) {
				posicionId = posicion;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		/*
		 * Escuchador de la orientaciñn de la palabra
		 */
		spOrientacion = (Spinner) findViewById(R.id.spinner_orientacion_palabras);
		aaOrientacion = ArrayAdapter.createFromResource(this, R.array.orientacion, android.R.layout.simple_spinner_item);
		spOrientacion.setAdapter(aaOrientacion);
		spOrientacion.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> av, View v, int posicion, long id) {
				orientacionId = posicion;
			}

			@Override
			public void onNothingSelected(AdapterView<?> av) {

			}
		});

		/*
		 * Escuchador del tipo de pista
		 */
		spTipoPista = (Spinner) findViewById(R.id.spinner_tipo_pista);
		aaTipoPista = ArrayAdapter.createFromResource(this, R.array.pistas, android.R.layout.simple_spinner_item);
		spTipoPista.setAdapter(aaTipoPista);
		spTipoPista.setSelection(0);
		spTipoPista.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> av, View v, int posicion, long id) {
				tipoPistaId = posicion;
			}

			@Override
			public void onNothingSelected(AdapterView<?> av) {

			}
		});

		categoria = (EditText) findViewById(R.id.editText_tema);
		if (GamificarPartida) {
			categoria.setText(categoriaStr);
			categoria.setEnabled(false);
		}

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == findViewById(R.id.boton_imagen).getId()) {

			if (categoria.getText().toString().equals("")) {
				Toast.makeText(this, R.string.error_introducir_imagen, Toast.LENGTH_LONG).show();
				return;
			}
			nombre = categoria.getText().toString();
			dialogPhoto();
		} else if (v.getId() == findViewById(R.id.boton_agregar_partida).getId()) {
			categoria = (EditText) findViewById(R.id.editText_tema);
			if (categoria.getText().toString().equals("")) {
				Toast.makeText(this, R.string.error_introduce_nombre_categoria, Toast.LENGTH_LONG).show();
				return;
			}
			if (palabrasInvertidasId == 0) {
				Toast.makeText(this, R.string.error_palabras_invertidas, Toast.LENGTH_LONG).show();
				return;

			}
			if (orientacionId == -1) {
				Toast.makeText(this, R.string.error_introduce_orientacion, Toast.LENGTH_LONG).show();
				return;
			}
			if (posicionId == -1) {
				Toast.makeText(this, R.string.error_introduce_tamano, Toast.LENGTH_LONG).show();
				return;
			}
			if (tipoPistaId == -1) {
				Toast.makeText(this, R.string.error_introduce_tipo_pista, Toast.LENGTH_LONG).show();
				return;
			}
			if (!imagenSubida) {
				Toast.makeText(this, R.string.error_introducir_imagen, Toast.LENGTH_LONG).show();
				return;
			}

			/*
			 * Ya tenemos toda la informaciñn que necesitamos para crear el archivo xml Antes que nada comprobamos que no exista ninguna partida con el mismo nombre
			 */
			if (TarjetaSD.existeCarpetaSD(Rutas.getRutaArchivos(), categoria.getText().toString())) {
				if (!GamificarPartida) {
					Toast.makeText(this, R.string.error_categoria_existente, Toast.LENGTH_LONG).show();
					return;
				}
			}
			if (!GamificarPartida) { // Es decir, estamos creando el nivel 1 del juego. Creamos las rutas

				TarjetaSD.crearRutaSD(rutaPrimaria + categoria.getText().toString() + Rutas.getNivel() + 1);
				imagenStr = rutaPrimaria + categoria.getText().toString() + Rutas.getNivel() + 1 + rutaImagenes;
				rutaArchivoPalabras = rutaPrimaria + categoria.getText().toString() + Rutas.getNivel() + 1 + Rutas.getPalabrasXml();
				rutaArchivoConfiguracion = rutaPrimaria + categoria.getText().toString() + Rutas.getNivel() + 1 + Rutas.getConfiguracionPartida();
				rutaArchivoImagenes = rutaPrimaria + categoria.getText().toString() + Rutas.getNivel() + 1 + rutaImagenes;
				nivel = 1;
				XMLParser xml = new XMLParser(this);
				rutaArchivoOpciones = rutaPrimaria + categoria.getText().toString() + Rutas.getOpcionesJuego();
				xml.escribeOpcionesJuego(rutaArchivoOpciones, 1, 1, EtiquetasXML.getNo());

			} else {
				nivel = TarjetaSD.cantidadElementosEnCarpeta(rutaPrimaria + categoria.getText().toString());
				TarjetaSD.crearRutaSD(rutaPrimaria + categoria.getText().toString() + Rutas.getNivel() + nivel);
				rutaArchivoPalabras = rutaPrimaria + categoria.getText().toString() + Rutas.getNivel() + nivel + Rutas.getPalabrasXml();
				rutaArchivoConfiguracion = rutaPrimaria + categoria.getText().toString() + Rutas.getNivel() + nivel + Rutas.getConfiguracionPartida();
				rutaArchivoImagenes = rutaPrimaria + categoria.getText().toString() + Rutas.getNivel() + nivel + rutaImagenes;
				XMLParser xml = new XMLParser(this);
				ArrayList<String> juego = xml.leeOpcionesJuego(Rutas.rutaOpcionesJuego(categoriaStr));
				rutaArchivoOpciones = rutaPrimaria + categoria.getText().toString() + Rutas.getOpcionesJuego();
				xml.escribeOpcionesJuego(rutaArchivoOpciones, Integer.parseInt(juego.get(1)) + 1, Integer.parseInt(juego.get(2)), EtiquetasXML.getSi());
				if (eTipoPista[tipoPistaId].equals(EtiquetasXML.getNodoPistaImagen()))
					TarjetaSD.crearRutaSD(rutaArchivoImagenes);
			}

			copiaPegaImagenes(direccionImagen);
			eligeImageView.setImageDrawable(null);
			eligeImageView = (ImageView) this.findViewById(R.id.agregar_partidas_imagen);

			XMLParser xml = new XMLParser(this);
			// Recogemos el contenido actual del fichero y le agregamos el nuevo a continuaciñn lo agregamos al fichero de lectura
			ArrayList<ConfiguracionPartida> arraycp = new ArrayList<ConfiguracionPartida>();
			ConfiguracionPartida cp = new ConfiguracionPartida();
			cp.setCategoria(categoria.getText().toString());
			cp.setTipoPartida("Partida");
			cp.setInvertida((palabrasInvertidasId == opcionInvertida) ? EtiquetasXML.getSi() : EtiquetasXML.getNo());
			cp.setOrientacion(eOrientaciones[orientacionId]);
			cp.setTamaño(eTamaños[posicionId]);
			cp.setTipoPista(eTipoPista[tipoPistaId]);
			arraycp.add(cp);
			Toast.makeText(this, R.string.categoria_agregada_correctamente, Toast.LENGTH_LONG).show();

			// Arrancamos la activity en la que vamos a agregar las palabras con las que se va a jugar
			Intent i = new Intent(this, NuevasPalabrasActivity.class);
			i.putExtra("tipo", cp.getTipoPista());
			i.putExtra("nombreFichero", cp.getCategoria());
			i.putExtra("tamano_maximo", eTamaños[posicionId]);
			i.putExtra("ruta", rutaArchivoPalabras);
			i.putExtra("ruta_imagenes", rutaArchivoImagenes);
			i.putExtra("nivel_juego", nivel);

			xml.escribePartidasPersonalizadas(arraycp, rutaArchivoConfiguracion);

			i.putExtra("categoria", categoria.getText().toString());
			startActivity(i);
			((Activity) this).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			((Activity) this).finish();
		}
	}

	/**
	 * Funciones que recogen las imagenes
	 * 
	 * @param
	 */
	private void dialogPhoto() {
		try {
			Imagenes.dialogoGaleriaImagenes(this);
		} catch (OutOfMemoryError e) {
			Log.i("Error Agregar Partidas -> dialogPhoto", e.getMessage());
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (requestCode == SELECT_IMAGE)
				if (resultCode == Activity.RESULT_OK) {
					Uri selectedImage = data.getData();
					direccionImagen = getPath(selectedImage);
					eligeImageView.setImageURI(selectedImage);

				}
			imagenSubida = true;
		} catch (Exception e) {
			Log.e("Error Agregar Palabras -> onActivityResult", e.getMessage());
		}
	}

	private String getPath(Uri uri) {
		String[] projection = { android.provider.MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private void copiaPegaImagenes(String ruta) {
		TarjetaSD.crearRutaSD(imagenStr);
		imagenStr += nombre + Rutas.getJpg();
		Imagenes.guardaImagenes(ruta, imagenStr);
	}

	/**
	 * Botñn atrñs
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Actividades.iniciarActividad(MainActivity.class, this);
		}
		return super.onKeyDown(keyCode, event);
	}

	/***************************************************************************************************************************/
	/** Mñtodos de las actividades */
	/***************************************************************************************************************************/

	/**
	 * Eliminamos el botñn settings de las pantallas
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.agregar_partidas, menu);
		return true;
	}
}