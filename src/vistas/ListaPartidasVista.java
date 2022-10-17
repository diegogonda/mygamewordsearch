package vistas;

import java.util.ArrayList;

import listViewPersonalizado.AdapterConfiguracionPartida;
import listViewPersonalizado.ConfiguracionPartida;
import objetos.Actividades;
import objetos.EtiquetasXML;
import objetos.Preferencias;
import objetos.Rutas;
import objetos.TarjetaSD;
import xml.XMLParser;
import actividades.AgregarPartidasActivity;
import actividades.GamasActivity;
import actividades.JuegoActivity;
import actividades.MainActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import es.uvigo.gti.mygamewordsearch.R;

public class ListaPartidasVista extends ListView implements OnClickListener {
	private ArrayList<ConfiguracionPartida> arraycp;
	private Context contexto;
	private int tamañoImagenes;
	private static String rutaArchivos = Rutas.getRutaArchivos();
	private final String archivoConfiguracion = Rutas.getConfiguracionPartida();
	private String categoria;

	public ListaPartidasVista(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.contexto = context;
		float dp = getResources().getDisplayMetrics().density;
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PistasVista, 0, 0);
		tamañoImagenes = a.getDimensionPixelSize(R.styleable.ListaPartidasVista_tamano_imagen, (int) (100 * dp));

		ListView lista = (ListView) findViewById(R.id.lista_partidas);

		// Lo aplico
		lista.setAdapter(listaPartidas());

		/***********************************************************************************************************************************************
		 * PULSACIÓN CORTA SOBRE UN ELEMENTO DEL LISTVIEW
		 *********************************************************************************************************************************************** */
		lista.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View v, int position, long id) {

				ConfiguracionPartida cp = arraycp.get(position);

				// Generamos las condiciones de juego cargando los datos de la partida
				String categoriaJuego = cp.getCategoria();
				// Antes de generar el tablero insertamos toda la información necesaria
				XMLParser xml = new XMLParser(contexto);
				ArrayList<String> config = new ArrayList<String>();
				try {
					config = xml.leeOpcionesJuego(Rutas.getRutaArchivos() + categoriaJuego + Rutas.getOpcionesJuego());

					Preferencias.setNombrePartidal(categoriaJuego, contexto);

					if (config.get(0).toString().equals(EtiquetasXML.getNo())) { // Es decir, no es una gama
						Preferencias.setNivelActual(1, contexto);
						Preferencias.setTamañoTablero(String.valueOf(cp.getTamaño()), contexto);
						Preferencias.setPalabrasInvertidas(cp.getInvertida().equals(EtiquetasXML.getSi()) ? true : false, contexto);
						Preferencias.setTipoPista(cp.getTipoPista(), contexto);

						int ori = Integer.parseInt(cp.getOrientacion());
						if (ori > 0 && ori < 6)
							Preferencias.setOrientacionPalabras(cp.getOrientacion(), contexto);
						else
							Preferencias.setOrientacionPalabras("5", contexto);

						Actividades.iniciarActividad(JuegoActivity.class, contexto);
					} else {
						categoria = arraycp.get(position).getCategoria();
						Actividades.iniciarActividad(GamasActivity.class, contexto);
					}
				} catch (Exception e) {
					Toast.makeText(contexto, R.string.error_creacion_partida, Toast.LENGTH_LONG).show();
					return;
				}
			}

		});

		/***********************************************************************************************************************************************
		 * FIN PULSACIÓN CORTA SOBRE UN ELEMENTO DEL LISTVIEW
		 *********************************************************************************************************************************************** */

		/***********************************************************************************************************************************************
		 * PULSACIÓN LARGA SOBRE UN ELEMENTO DEL LISTVIEW
		 *********************************************************************************************************************************************** */

		lista.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> av, View v, final int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
				builder.setTitle(R.string.que_haces);

				builder.setPositiveButton(R.string.eliminar_juego, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						ConfiguracionPartida cp = arraycp.get(position);
						String nombreCarpeta = Rutas.getRutaArchivos() + cp.getCategoria();
						TarjetaSD.eliminaContenidoCarpeta(nombreCarpeta);
						Actividades.iniciarActividad(MainActivity.class, contexto);
						Actividades.iniciarActividad(MainActivity.class, contexto);
					}
				});
				builder.setNegativeButton(R.string.anadir_nivel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ConfiguracionPartida cp = arraycp.get(position);
						categoria = cp.getCategoria();
						Actividades.iniciarActividadAgregarPartidasDosParametros(AgregarPartidasActivity.class, contexto, categoria, true);
					}

				});

				AlertDialog alerta = builder.create();
				alerta.show();

				return false;
			}
		});
		/***********************************************************************************************************************************************
		 * FIN PULSACIÓN LARGA SOBRE UN ELEMENTO DEL LISTVIEW
		 *********************************************************************************************************************************************** */
	}

	private AdapterConfiguracionPartida listaPartidas() {
		/**
		 * Generamos la lista con las posibilidades que apareceran en el ListView
		 */
		if (!TarjetaSD.isSDConectada() && !TarjetaSD.isPermisoEscritura()) {
			Toast.makeText(contexto, R.string.error_introduce_sd, Toast.LENGTH_LONG).show();
			return null;
		}

		if (!TarjetaSD.isSDConectada()) {
			Toast.makeText(contexto, R.string.error_introduce_sd, Toast.LENGTH_LONG).show();
			return null;
		}
		ArrayList<String> carpetas = TarjetaSD.leerCarpetasSD(Rutas.getRutaArchivos());
		arraycp = new ArrayList<ConfiguracionPartida>();

		for (String aux : carpetas) {
			// Entramos en las carpetas para recuperar el contenido del archivo configuracion_partida.xml
			String ruta = rutaArchivos + aux + Rutas.getNivel() + 1 + archivoConfiguracion;
			XMLParser xml = new XMLParser(contexto);
			ConfiguracionPartida cp = xml.leerPartidasDefinidas(ruta, tamañoImagenes, tamañoImagenes);
			arraycp.add(cp);

		}
		return new AdapterConfiguracionPartida((Activity) contexto, arraycp);
	}

	@Override
	public void onClick(View v) {

	}

	/***********************************************************************************************************************************************
	 * ALERTA PULSACIÓN LARGA SOBRE UN ELEMENTO DEL LISTVIEW
	 *********************************************************************************************************************************************** */

	/***********************************************************************************************************************************************
	 * FIN ALERTA PULSACIÓN LARGA SOBRE UN ELEMENTO DEL LISTVIEW
	 *********************************************************************************************************************************************** */

}
