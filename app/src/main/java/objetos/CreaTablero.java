package objetos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dominio.ConfiguracionJuego;
import dominio.GeneradorTablero;
import dominio.ModoOrientacion;
import dominio.PosicionPalabra;
import dominio.Tablero;
import dominio.TableroException;
import dominio.TableroGenerado;
import xml.XMLParser;
import actividades.MainActivity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import es.uvigo.gti.mygamewordsearch.R;

public class CreaTablero {

	private int tamanoTablero;
	private char[][] tableroJuego;
	private int cantidadPalabras;
	private boolean invierte; // true inverte algunas palabras, false no
	private String opciones; // valor de la preferencia de orientación

	// Objeto Posiciones de las palabras
	private ArrayList<PosicionesPalabras> pp;

	XMLParser xml;

	private Context contexto;
	private String tipoPista;

	public CreaTablero(Context contexto) {
		this.contexto = contexto;
	}

	public CreaTablero(Context c, ArrayList<PosicionesPalabras> p) {
		this.contexto = c;
		cargaValoresJuego();

		pp = p;

		/**
		 * Las palabras de la partida. El motor se encarga de escribirlas sin
		 * tildes, porque el tablero sólo tiene las 26 letras del abecedario. La
		 * palabra que ve el jugador es la de la lista, con sus tildes.
		 * */
		List<String> palabras = new ArrayList<String>();
		for (PosicionesPalabras aux : pp) {
			palabras.add(aux.getPalabra());
		}

		ConfiguracionJuego configuracion = new ConfiguracionJuego(tamanoTablero, ModoOrientacion.desdeValor(opciones), invierte);

		TableroGenerado generado = generaTablero(configuracion, palabras);
		AdaptadorTablero.TableroPreparado preparado = AdaptadorTablero.adapta(generado, pp);

		tableroJuego = preparado.getLetras();
		pp = new ArrayList<PosicionesPalabras>(preparado.getPalabras());
		cantidadPalabras = pp.size();

		/**
		 * Como puede haber palabras que no han entrado en el tablero, en el
		 * fichero de la partida en juego se guardan sólo las que sí.
		 * */
		xml = new XMLParser(contexto);
		xml.escribePosicionesPalabras(pp);
	}

	public int getCantidadPalabras() {
		return cantidadPalabras;
	}

	public ArrayList<PosicionesPalabras> getPp() {
		return pp;
	}

	public char[][] getTableroJuego() {
		return tableroJuego;
	}

	/**
	 * Genera el tablero de la partida con el motor de tableros.
	 *
	 * Si el motor no consigue un tablero con un número razonable de palabras,
	 * se usa el mejor tablero parcial que ha encontrado: es mejor jugar con un
	 * tablero incompleto que dejar la aplicación colgada.
	 */
	private TableroGenerado generaTablero(ConfiguracionJuego configuracion, List<String> palabras) {
		try {
			return new GeneradorTablero().genera(configuracion, palabras);
		} catch (TableroException e) {
			Log.w("CreaTablero -> generaTablero", " " + e.getMessage());
			TableroGenerado parcial = e.getTableroParcial();
			if (parcial != null) {
				return parcial;
			}
			return tableroSinPalabras(configuracion.getTamanoTablero());
		}
	}

	/**
	 * Tablero con letras al azar y sin ninguna palabra, para el caso de que el
	 * motor no haya podido colocar ni una.
	 */
	private TableroGenerado tableroSinPalabras(int tamano) {
		Tablero tablero = new Tablero(tamano);
		Random aleatorio = new Random();
		for (int y = 0; y < tamano; y++) {
			for (int x = 0; x < tamano; x++) {
				tablero.setCelda(x, y, (char) (aleatorio.nextInt(26) + 'a'));
			}
		}
		return new TableroGenerado(tablero, new ArrayList<PosicionPalabra>());
	}

	private void cargaValoresJuego() {
		tamanoTablero = Integer.valueOf(Preferencias.getTamañoTablero(contexto));
		tipoPista = Preferencias.getTipoPista(contexto);

		invierte = Preferencias.isPalabrasInvertidas(contexto);
		opciones = Preferencias.getOrientacionPalabras(contexto);

	}

	/**
	 * Funciones separadas de CreaTablero Se encargan de controlar qe las palabras que se van a utilizar en el tablero cumplan los requisitos para que no ocurran errores en tiempo de ejecución
	 *
	 * @param palabras
	 * @param categoriaJuego
	 * @return
	 */
	public boolean generaPartidaRapida(String categoriaJuego) {
		try {
			cargaValoresJuego();

			XMLParser xml = new XMLParser(contexto);

			if (!tipoPista.equals(EtiquetasXML.getNodoPistaDescripcion())) {
				pp = xml.getPalabrasPersonalizadasTemas(Rutas.getRutaArchivos() + categoriaJuego + Rutas.getPalabrasXml());
				int i = 0;
				for (PosicionesPalabras aux : pp) {
					aux.setPistaCategoria(categoriaJuego);
					pp.set(i++, aux);
				}
			} else {
				String ruta = Rutas.getRutaArchivos() + categoriaJuego + Rutas.getPalabrasXml();
				xml.getPalabrasDescripcionesPersonalizadasTemas(ruta);
				pp = xml.getPp();
			}

			if (funcionControlTamanos(pp.size())) {
				/**
				 * Creamos un fichero posiciones_palabras.xml sólo con la información que tenemos Para evitar errores en la creación, los valores que todavía no se han hallado los incializaremos a -1
				 * El fichero posiciones_palabras.xml contendrá la información de la partida en juego
				 */
				xml = new XMLParser(contexto);
				xml.escribePosicionesPalabras(pp);

				return true;
			}
		} catch (Exception e) {
			Toast.makeText(contexto, "error al generar la partida", Toast.LENGTH_LONG).show();
			Actividades.iniciarActividad(MainActivity.class, contexto);
			Log.e("CreaTablero -> generaPartidaRapida", " " + e.getMessage());
			return false;
		}

		return false;
	}

	private boolean funcionControlTamanos(int tamanoPP) {
		tamanoTablero = Integer.valueOf(Preferencias.getTamañoTablero(contexto));
		/*
		 * Contamos cuántas palabras caben en nuestro tablero, si son cero, indicamos mediante un mensaje Toast que no se puede generar el tablero requerido
		 */
		int contadorPalabras = 0;
		int i = 0;
		for (PosicionesPalabras aux : pp) {

			if (aux.getPalabra().length() <= tamanoTablero)
				contadorPalabras++;
			if (++i == tamanoPP && contadorPalabras == 0) {
				Toast.makeText(contexto, contexto.getResources().getText(R.string.error_tablero_pequeno), Toast.LENGTH_LONG).show();
				return false;
			}
		}

		/*
		 * Eliminamos aquellas palabras que no caben en el tablero por su tamaño
		 */
		ajusteTamanoPalabras(tamanoPP);

		return true;
	}

	private void ajusteTamanoPalabras(int tamanoPP) {
		ArrayList<PosicionesPalabras> ppCopias = new ArrayList<PosicionesPalabras>();

		for (PosicionesPalabras aux : pp) {
			String str = aux.getPalabra();
			if (tamanoTablero >= str.length()) {
				ppCopias.add(aux);
			}
		}
		pp = ppCopias;
	}

}
