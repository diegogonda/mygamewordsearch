package actividades;

import java.util.ArrayList;

import objetos.Actividades;
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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import es.uvigo.gti.mygamewordsearch.R;

public class NuevasPalabrasActivity extends Activity implements OnClickListener {
	private Button bAñadir;
	private Button bTerminar;
	private Button bImagen;
	private Button bNueva;
	private int panel = -1;
	private EditText ePalabra;
	private EditText eDescripcion;
	private ArrayList<String> arrayPalabras;
	private ArrayList<String> arrayDescripcion;
	private String nombreFichero;
	/**
	 * Variables de los ficheros de Imágenes
	 */
	private String rutaImagenes = Rutas.getRutaArchivos();
	private String direccionImagen;
	private ImageView eligeImageView;
	private int SELECT_IMAGE = Imagenes.getSELECT_IMAGE();
	private boolean imagenSubida = false;
	private String rutaImagen;
	private static String categoria;

	/**
	 * ListView de las palabras agregadas
	 */
	private ListView listView;
	private String rutaCarpetaImagenes;
	private int nivel;
	private boolean seguirAñadiendo = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Idioma.CambiaIdioma(this); // Cambiamos el idioma de la aplicación
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Bundle bundle = getIntent().getExtras();
		String str = bundle.getString("tipo");
		String tamano = bundle.getString("tamano_maximo");
		categoria = bundle.getString("categoria");
		nombreFichero = bundle.getString("ruta");
		rutaCarpetaImagenes = bundle.getString("ruta_imagenes");
		nivel = bundle.getInt("nivel_juego", 1);
		

		ePalabra = new EditText(this);

		if (str.equals("nombre") || str.equals("pista_audio") || str.equals("pista_categoria") || str.equals("pista_marca") || str.equals("sin_pista")) {
			setContentView(R.layout.activity_nuevas_palabras);

			ePalabra = (EditText) findViewById(R.id.editText_nueva_palabra);
			bAñadir = (Button) findViewById(R.id.boton_anadir_nueva_palabra);
			bTerminar = (Button) findViewById(R.id.boton_terminar_nueva_palabra);
			bNueva = (Button) findViewById(R.id.boton_agregar_mas_niveles);

			panel = 1;

		} else if (str.equals("pista_descripcion")) {
			setContentView(R.layout.activity_nuevas_palabras_descripcion);

			ePalabra = (EditText) findViewById(R.id.editText_nueva_palabra_descripcion);
			bAñadir = (Button) findViewById(R.id.boton_anadir_nueva_palabra_descripcion);
			bTerminar = (Button) findViewById(R.id.boton_terminar_nueva_palabra_descripcion);
			bNueva = (Button) findViewById(R.id.boton_agregar_mas_niveles_descripcion);
			arrayDescripcion = new ArrayList<String>();
			panel = 2;

		} else if (str.equals("pista_imagen")) {
			setContentView(R.layout.activity_nuevas_palabras_imagen);

			ePalabra = (EditText) findViewById(R.id.editText_nuevas_palabras_imagen);
			bAñadir = (Button) findViewById(R.id.boton_anadir_nueva_palabra_imagen);
			bTerminar = (Button) findViewById(R.id.boton_terminar_nueva_palabra_imagen);
			bImagen = (Button) findViewById(R.id.boton_anadir_imagen_imagen);
			eligeImageView = (ImageView) this.findViewById(R.id.imageView_anadir_nueva_palabra);
			bNueva = (Button) findViewById(R.id.boton_agregar_mas_niveles_imagenes);

			rutaImagenes += "/";
			panel = 3;

		} else {
			Toast.makeText(this, R.string.error_opcion_incorrecta, Toast.LENGTH_LONG).show();
			Actividades.iniciarActividad(MainActivity.class, this);
		}
		if (panel != -1) {
			/**
			 * Limitamos el tamaño del edittext al tamaño del tablero, cualquier palabra mayor no se podrá escribir
			 */
			int maxLength = Integer.parseInt(tamano);
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(maxLength);
			ePalabra.setFilters(FilterArray);
			arrayPalabras = new ArrayList<String>();
		}
		if (TarjetaSD.isSDConectada()) {
			TarjetaSD.crearRutaSD(rutaImagenes);
			bAñadir.setOnClickListener(this);
			bTerminar.setOnClickListener(this);
			bNueva.setOnClickListener(this);
			if (panel == 3)
				bImagen.setOnClickListener(this);
		} else {
			bImagen.setEnabled(false);
			Toast.makeText(this, R.string.error_introduce_sd, Toast.LENGTH_LONG).show();
		}

		listView = (ListView) findViewById(R.id.listView_palabras_agregadas);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nuevas_palabras, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (panel == 1) {
			panelUno(v);
		} else if (panel == 2) {
			panelDos(v);
		} else if (panel == 3) {
			/*
			 * Botón imagen
			 */
			if (v.getId() == findViewById(R.id.boton_anadir_imagen_imagen).getId()) {
				if (ePalabra.getText().toString().equals("")) {
					Toast.makeText(this, R.string.error_introducir_imagen, Toast.LENGTH_LONG).show();
					return;
				}
				Imagenes.dialogoGaleriaImagenes(this); // Creamos una llamada a la galería
			}
			panelTres(v);
		}
	}

	/************************************************************************************************************************************/
	/* CONFIGURACIONES DE LOS PANELES */
	/************************************************************************************************************************************/

	/**
	 * EL PANEL UNO ES EL QUE CONTIENE SOLO EL EDITTEXT PARA AÑADIR PALABRAS
	 */
	private void panelUno(View v) {
		if (v.getId() == findViewById(R.id.boton_anadir_nueva_palabra).getId()) {
			if (EditTextVacio(ePalabra))
				Toast.makeText(this, R.string.error_rellenar_campo, Toast.LENGTH_LONG).show();
			else
				agregaPalabras(ePalabra.getText().toString());
		} else if (v.getId() == findViewById(R.id.boton_terminar_nueva_palabra).getId()) {
			if (!EditTextVacio(ePalabra)) {
				if (agregaPalabras(ePalabra.getText().toString()))
					AgregarPalabrasXml();
			} else if (!estaArrayPalabrasVacio()) {
				AgregarPalabrasXml();
			} else {
				Toast.makeText(this, R.string.error_anadir_una_palabra, Toast.LENGTH_LONG).show();
			}
		} else if (v.getId() == findViewById(R.id.boton_agregar_mas_niveles).getId()) {
			if (!EditTextVacio(ePalabra)) {
				if (agregaPalabras(ePalabra.getText().toString())) {
					seguirAñadiendo = true;
					AgregarPalabrasXml();
				}
			} else if (!estaArrayPalabrasVacio()) {
				seguirAñadiendo = true;
				AgregarPalabrasXml();
			} else {
				Toast.makeText(this, R.string.error_anadir_una_palabra, Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * EL PANEL DOS ES EL QUE CONTIENE LAS PALABRAS Y LAS DESCRIPCIONES
	 */
	private void panelDos(View v) {
		eDescripcion = (EditText) findViewById(R.id.editText_nueva_palabra_descripcion_descripcion);
		if (v.getId() == findViewById(R.id.boton_anadir_nueva_palabra_descripcion).getId()) {
			if (EditTextVacio(ePalabra) || EditTextVacio(eDescripcion)) {
				Toast.makeText(this, R.string.error_rellenar_ambos_campos, Toast.LENGTH_LONG).show();
			} else {
				if (agregaPalabras(ePalabra.getText().toString()))
					agregaDescripciones(eDescripcion.getText().toString());
			}
		} else if (v.getId() == findViewById(R.id.boton_terminar_nueva_palabra_descripcion).getId()) {
			if (!EditTextVacio(ePalabra) && !EditTextVacio(eDescripcion)) { // Es decir, si ambos tienen contenido
				if (agregaPalabras(ePalabra.getText().toString())) {
					agregaDescripciones(eDescripcion.getText().toString());
					AgregarPalabraDescripcionXml();
				}
			} else if (!estaArrayPalabrasVacio() && !estaArrayDescripcionesVacio()) { // Es decir, se introdujo al menos una palabra y una descripcion
				AgregarPalabraDescripcionXml();
			} else {
				Toast.makeText(this, R.string.error_anadir_una_palabra, Toast.LENGTH_LONG).show();
			}
		} else if (v.getId() == findViewById(R.id.boton_agregar_mas_niveles_descripcion).getId()) {
			if (!EditTextVacio(ePalabra) && !EditTextVacio(eDescripcion)) { // Es decir, si ambos tienen contenido
				if (agregaPalabras(ePalabra.getText().toString())) {
					seguirAñadiendo = true;
					agregaDescripciones(eDescripcion.getText().toString());
					AgregarPalabraDescripcionXml();
				}
			} else if (!estaArrayPalabrasVacio() && !estaArrayDescripcionesVacio()) { // Es decir, se introdujo al menos una palabra y una descripcion
				seguirAñadiendo = true;
				AgregarPalabraDescripcionXml();
			} else {
				Toast.makeText(this, R.string.error_anadir_una_palabra, Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * EL PANEL TRES ES EL QUE CONTIENE LAS PALABRAS Y LAS IMAGENES
	 */
	private void panelTres(View v) {
		if (v.getId() == findViewById(R.id.boton_anadir_nueva_palabra_imagen).getId()) {
			if (EditTextVacio(ePalabra) || !imagenSubida) {
				Toast.makeText(this, R.string.error_rellenar_campo_text_imagen, Toast.LENGTH_LONG).show();
			} else {
				copiaPegaImagenes(direccionImagen);
				if (agregaPalabras(ePalabra.getText().toString()))
					restauraImagenOriginal();
			}
		} else if (v.getId() == findViewById(R.id.boton_terminar_nueva_palabra_imagen).getId()) {
			if (!EditTextVacio(ePalabra) && imagenSubida) { // Es decir, ambos tienen contenido
				copiaPegaImagenes(direccionImagen);
				if (agregaPalabras(ePalabra.getText().toString()))
					AgregarPalabrasXml();

			} else if (!estaArrayPalabrasVacio()) {
				AgregarPalabrasXml();
			} else {
				Toast.makeText(this, R.string.error_anadir_una_palabra, Toast.LENGTH_LONG).show();
			}
		} else if (v.getId() == findViewById(R.id.boton_agregar_mas_niveles_imagenes).getId()) {
			if (!EditTextVacio(ePalabra) && imagenSubida) { // Es decir, ambos tienen contenido
				copiaPegaImagenes(direccionImagen);
				if (agregaPalabras(ePalabra.getText().toString())){
					seguirAñadiendo = true;
					AgregarPalabrasXml();
				}

			} else if (!estaArrayPalabrasVacio()) {
				seguirAñadiendo = true;
				AgregarPalabrasXml();
			} else {
				Toast.makeText(this, R.string.error_anadir_una_palabra, Toast.LENGTH_LONG).show();
			}
		}
	}

	/************************************************************************************************************************************/
	/* / CONFIGURACIONES DE LOS PANELES */
	/************************************************************************************************************************************/
	/**
	 * Función que guarda las palabras que vamos agregando al juego y actualiza el contenido del ListView
	 * 
	 * @param palabra
	 *            Palabra que vamos a guardar para en un futuro poder jugar con ella
	 */
	private boolean agregaPalabras(String palabra) {
		// Primero comprobamos que la palabra no exista dentro del array
		for (String aux : arrayPalabras)
			if (aux.equals(palabra)) {
				Toast.makeText(this, "Error. La palabra ya existe", Toast.LENGTH_LONG).show();
				return false;
			}
		arrayPalabras.add(palabra);
		ePalabra.setText("");
		ActualizaListView();
		return true;
	}

	private void agregaDescripciones(String descripcion) {
		arrayDescripcion.add(descripcion);
		eDescripcion.setText("");
	}

	private boolean estaArrayPalabrasVacio() {
		return (arrayPalabras.size() == 0) ? true : false;
	}

	private boolean estaArrayDescripcionesVacio() {
		return (arrayDescripcion.size() == 0) ? true : false;
	}

	private void restauraImagenOriginal() {
		imagenSubida = false;
		// Vaciamos las imégenes de la mamoria antes de insertar la imagen marco, para evitar que ocurra un problema en tiempo de ejecucion
		Drawable drawable = eligeImageView.getDrawable();
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			Bitmap bitmap = bitmapDrawable.getBitmap();
			bitmap.recycle();
		}
		eligeImageView.setImageResource(R.drawable.marco);

	}

	private boolean EditTextVacio(EditText e) {
		return (e.getText().toString().equals("")) ? true : false;
	}

	/**
	 * Funciones que guardan las imágenes en la carpeta especificada
	 */

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
		rutaImagen = rutaCarpetaImagenes + ePalabra.getText().toString() + Rutas.getJpg();
		if (!Imagenes.guardaImagenes(ruta, rutaImagen))
			Toast.makeText(this, R.string.error_copia_imagen, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// Esto es lo que hace mi botón al pulsar ir a atrás
			Toast.makeText(this, R.string.pulsa_boton_terminar, Toast.LENGTH_LONG).show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Eliminamos el botón settings de las pantallas
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	/***********************************************************************************************************************************************
	 * FUNCIONES QUE AGREGAN LAS PALABRAS INTRODUCIDAS EN EL CORRESPONDIENTE ARCHIVO XML
	 *********************************************************************************************************************************************** */
	/**
	 * Funcion que agrega las palabras. Se usa para agregar las palabras cuando la pista es nombre, imagen, marca, sonido y sin pista
	 * */
	private void AgregarPalabrasXml() {
		XMLParser xml = new XMLParser(this);
		xml.escribePalabrasPersonalizadas(nombreFichero, arrayPalabras);
		if (seguirAñadiendo) {
			Actividades.iniciarActividadAgregarPartidasDosParametros(AgregarPartidasActivity.class, this, categoria, true);
		} else {
			Actividades.iniciarActividad(MainActivity.class, this);
		}

	}

	/**
	 * Funcion que agrega las palabras y descripciones. Se usa para agregar las palabras cuando la pista es descripcion
	 * */
	private void AgregarPalabraDescripcionXml() {
		XMLParser xml = new XMLParser(this);
		xml.escribePalabrasDescripcionPersonalizadas(nombreFichero, arrayPalabras, arrayDescripcion);
		if (seguirAñadiendo) {
			Actividades.iniciarActividadAgregarPartidasDosParametros(AgregarPartidasActivity.class, this, categoria, true);
		} else {
			Actividades.iniciarActividad(MainActivity.class, this);
		}
	}

	/***********************************************************************************************************************************************
	 * PULSACIÓN LARGA SOBRE UN ELEMENTO DEL LISTVIEW
	 *********************************************************************************************************************************************** */
	/**
	 * Habilitamos el escuchador para que cuando se realice una pulsación larga se pueda modificar o eliminar una palabra de la lista agregada
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.listView_palabras_agregadas) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(arrayPalabras.get(info.position));
			String[] menuItems = getResources().getStringArray(R.array.menu);
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	}

	/**
	 * Habilitamos el método que nos permite determinar si se seleccionó borrar o modificar
	 */

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int menuItemIndex = item.getItemId();
		String[] menuItems = getResources().getStringArray(R.array.menu);
		String menuItemName = menuItems[menuItemIndex];
		if (menuItemName.equals("Delete") || menuItemName.equals("Borrar") || menuItemName.equals("Supprimer") || menuItemName.equals("Entfernen") || menuItemName.equals("Chiaro")) {
			arrayPalabras.remove(info.position);
			ActualizaListView();

		} else if (menuItemName.equals("Edit") || menuItemName.equals("Editar") || menuItemName.equals("Modifier") || menuItemName.equals("Veränderung") || menuItemName.equals("Modifica")) {
			ePalabra.setText(arrayPalabras.get(info.position));
			if (panel == 2) {
				eDescripcion.setText(arrayDescripcion.get(info.position));
				arrayDescripcion.remove(info.position);
			}
			if (panel == 3) {
				String ruta = Rutas.getImagenesPalabras(categoria + Rutas.getNivel() + nivel + "", arrayPalabras.get(info.position));
				// Tenemos que reducir el tamaño del Bitmap para evitar que dé
				// problemas a la hora de cerrar la actividad. Reducimos de 8/1
				Bitmap myBitmap = BitmapFactory.decodeFile(ruta);

				eligeImageView.setImageBitmap(myBitmap);
				imagenSubida = true;
			}
			arrayPalabras.remove(info.position);
			ActualizaListView();
		}
		return true;
	}

	/**
	 * 
	 */
	public void ActualizaListView() {
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayPalabras);
		listView.setAdapter(adaptador);
		registerForContextMenu(listView);
	}
	/***********************************************************************************************************************************************
	 * FIN PULSACIÓN LARGA SOBRE UN ELEMENTO DEL LISTVIEW
	 *********************************************************************************************************************************************** */
}
