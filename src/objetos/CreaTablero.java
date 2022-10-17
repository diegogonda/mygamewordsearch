package objetos;

import java.util.ArrayList;
import java.util.Vector;

import modelo.Diagonal1;
import modelo.Diagonal2;
import modelo.Horizontal;
import modelo.QuickSort;
import modelo.Vertical;
import xml.XMLParser;
import actividades.MainActivity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import es.uvigo.gti.mygamewordsearch.R;

public class CreaTablero {
	/**
	 * @param args
	 */
	private static int tamanoTablero;
	private static char letraRelleno = (char) 47;
	private Vector<String> Palabras;

	/**
	 * Valores de juego
	 */

	private static char[][] tableroJuego;
	private int intentos = 0;
	private int intentosMaximos = 100;
	private int cantidadPalabras;
	private boolean invierte; // true inverte algunas palabras, false no
	private int opciones; // = 1 s�lo horizontal, 2 hor + vertical, 3 hor + ver + diagonal1, 4 todos

	// Objeto Posiciones de las palabras
	private ArrayList<PosicionesPalabras> pp;
	private int contador;

	// Datos que guardar� el objeto Posiciones de las palabras
	private static int xInicial;
	private static int yInicial;
	private static int xFinal;
	private static int yFinal;
	private int xVectores[];
	private int yVectores[];
	private String palabraGuardada;

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
		 * Ordenamos las palabras de mayor a menor tamano
		 * */
		Palabras = new Vector<String>();
		for (PosicionesPalabras aux : pp) {
			Palabras.add(aux.getPalabra());
			aux.setMostradaPantalla(false);
		}
		QuickSort QS = new QuickSort(Palabras);
		Palabras = QS.devuelveVectorOrdenado();
		/**
		 * Generamos un Tablero de Juego
		 * */
		tableroJuego = new char[tamanoTablero][tamanoTablero];
		/**
		 * Generamos el objeto que contendra la informacion de todas las palabras del tablero
		 * */
		contador = 0;
		/**
		 * Por defecto el tablero siempre va a aceptar palabras en Horizontal, mientras que Vertical, Diagonales y palabras del rev�s s�lo si lo pide el jugador.
		 * 
		 * La primera palabra cumplir� las siguientes caracter�stica: 1.- Su colocacion ser� siempre en Horizontal. 2.- Ser� la primera palabra del Vector Palabras, es decir, la m�s larga
		 * */
		rellenaTablero();
		/**
		 * Las clases Horizontal, Diagonal1 y 2 y Vertical Colocan las palabras aleatoriamente en los espacios del tablero. Si si quiere introducir la palabra de rev�s... hay que mand�rsela del rev�s
		 * */
		try {
			boolean seguir = true;
			while (seguir) {
				boolean invierto = false;
				for (int i = 0; i < Palabras.size(); i++) {
					invierto = false;
					String palabra = Palabras.get(i);
					palabra = eliminaTildes(palabra);

					xVectores = new int[palabra.length()];
					yVectores = new int[palabra.length()];

					int aleatorio = 0;
					if (opciones == 1 || opciones == 2)
						aleatorio = opciones - 1;
					else if (opciones == 3)
						aleatorio = (int) (Math.random() * 2);
					else if (opciones == 4)
						aleatorio = (int) (Math.random() * 2) + 2;
					else if (opciones == 5)
						aleatorio = (int) (Math.random() * opciones);

					if (invierte) {
						if ((int) (Math.random() * 2) == 0) {// Invierte la palabra
							String str = inviertePalabra(palabra);
							invierto = true;
							palabra = str;
						}
					}
					boolean resultado = false;
					switch (aleatorio) {
					case 0:
						resultado = horizontal(palabra);
						break;
					case 1:
						resultado = vertical(palabra);
						break;
					case 2:
						resultado = diagonal1(palabra);
						break;
					case 3:
						resultado = diagonal2(palabra);
						break;
					}
					if (!resultado) {
						intentos++;
						if (intentos == intentosMaximos) {
							// palabrasSobrantes.add(palabra);
							break;
						}
						i--;
					} else {
						palabraGuardada = Palabras.get(i);
						setPosicionesPalabras(aleatorio, invierto);
						intentos = 0;
						contador++;
					}
				}
				if ((contador >= minimoPalabrasTablero(tamanoTablero) && contador <= maximoPalabrasTablero(tamanoTablero)) || (contador == Palabras.size())) {
					seguir = false;
				} else {
					rellenaTablero();
					contador = 0;

				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		/*
		 * Como puede haber palabras que no entraron el tablero, procedemos a eliminarlas de la variable PosicionesPalabras pp
		 */
		eliminaLasPalabrasSobrantes();
		completaTablero();
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

	private void setPosicionesPalabras(int orientacion, boolean invierte) {
		// PosicionesPalabras aux = new PosicionesPalabras();
		int i = 0;
		for (PosicionesPalabras aux : pp) {
			if (aux.getPalabra().equals(palabraGuardada)) {
				aux.setxTableroInicial(xInicial); // x.- Vector x donde cuadra la letra que inicia la palabra
				aux.setyTableroInicial(yInicial); // y.- Vector y donde cuadra la letra que inicia la palabra
				aux.setxTableroFinal(xFinal); // x.- Vector x donde cuadra la letra que finaliza la palabra
				aux.setyTableroFinal(yFinal); // y.- Vector y donde cuadra la letra que finaliza la palabra
				aux.setElegida(false); // Inicializamos como palabra no seleccionada
				aux.setMostradaPantalla(true); // Inicializamos como palabra mostrada
				aux.setOrientacion(orientacion); // Guardamos la orientacion de la palabra
				aux.setInvertida(invierte); // Si est� invertida en el tablero
				aux.setxTableroLetras(xVectores); // Los vectores del tablero por donde pasa en x
				aux.setyTableroLetras(yVectores); // Los vectores del tablero por donde pasa en y
				pp.set(i, aux); // Guardamos la variable PosicionesPalabras que almacena todo el contenido
				break;
			}
			i++;

		}

	}

	private String inviertePalabra(String palabra) {
		StringBuilder builder = new StringBuilder(palabra);
		String sCadenaInvertida = builder.reverse().toString();
		return sCadenaInvertida;
	}

	static boolean horizontal(String palabra) {
		Horizontal H = new Horizontal(palabra, tableroJuego, tamanoTablero, letraRelleno);
		tableroJuego = H.devuelveTableroJuego();
		if (H.resultado()) {
			xInicial = H.getxInicial();
			yInicial = H.getyInicial();
			xFinal = H.getxFinal();
			yFinal = H.getyFinal();

			return true;
		}
		return false;

	}

	static boolean vertical(String palabra) {
		Vertical V = new Vertical(palabra, tableroJuego, tamanoTablero, letraRelleno);
		tableroJuego = V.devuelveTableroJuego();
		if (V.resultado()) {
			xInicial = V.getxInicial();
			yInicial = V.getyInicial();
			xFinal = V.getxFinal();
			yFinal = V.getyFinal();

			return true;
		}
		return false;

	}

	static boolean diagonal1(String palabra) {
		Diagonal1 D1 = new Diagonal1(palabra, tableroJuego, tamanoTablero, letraRelleno);
		tableroJuego = D1.devuelveTableroJuego();
		if (D1.resultado()) {
			xInicial = D1.getxInicial();
			yInicial = D1.getyInicial();
			xFinal = D1.getxFinal();
			yFinal = D1.getyFinal();

			return true;
		}
		return false;

	}

	static boolean diagonal2(String palabra) {
		Diagonal2 D2 = new Diagonal2(palabra, tableroJuego, tamanoTablero, letraRelleno);
		tableroJuego = D2.devuelveTableroJuego();
		if (D2.resultado()) {
			xInicial = D2.getxInicial();
			yInicial = D2.getyInicial();
			xFinal = D2.getxFinal();
			yFinal = D2.getyFinal();

			return true;
		}
		return false;
	}

	static void completaTablero() {
		for (int i = 0; i < tamanoTablero; i++) {
			for (int j = 0; j < tamanoTablero; j++) {
				if (tableroJuego[i][j] == letraRelleno)
					tableroJuego[i][j] = (char) (Math.random() * 26 + 97);
			}
		}
	}

	static void rellenaTablero() {
		for (int i = 0; i < tamanoTablero; i++) {
			for (int j = 0; j < tamanoTablero; j++) {
				tableroJuego[i][j] = letraRelleno;
			}
		}
	}

	private void cargaValoresJuego() {
		tamanoTablero = Integer.valueOf(Preferencias.getTamanoTablero(contexto));
		tipoPista = Preferencias.getTipoPista(contexto);

		invierte = Preferencias.isPalabrasInvertidas(contexto);
		opciones = Integer.valueOf(Preferencias.getOrientacionPalabras(contexto));

	}

	/**
	 * Funciones separadas de CreaTablero Se encargan de controlar qe las palabras que se van a utilizar en el tablero cumplan los requisitos para que no ocurran errores en tiempo de ejecucion
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
				 * Creamos un fichero posiciones_palabras.xml s�lo con la informacion que tenemos Para evitar errores en la creacion, los valores que todav�a no se han hallado los incializaremos a -1
				 * El fichero posiciones_palabras.xml contendr� la informacion de la partida en juego
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
		tamanoTablero = Integer.valueOf(Preferencias.getTamanoTablero(contexto));
		/*
		 * Contamos cu�ntas palabras caben en nuestro tablero, si son cero, indicamos mediante un mensaje Toast que no se puede generar el tablero requerido
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
		 * Eliminamos aquellas palabras que no caben en el tablero por su tamano
		 */
		ajusteTamanoPalabras(tamanoPP);

		return true;
	}

	private void eliminaLasPalabrasSobrantes() {
		for (PosicionesPalabras aux : pp) {
			if (!aux.getMostradaPantalla())
				pp.remove(aux);
		}
		XMLParser x = new XMLParser(contexto);
		x.escribePosicionesPalabras(pp);

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

	private int maximoPalabrasTablero(int tamanoTablero) {
		switch (tamanoTablero) {
		case 4:
			return 4;
		case 5:
			return 5;
		case 6:
			return 6;
		case 7:
			return 7;
		case 8:
			return 8;
		case 9:
			return 9;
		case 10:
			return 11;
		case 11:
			return 12;
		case 12:
			return 14;
		default:
			return 4;
		}
	}

	private int minimoPalabrasTablero(int tamanoTablero) {
		switch (tamanoTablero) {
		case 4:
			return 3;
		case 5:
			return 4;
		case 6:
			return 5;
		case 7:
			return 6;
		case 8:
			return 7;
		case 9:
			return 8;
		case 10:
			return 9;
		case 11:
			return 10;
		case 12:
			return 11;
		default:
			return 4;
		}
	}

	/**
	 * Funcion que determina si en una cadena de caracteres hay o no caracteres con tilde, de haberlos los elimina
	 * 
	 * @param palabra
	 * @return palabra
	 */
	private String eliminaTildes(String palabra) {

		palabra = palabra.replace('á', 'a');
		palabra = palabra.replace('à', 'a');
		palabra = palabra.replace('ä', 'a');
		palabra = palabra.replace('â', 'a');

		palabra = palabra.replace('é', 'e');
		palabra = palabra.replace('è', 'e');
		palabra = palabra.replace('ë', 'e');
		palabra = palabra.replace('ê', 'e');

		palabra = palabra.replace('í', 'i');
		palabra = palabra.replace('ì', 'i');
		palabra = palabra.replace('ï', 'i');
		palabra = palabra.replace('î', 'i');

		palabra = palabra.replace('í', 'o');
		palabra = palabra.replace('î', 'o');
		palabra = palabra.replace('ï', 'o');
		palabra = palabra.replace('î', 'o');

		palabra = palabra.replace('ú', 'u');
		palabra = palabra.replace('û', 'u');
		palabra = palabra.replace('ü', 'u');
		palabra = palabra.replace('û', 'u');

		return palabra;
	}

}
