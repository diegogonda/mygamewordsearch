package xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Vector;
import listViewPersonalizado.ConfiguracionPartida;
import objetos.EtiquetasXML;
import objetos.Imagenes;
import objetos.PosicionesPalabras;
import objetos.Rutas;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;
import es.uvigo.gti.mygamewordsearch.R;

public class XMLParser {
	/**
	 * Variables que nos permitirán abrir los ficheros xml
	 */
	private static FileOutputStream fout;
	private static FileInputStream fin;
	private static Context contexto;
	private static ArrayList<PosicionesPalabras> pp;
	private static String posicionesPalabras = Rutas.getPosicionesPalabras();
	private static String partidaJuego = Rutas.getPartidaJuego();
	private static String rutaSharedPreferences = Rutas.getRutaArchivoPreferencias() + "_preferencias";
	private static String xmlTexto = EtiquetasXML.getXml();

	public XMLParser(Context c) {
		contexto = c;
	}

	/**
	 * MÉTODOS ENCARGADOS DE GENERAR Y LEER EL ARCHIVO /nombre_categoria/categoria/opciones_juego.xml
	 */
	public void escribeOpcionesJuego(String ruta, int numeroNiveles, int ultimoSuperado, String gama) {
		try {
			XmlSerializer serializer = serializerEscrituraTarjetaSD(ruta);
			serializer.startTag(null, EtiquetasXML.getOpcionesJuego());

			agregaEtiqueta(serializer, EtiquetasXML.getGamificacion(), gama);
			agregaEtiqueta(serializer, EtiquetasXML.getNumeroNiveles(), String.valueOf(numeroNiveles));
			agregaEtiqueta(serializer, EtiquetasXML.getUltimoNivelSuperado(), String.valueOf(ultimoSuperado));

			serializer.endTag(null, EtiquetasXML.getOpcionesJuego());
			serializer.endDocument();
			fout.close();
		} catch (IOException e) {
			Log.e("XMLParserE -> escribeOpcionesJuego:", e.getMessage());
		}
	}

	/**
	 * Devuelve el contenido del fichero /nombre_categoria/categoria/opciones_juego.xml a través de un arrayList en el que se guardan la información la siguiente estructura 1.- gamificación: ¿Es gama
	 * o no? 2.- numero niveles: ¿Cuántos niveles hay en la categoria? 3.- ultimo nivel ganado: ¿Cuál es el último nivel superado?
	 * 
	 * @return
	 */
	public ArrayList<String> leeOpcionesJuego(String ruta) {
		ArrayList<String> juego = new ArrayList<String>();
		XmlPullParser xpp = serializerLecturaTarjetaSD(ruta);
		try {
			int event = xpp.next();
			while (event != XmlPullParser.END_DOCUMENT) {
				if (event == XmlPullParser.START_TAG && xpp.getName().equals(EtiquetasXML.getGamificacion()))
					juego.add(xpp.nextText());

				if (event == XmlPullParser.START_TAG && xpp.getName().equals(EtiquetasXML.getNumeroNiveles()))
					juego.add(xpp.nextText());

				if (event == XmlPullParser.START_TAG && xpp.getName().equals(EtiquetasXML.getUltimoNivelSuperado()))
					juego.add(xpp.nextText());

				event = xpp.next();
			}
			return juego;
		} catch (XmlPullParserException e) {
			Log.e("XMLParserE -> leeOpcionesJuego:", e.getMessage());
		} catch (IOException e) {
			Log.e("XMLParserE -> leeOpcionesJuego:", e.getMessage());
		}
		return null;
	}

	/**
	 * MÉTODOS ENCARGADOS DE GENERAR Y LEER EL ARCHIVO partidas_definidas.xml
	 */
	public void escribePalabrasPersonalizadas(String nombreFichero, ArrayList<String> arrayPalabras) {
		try {
			XmlSerializer serializer = serializerEscrituraTarjetaSD(nombreFichero);
			serializer.startTag(null, EtiquetasXML.getNodoPalabra());
			for (String i : arrayPalabras) {
				serializer.startTag(null, EtiquetasXML.getNodoPalabra());

				agregaEtiqueta(serializer, EtiquetasXML.getNodoNombre(), i);

				serializer.endTag(null, EtiquetasXML.getNodoPalabra());
			}
			serializer.endTag(null, EtiquetasXML.getNodoPalabra());
			serializer.endDocument();
			fout.close();
		} catch (IOException e) {
			Log.e("XMLParserE -> escribePartidasPersonalizadas:", e.getMessage());
		}
	}

	public void escribePalabrasDescripcionPersonalizadas(String nombreFichero, ArrayList<String> arrayPalabras, ArrayList<String> arrayDescripcion) {
		try {
			XmlSerializer serializer = serializerEscrituraTarjetaSD(nombreFichero);
			serializer.startTag(null, EtiquetasXML.getNodoPalabra());
			for (int i = 0; i < arrayPalabras.size(); i++) {
				serializer.startTag(null, EtiquetasXML.getNodoPalabra());

				agregaEtiqueta(serializer, EtiquetasXML.getNodoNombre(), arrayPalabras.get(i));
				if (arrayDescripcion != null)
					agregaEtiqueta(serializer, EtiquetasXML.getNodoPistaDescripcion(), arrayDescripcion.get(i));

				serializer.endTag(null, EtiquetasXML.getNodoPalabra());
			}
			serializer.endTag(null, EtiquetasXML.getNodoPalabra());
			serializer.endDocument();
			fout.close();

		} catch (IOException e) {
			Log.e("XMLParserE -> escribePartidasPersonalizadas:", e.getMessage());
		}
	}

	public void escribePartidasPersonalizadas(ArrayList<ConfiguracionPartida> arraycp, String archivo) {
		try {
			XmlSerializer serializer = serializerEscrituraTarjetaSD(archivo);
			if (serializer == null) {
				Toast.makeText(contexto, R.string.error_introduce_sd, Toast.LENGTH_LONG).show();
				return;
			}
			serializer.startTag(null, EtiquetasXML.getNodoPartida());

			for (ConfiguracionPartida cp : arraycp) {
				serializer.startTag(null, EtiquetasXML.getNodoPartida());

				agregaEtiqueta(serializer, EtiquetasXML.getNodoTipo(), cp.getTipoPartida());
				agregaEtiqueta(serializer, EtiquetasXML.getNodoTema(), cp.getCategoria());
				agregaEtiqueta(serializer, EtiquetasXML.getNODO_TAMAÑO(), cp.getTamaño());
				agregaEtiqueta(serializer, EtiquetasXML.getNodoOrientacion(), cp.getOrientacion());
				agregaEtiqueta(serializer, EtiquetasXML.getNodoInvertida(), cp.getInvertida());
				agregaEtiqueta(serializer, EtiquetasXML.getNodoTipoPista(), cp.getTipoPista());
				serializer.endTag(null, EtiquetasXML.getNodoPartida());
			}

			serializer.endTag(null, EtiquetasXML.getNodoPartida());
			serializer.endDocument();
			fout.close();
		} catch (IOException e) {
			Log.e("XMLParserE -> escribePartidasPersonalizadas:", e.getMessage());
		}
	}

	@SuppressLint("DefaultLocale")
	public ConfiguracionPartida leerPartidasDefinidas(String archivo, int anchoImagenes, int altoImagenes) {

		ConfiguracionPartida cp = new ConfiguracionPartida();
		int ids = 0;
		XmlPullParser xpp;
		xpp = serializerLecturaTarjetaSD(archivo);
		try {
			int event = xpp.next();
			while (event != XmlPullParser.END_DOCUMENT) {
				if (event == XmlPullParser.START_TAG && xpp.getName().equals(EtiquetasXML.getNodoPartida())) {
					// entramos en la configuracion de un tablero
					cp = new ConfiguracionPartida();
					cp.setId(ids++);
					XmlPullParser aux = xpp;
					cp.setTipoPartida(getDatosPartida(aux, EtiquetasXML.getNodoTipo()));
					cp.setCategoria(getDatosPartida(aux, EtiquetasXML.getNodoTema()));
					cp.setTamaño(getDatosPartida(aux, EtiquetasXML.getNODO_TAMAÑO()));
					cp.setOrientacion(getDatosPartida(aux, EtiquetasXML.getNodoOrientacion()));
					cp.setInvertida(getDatosPartida(aux, EtiquetasXML.getNodoInvertida()));
					cp.setTipoPista(getDatosPartida(aux, EtiquetasXML.getNodoTipoPista()));

					Bitmap myBitmap = BitmapFactory.decodeFile(Rutas.getRutaArchivos() + cp.getCategoria() + "/Nivel 1/imagenes/" + cp.getCategoria() + Rutas.getJpg());
					myBitmap = Imagenes.getResizedBitmap(myBitmap, anchoImagenes, altoImagenes);
					Drawable b = new BitmapDrawable(contexto.getResources(), myBitmap);
					cp.setFoto(b);
				}
				event = xpp.next();
			}
		} catch (XmlPullParserException e) {
			Log.e("XMLParserE -> leerPartidasDefinidas:", e.getMessage());
		} catch (IOException e) {
			Log.e("XMLParserE -> leerPartidasDefinidas:", e.getMessage());
		}
		return cp;
	}

	/**
	 * Método encargado de generar el archivo partida_en_juego.xml
	 */
	public void escribeTableroJuego(char[][] tableroJuego, int tamañoTablero) {
		try {
			XmlSerializer serializer = serializerEscrituraMemoriaInterna(partidaJuego);
			serializer.startTag(null, EtiquetasXML.getNodoTablero());

			for (int i = 0; i < tamañoTablero; i++) {
				serializer.startTag(null, EtiquetasXML.getNodoFila());
				serializer.attribute(null, "fila", String.valueOf(i));
				for (int j = 0; j < tamañoTablero; j++) {

					serializer.startTag(null, EtiquetasXML.getNodoLetra());
					serializer.attribute(null, "columna", String.valueOf(j));
					serializer.text(String.valueOf(tableroJuego[i][j]));
					serializer.endTag(null, EtiquetasXML.getNodoLetra());
					serializer.flush();

				}
				serializer.endTag(null, EtiquetasXML.getNodoFila());
			}
			serializer.endTag(null, EtiquetasXML.getNodoTablero());
			serializer.endDocument();
			fout.close();
		} catch (Exception e) {
			Log.e("XMLParserE -> escribeTableroJuego: Exception", e.getMessage());
		}
	}

	/**
	 * Esta funcion se usa tanto para leer los recursos guardados en la carpeta xml como para leer los recursos temporales con los que trabaja el tablero. Por lo tanto la variable (temporales) de
	 * tipo booleano determina que tipo de archivo hay que abrir en cada caso. Esto es necesario porque a la hora de crear un tablero predefinido inicialmente tenemos que recoger un recurso de la
	 * carpeta /xml, pero a continuacion debemos leer siempre el contenido del archivo /files/posiciones_palabras.xml
	 */
	public ArrayList<PosicionesPalabras> leePosicionesPalabras(boolean temporales) {
		Vector<String> ids = new Vector<String>();
		Vector<String> palabras = new Vector<String>();
		Vector<String> imagenes = new Vector<String>();
		Vector<String> audios = new Vector<String>();
		Vector<String> categorias = new Vector<String>();
		Vector<String> descripciones = new Vector<String>();
		Vector<String> elegidas = new Vector<String>();
		Vector<String> mostradas = new Vector<String>();
		Vector<String> orientaciones = new Vector<String>();
		Vector<String> color = new Vector<String>();
		Vector<String> xpixelInicial = new Vector<String>();
		Vector<String> ypixelInicial = new Vector<String>();
		Vector<String> xpixelFinal = new Vector<String>();
		Vector<String> ypixelFinal = new Vector<String>();
		Vector<String> xtableroInicial = new Vector<String>();
		Vector<String> ytableroInicial = new Vector<String>();
		Vector<String> xtableroFinal = new Vector<String>();
		Vector<String> ytableroFinal = new Vector<String>();

		try {
			pp = new ArrayList<PosicionesPalabras>();
			XmlPullParser xpp = null;
			xpp = serializerLecturaMemoriaInterna(posicionesPalabras);
			int eventType = xpp.next();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(EtiquetasXML.getNodoId())) {
						xpp.next();
						ids.add(xpp.getText());
					} else if (xpp.getName().equals(EtiquetasXML.getNodoNombre())) {
						xpp.next();
						palabras.add(xpp.getText());
					} else if (xpp.getName().equals(EtiquetasXML.getNodoPistaImagen())) {
						xpp.next();
						imagenes.add(xpp.getText());
					} else if (xpp.getName().equals(EtiquetasXML.getNodoPistaAudio())) {
						xpp.next();
						audios.add(xpp.getText());
					} else if (xpp.getName().equals(EtiquetasXML.getNodoPistaCategoria())) {
						xpp.next();
						categorias.add(xpp.getText());
					} else if (xpp.getName().equals(EtiquetasXML.getNodoPistaDescripcion())) {
						xpp.next();
						descripciones.add(xpp.getText());
					} else if (xpp.getName().equals(EtiquetasXML.getNodoElegida())) {
						xpp.next();
						elegidas.add(xpp.getText());
					} else if (xpp.getName().equals(EtiquetasXML.getNodoMostradaPantalla())) {
						xpp.next();
						mostradas.add(xpp.getText());
					} else if (xpp.getName().equals(EtiquetasXML.getNodoOrientacion())) {
						xpp.next();
						orientaciones.add(xpp.getText());
					} else if (xpp.getName().equals(EtiquetasXML.getNodoXPixelInicial())) {
						xpp.next();
						xpixelInicial.add(xpp.getText());
					} else if (xpp.getName().equals(EtiquetasXML.getNodoYPixelInicial())) {
						xpp.next();
						ypixelInicial.add(xpp.getText());
					} else if (xpp.getName().equals(EtiquetasXML.getNodoXPixelFinal())) {
						xpp.next();
						xpixelFinal.add(xpp.getText());
					} else if (xpp.getName().equals(EtiquetasXML.getNodoYPixelFinal())) {
						xpp.next();
						ypixelFinal.add(xpp.getText());
					} else if (xpp.getName().equals(EtiquetasXML.getNodoXTableroInicial())) {
						xpp.next();
						xtableroInicial.add(xpp.getText());
					} else if (xpp.getName().equals(EtiquetasXML.getNodoYTableroInicial())) {
						xpp.next();
						ytableroInicial.add(xpp.getText());
					} else if (xpp.getName().equals(EtiquetasXML.getNodoXTableroFinal())) {
						xpp.next();
						xtableroFinal.add(xpp.getText());
					} else if (xpp.getName().equals(EtiquetasXML.getNodoYTableroFinal())) {
						xpp.next();
						ytableroFinal.add(xpp.getText());
					} else if (xpp.getName().equals(EtiquetasXML.getNodoColor())) {
						xpp.next();
						color.add(xpp.getText());
					}
				}
				eventType = xpp.next();
			}
			fin.close();
			
			pp = new ArrayList<PosicionesPalabras>();
			for (int i = 0; i < ids.size(); i++) {
				PosicionesPalabras aux = new PosicionesPalabras();
				aux.setId(Integer.parseInt(ids.get(i)));
				aux.setPalabra(palabras.get(i));
				aux.setPistaImagen(imagenes.get(i));
				aux.setPistaAudio(audios.get(i));
				aux.setPistaCategoria(categorias.get(i));
				aux.setPistaDescripcion(descripciones.get(i));
				if (elegidas.get(i).equals(EtiquetasXML.getNo()))
					aux.setElegida(false);
				else
					aux.setElegida(true);

				if (mostradas.get(i).equals(EtiquetasXML.getNo()))
					aux.setMostradaPantalla(false);
				else
					aux.setMostradaPantalla(true);
				aux.setOrientacion(Integer.parseInt(orientaciones.get(i)));
				aux.setxPixelInicial(Integer.parseInt(xpixelInicial.get(i)));
				aux.setyPixelInicial(Integer.parseInt(ypixelInicial.get(i)));
				aux.setxPixelFinal(Integer.parseInt(xpixelFinal.get(i)));
				aux.setyPixelFinal(Integer.parseInt(ypixelFinal.get(i)));
				aux.setxTableroInicial(Integer.parseInt(xtableroInicial.get(i)));
				aux.setyTableroInicial(Integer.parseInt(ytableroInicial.get(i)));
				aux.setxTableroFinal(Integer.parseInt(xtableroFinal.get(i)));
				aux.setyTableroFinal(Integer.parseInt(ytableroFinal.get(i)));
				aux.setColor(Integer.parseInt(color.get(i)));
				pp.add(aux);
			}
			return pp;
		} catch (Exception e) {
			Log.e("Error leePosicionesPalabras: ", "Error:  " + e);
		}
		return null;
	}

	public void escribePosicionesPalabras(ArrayList<PosicionesPalabras> pp) {
		/**
		 * Método encargado de escribir el archivo posiciones_palabras.xml
		 */
		try {

			XmlSerializer serializer = serializerEscrituraMemoriaInterna(posicionesPalabras);
			serializer.startTag(null, EtiquetasXML.getNodoPalabra());

			for (PosicionesPalabras aux : pp) {
				serializer.startTag(null, EtiquetasXML.getNodoPalabra());
				
				agregaEtiqueta(serializer, EtiquetasXML.getNodoId(), String.valueOf(aux.getId()));
				agregaEtiqueta(serializer, EtiquetasXML.getNodoNombre(), aux.getPalabra());
				agregaEtiqueta(serializer, EtiquetasXML.getNodoPistaImagen(), (aux.getPistaImagen() == null) ? "-" : aux.getPistaImagen());
				agregaEtiqueta(serializer, EtiquetasXML.getNodoPistaAudio(), (aux.getPistaAudio() == null) ? "-" : aux.getPistaAudio());
				agregaEtiqueta(serializer, EtiquetasXML.getNodoPistaCategoria(), (aux.getPistaCategoria() == null) ? "-" : aux.getPistaCategoria());
				agregaEtiqueta(serializer, EtiquetasXML.getNodoPistaDescripcion(), (aux.getPistaDescripcion() == null) ? "-" : aux.getPistaDescripcion());
				/*
				 * ELEGIDA, MOSTRADA, INVERTIDA, ORIENTACION
				 */
				String no = EtiquetasXML.getNo();
				String si = EtiquetasXML.getSi();
				agregaEtiqueta(serializer, EtiquetasXML.getNodoElegida(), aux.isElegida()?si:no);
				agregaEtiqueta(serializer, EtiquetasXML.getNodoMostradaPantalla(), aux.getMostradaPantalla()?si:no);
				agregaEtiqueta(serializer, EtiquetasXML.getNodoInvertida(), (aux.isInvertida())?no:si );
				/*
				 * POSICION DE LAS PALABRAS EN LA PANTALLA
				 */
				agregaEtiqueta(serializer, EtiquetasXML.getNodoOrientacion(), String.valueOf(aux.getOrientacion()));
				agregaEtiqueta(serializer, EtiquetasXML.getNodoXPixelInicial(), String.valueOf(aux.getxPixelInicial()));
				agregaEtiqueta(serializer, EtiquetasXML.getNodoYPixelInicial(), String.valueOf(aux.getyPixelInicial()));
				agregaEtiqueta(serializer, EtiquetasXML.getNodoXPixelFinal(), String.valueOf(aux.getxPixelFinal()));
				agregaEtiqueta(serializer, EtiquetasXML.getNodoYPixelFinal(), String.valueOf(aux.getyPixelFinal()));
				agregaEtiqueta(serializer, EtiquetasXML.getNodoXTableroInicial(), String.valueOf(aux.getxTableroInicial()));
				agregaEtiqueta(serializer, EtiquetasXML.getNodoYTableroInicial(), String.valueOf(aux.getyTableroInicial()));
				agregaEtiqueta(serializer, EtiquetasXML.getNodoXTableroFinal(), String.valueOf(aux.getxTableroFinal()));
				agregaEtiqueta(serializer, EtiquetasXML.getNodoYTableroFinal(), String.valueOf(aux.getyTableroFinal()));
				agregaEtiqueta(serializer, EtiquetasXML.getNodoColor(), String.valueOf(aux.getColor()));

				serializer.endTag(null, EtiquetasXML.getNodoPalabra());
				serializer.flush();
			}
			serializer.endTag(null, EtiquetasXML.getNodoPalabra());
			serializer.endDocument();
			fout.close();
		} catch (Exception e) {
			Log.e("XMLParserE -> escribePosicionesPalabras", "Error: " + e.getMessage());
		}
	}

	public void guardaConfiguracionTableros() {
		/**
		 * Método que se encarga de restaurar la copia de seguridad
		 */
		File fichero = new File(rutaSharedPreferences + xmlTexto);
		if (fichero.exists()) {
			File salida = new File(rutaSharedPreferences + EtiquetasXML.getCopia() + xmlTexto);
			try {
				InputStream in = new FileInputStream(fichero);
				OutputStream out = new FileOutputStream(salida);
				byte[] buf = new byte[1024];
				int len;

				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			} catch (FileNotFoundException e) {
				Log.e("XMLParserExternos : guardaConfiguracionTableros", "Error " + e.getMessage());
			} catch (IOException e) {
				Log.e("XMLParserExternos : guardaConfiguracionTableros", "Error " + e.getMessage());
			}
		}
	}

	public int getPalabrasDescripcionesPersonalizadasTemas(String tema) {
		/**
		 * Método que nos permite sólo las palabra de un tema determinado
		 */
		try {
			XmlPullParser xpp = serializerLecturaTarjetaSD(tema);
			int eventType = xpp.getEventType();
			ArrayList<PosicionesPalabras> arraypp = new ArrayList<PosicionesPalabras>();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG && xpp.getName().equals(EtiquetasXML.getNodoNombre())) {
					PosicionesPalabras aux = new PosicionesPalabras();
					eventType = xpp.next();
					if (eventType == XmlPullParser.TEXT)
						aux.setPalabra(xpp.getText());

					xpp.next();
					xpp.next();
					xpp.next();
					if (xpp.getName().equals(EtiquetasXML.getNodoPistaDescripcion())) {
						eventType = xpp.next();
						if (eventType == XmlPullParser.TEXT) {
							aux.setPistaDescripcion(xpp.getText());
						}
					}
					arraypp.add(aux);
				}
				eventType = xpp.next();
			}
			pp = new ArrayList<PosicionesPalabras>();
			for (PosicionesPalabras aux : arraypp)
				pp.add(aux);

			return pp.size();

		} catch (XmlPullParserException e) {
			Log.e("Clase: getPalabrasPersonalizadas", "Error -> XmlPullParserException: " + e.getMessage());
		} catch (FileNotFoundException e) {
			Log.e("Clase: getPalabrasPersonalizadas", "Error -> FileNotFoundException: " + e.getMessage());
		} catch (IOException e) {
			Log.e("Clase: getPalabrasPersonalizadas", "Error -> IOException: " + e.getMessage());
		}
		return 0;
	}

	/**
	 * Método que nos permite sólo las palabra de un tema determinado
	 */
	public ArrayList<PosicionesPalabras> getPalabrasPersonalizadasTemas(String tema) {
		try {
			XmlPullParser xpp = serializerLecturaTarjetaSD(tema);
			int eventType = xpp.getEventType();
			ArrayList<PosicionesPalabras> arraypp = new ArrayList<PosicionesPalabras>();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG && xpp.getName().equals(EtiquetasXML.getNodoNombre())) {
					eventType = xpp.next();
					if (eventType == XmlPullParser.TEXT) {

						PosicionesPalabras aux = new PosicionesPalabras();
						aux.setPalabra(xpp.getText());
						arraypp.add(aux);
					}
				}
				eventType = xpp.next();
			}
			ArrayList<PosicionesPalabras> pp = new ArrayList<PosicionesPalabras>();
			for (PosicionesPalabras aux : arraypp)
				pp.add(aux);
			return pp;

		} catch (XmlPullParserException e) {
			Log.e("Clase: getPalabrasPersonalizadas", "Error -> getCategoria: " + e.getMessage());
		} catch (FileNotFoundException e) {
			Log.e("Clase: getPalabrasPersonalizadas", "Error -> getCategoria: " + e.getMessage());
		} catch (IOException e) {
			Log.e("Clase: getPalabrasPersonalizadas", "Error -> getCategoria: " + e.getMessage());
		}
		return null;
	}

	/**
	 * METODOS get Y set DEL ARRAY PosicionesPalabras
	 */
	public ArrayList<PosicionesPalabras> getPp() {
		return pp;
	}

	/**
	 * MÉTODOS DE OBTENCIÓN DE LOS DIFERENTES PARÁMETROS NECESARIOS PARA GESTIONAR LOS ARCHIVOS xml
	 */
	private void agregaEtiqueta(XmlSerializer serializer, String etiqueta, String contenido) {
		try {
			serializer.startTag(null, etiqueta);
			serializer.text(contenido);
			serializer.endTag(null, etiqueta);
		} catch (IllegalArgumentException e) {
			Log.e("XMLParserE -> agregaEtiqueta: IllegalArgumentException", e.getMessage());
		} catch (IllegalStateException e) {
			Log.e("XMLParserE -> agregaEtiqueta: IllegalStateException", e.getMessage());
		} catch (IOException e) {
			Log.e("XMLParserE -> agregaEtiqueta: IOException", e.getMessage());
		}
	}

	/**
	 * Método que nos sirve para generar un parser con el que gestionar el contenido guardado en un fichero xml
	 * 
	 * @param archivo
	 *            nombre del archivo que se quiere abrir como lectura
	 * @return XmlPullParser devuelve el parser con el que se va a trabajar en modo de lectura
	 */
	private XmlPullParser serializerLecturaTarjetaSD(String archivo) {

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser parser = factory.newPullParser();
			File file = new File(archivo);
			FileInputStream fis = new FileInputStream(file);
			parser.setInput(new InputStreamReader(fis));
			return parser;
		} catch (FileNotFoundException e) {
			Log.e("XMLParserE -> getSerializer: FileNotFoundException", e.getMessage());
		} catch (XmlPullParserException e) {
			Log.e("XMLParserE -> getSerializer: XmlPullParserException", e.getMessage());
		}
		return null;
	}

	/**
	 * Método que nos sirve para generar un parser con el que gestionar el contenido guardado en un fichero xml
	 * 
	 * @param archivo
	 *            nombre del archivo que se quiere abrir como lectura
	 * @return XmlPullParser devuelve el parser con el que se va a trabajar en modo de lectura
	 */
	private XmlPullParser serializerLecturaMemoriaInterna(String archivo) {

		try {
			fin = contexto.openFileInput(archivo);
			XmlPullParser xpp = Xml.newPullParser();
			xpp.setInput(fin, "utf-8");
			return xpp;
		} catch (FileNotFoundException e) {
			Log.e("XMLParserE -> getSerializer: FileNotFoundException", e.getMessage());
		} catch (XmlPullParserException e) {
			Log.e("XMLParserE -> getSerializer: XmlPullParserException", e.getMessage());
		}
		return null;
	}

	private XmlSerializer serializerEscrituraTarjetaSD(String archivo) {

		try {
			File newxmlfile = new File(archivo);
			newxmlfile.createNewFile();
			fout = new FileOutputStream(newxmlfile);
			XmlSerializer serializer = Xml.newSerializer();
			serializer.setOutput(fout, "utf-8");
			serializer.startDocument(null, true);
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			return serializer;
		} catch (FileNotFoundException e) {
			Log.e("XMLParserExternos -> serializerEscrituraTarjetaSD", e.getMessage());
		} catch (IOException e) {
			Log.e("XMLParserExternos -> serializerEscrituraTarjetaSD", e.getMessage());
		}
		return null;
	}

	/**
	 * Método que nos permite generar un parser con el que generar un archivo xml
	 * 
	 * @param archivo
	 *            nombre del archivo que se quiere abrir como escritura
	 * @return XmlSerializer Parser con el que vamos a escribir el archivo xml
	 */
	private XmlSerializer serializerEscrituraMemoriaInterna(String archivo) {

		try {
			fout = contexto.openFileOutput(archivo, Context.MODE_PRIVATE);
			XmlSerializer serializer = Xml.newSerializer();
			serializer.setOutput(fout, "utf-8");
			serializer.startDocument(null, true);
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			return serializer;
		} catch (FileNotFoundException e) {
			Log.e("XMLParserE -> serializerEscrituraMemoriaInterna: FileNotFoundException", e.getMessage());
		} catch (IllegalArgumentException e) {
			Log.e("XMLParserE -> serializerEscrituraMemoriaInterna: IllegalArgumentException", e.getMessage());
		} catch (IllegalStateException e) {
			Log.e("XMLParserE -> serializerEscrituraMemoriaInterna: IllegalStateException", e.getMessage());
		} catch (IOException e) {
			Log.e("XMLParserE -> serializerEscrituraMemoriaInterna: IOException", e.getMessage());
		}
		return null;
	}

	private String getDatosPartida(XmlPullParser xpp, String etiqueta) throws XmlPullParserException, IOException {
		int event = xpp.getEventType();
		do {
			if ((event = xpp.next()) == XmlPullParser.END_TAG && xpp.getName().equals(EtiquetasXML.getNodoPartida()))
				break;
			else if (event == XmlPullParser.START_TAG && xpp.getName().equals(etiqueta))
				return xpp.nextText();
		} while (event != XmlPullParser.END_DOCUMENT);
		return null;
	}
}
