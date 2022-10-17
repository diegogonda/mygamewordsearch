package vistas;

import java.util.ArrayList;
import java.util.Locale;
import objetos.EtiquetasXML;
import objetos.Imagenes;
import objetos.PosicionesPalabras;
import objetos.Preferencias;
import objetos.Rutas;
import objetos.ScrollVertical;
import xml.XMLParser;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import es.uvigo.gti.mygamewordsearch.R;

public class PistasVista extends View implements TextToSpeech.OnInitListener {
	/**
	 * Variables del Scroll Horizontal Personalizado
	 */
	private int yInicial;
	private int yFinal;
	private boolean primeraIteracion = true;
	private static ScrollVertical scroll = new ScrollVertical();
	private static Context contexto;
	private static ArrayList <PosicionesPalabras> pp;
	private static XMLParser xml;
	private static Paint textoPaint = new Paint();
	private static int ancho;
	private static int alto;
	private static String tipoPista;
	private static int tamanoImagenes;
	private static int paddingImagenes;
	private static ArrayList<Drawable> bmp;
	private static Drawable correcto = null;
	private static String nombrePaquete = Rutas.getNombrePaquete();
	private static String recursoDrawable = EtiquetasXML.getRecursoDrawable();
	private static String imagenCorrecto = EtiquetasXML.getImagenCorrecto();
	private static int cantidadCaracteresDescripcion;
	private static TextToSpeech tts;
	private static int alturaPrimeraPalabra;
	private static boolean primeraIteraccion = true;

	/**
	 * Variables de las posiciones en que vamos a pintar las im�genes
	 */
	private static int xIni = 0;
	private static int yIni = 0;
	private static int xFin = 0;
	private static int yFin = 0;
	private static int filas = 1;
	private static int columnas = 0;
	/**
	 * Variables de las posiciones en que vamos a pintar los audios, qye se utilizar�n para reproducir los sonidos
	 */
	private static ArrayList<Integer> xAudiosIni;
	private static ArrayList<Integer> yAudiosIni;
	private static ArrayList<Integer> xAudiosFin;
	private static ArrayList<Integer> yAudiosFin;
	private static boolean esAudio = false;

	public PistasVista(Context context, AttributeSet attrs) {
		super(context, attrs);
		contexto = context;
		float dp = getResources().getDisplayMetrics().density;
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PistasVista, 0, 0);
		primeraIteraccion = true;

		xml = new XMLParser(contexto);
		pp = xml.leePosicionesPalabras(true);

		try {
			textoPaint.setTextSize(a.getDimension(R.styleable.PistasVista_altoPalabras, 16 * dp));
			textoPaint.setColor(a.getColor(R.styleable.PistasVista_colorPalabras, Color.BLACK));
			tamanoImagenes = a.getDimensionPixelSize(R.styleable.PistasVista_tamano_imagenes_pistas, (int) (45 * dp));
			paddingImagenes = a.getDimensionPixelSize(R.styleable.PistasVista_padding_imagenes, (int) (10 * dp));
			cantidadCaracteresDescripcion = a.getDimensionPixelSize(R.styleable.PistasVista_cantidad_caracteres_descripcion, (int) (25 * dp));
			alturaPrimeraPalabra = a.getDimensionPixelSize(R.styleable.PistasVista_altura_primera_palabra, (int) (30 * dp));
			columnas = 5;//a.getInt(R.styleable.PistasVista_numero_columnas, 4);
			scroll.setY_0(0);

		} finally {
			a.recycle();
		}
		textoPaint.setAntiAlias(true);
		textoPaint.setTextAlign(Paint.Align.CENTER);

		tipoPista = Preferencias.getTipoPista(contexto);
		if (tipoPista.equals(EtiquetasXML.getNodoPistaAudio())) {
			tts = new TextToSpeech(contexto, new TextToSpeech.OnInitListener() {
				@Override
				public void onInit(int status) {
					Locale loc = new Locale(EtiquetasXML.getISOEspanol(), "ES");
					tts.setLanguage(loc);
				}
			});
			esAudio = true;
			xAudiosIni = new ArrayList<Integer>();
			yAudiosIni = new ArrayList<Integer>();
			xAudiosFin = new ArrayList<Integer>();
			yAudiosFin = new ArrayList<Integer>();
			bmp = new ArrayList<Drawable>();
			int c = getResources().getIdentifier(imagenCorrecto, recursoDrawable, nombrePaquete);
			Resources res = contexto.getResources();
			correcto = res.getDrawable(c);
		} else if (tipoPista.equals(EtiquetasXML.getNodoPistaImagen())) {
			bmp = new ArrayList<Drawable>();
			int c = getResources().getIdentifier(imagenCorrecto, recursoDrawable, nombrePaquete);
			Resources res = contexto.getResources();
			correcto = res.getDrawable(c);

		}
	}

	private Canvas pistaImagen(Canvas canvas, ArrayList <PosicionesPalabras> pp) {
		if (primeraIteraccion) {
			for (PosicionesPalabras aux : pp) {
				String ruta = Rutas.getImagenesPalabras(aux.getPistaCategoria(), aux.getPalabra());
				// Tenemos que reducir el tamano del Bitmap para evitar que d� problemas a la hora de cerrar la actividad.
				Bitmap myBitmap = Imagenes.getResizedBitmap(BitmapFactory.decodeFile(ruta), getWidth() / columnas, 277 / columnas);
				bmp.add(new BitmapDrawable(contexto.getResources(), myBitmap));
			}
		}
		return pintaImagenes(canvas, pp);

	}

	private Canvas pistaNombre(Canvas canvas,ArrayList <PosicionesPalabras> pp) {

		int columna1 = ancho / 3;
		int columna2 = ancho * 2 / 3;

		int y = alturaPrimeraPalabra + scroll.getY_0(); // Posicion Inicial
		for (int i = 0; i < pp.size(); i++) {
			PosicionesPalabras aux = pp.get(i++); // Almacenamos y apuntamos a la siguiente

			textoPaint.setColor(aux.isElegida() ? Color.RED : Color.BLACK);
			canvas.drawText(aux.getPalabra(), columna1, y, textoPaint);

			if (i >= pp.size())
				break;
			aux = pp.get(i);

			textoPaint.setColor(aux.isElegida() ? Color.RED : Color.BLACK);

			canvas.drawText(aux.getPalabra(), columna2, y, textoPaint);
			y += alturaPrimeraPalabra;
		}
		if (primeraIteracion) {
			scroll.setY_f(y -= alturaPrimeraPalabra / 2);
		}

		return canvas;
	}

	private void redibuja() {
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		ancho = getWidth();
		alto = getHeight();

		if (primeraIteracion) {
			scroll.setY_v(alto);
		}

		xml = new XMLParser(contexto);
		pp = xml.leePosicionesPalabras(true);

			if (tipoPista.equals(EtiquetasXML.getNodoNombre()) || tipoPista.equals(EtiquetasXML.getNodoPistaMarca())) {
				canvas = pistaNombre(canvas, pp);
				primeraIteracion = false;
			} else if (tipoPista.equals(EtiquetasXML.getNodoPistaImagen())) {
				canvas = pistaImagen(canvas, pp);
				primeraIteraccion = false;
			} else if (tipoPista.equals(EtiquetasXML.getNodoPistaAudio())) {
				canvas = pistaAudio(canvas, pp);
				primeraIteraccion = false;
			} else if (tipoPista.equals(EtiquetasXML.getNodoPistaDescripcion())) {
				canvas = pistaDescripcion(canvas, pp);
				primeraIteracion = false;
			}
			if (!tipoPista.equals(EtiquetasXML.getNodoPistaSinPista()))
				redibuja();
		
	}

	private Canvas pistaDescripcion(Canvas canvas, ArrayList <PosicionesPalabras> pp) {

		int y = alturaPrimeraPalabra + scroll.getY_0(); // Posicion Inicial
		for (int i = 0; i < pp.size(); i++) {
			PosicionesPalabras aux = pp.get(i);

			textoPaint.setColor((aux.isElegida()) ? (Color.RED) : Color.BLACK);

			int letraInicial = 0;
			int letraFinal = 0;
			String descripcion = aux.getPistaDescripcion();
			for (int j = 0; j < descripcion.length(); j++) {
				letraInicial = (j == 0) ? (cantidadCaracteresDescripcion * j) : (letraFinal++);
				boolean rompe = false;
				if (cantidadCaracteresDescripcion * (j + 1) < descripcion.length()) {
					letraFinal = cantidadCaracteresDescripcion * (j + 1);
					letraFinal = esEspacio(letraInicial, letraFinal, descripcion);
				} else {
					rompe = true;
					letraFinal = descripcion.length();
				}
				canvas.drawText(descripcion, letraInicial, letraFinal, ancho / 2, y, textoPaint);
				y += alturaPrimeraPalabra;
				if (rompe)
					break;
			}
			textoPaint.setColor(Color.BLACK);
			y += 1; // separacion a la linea
			canvas.drawLine(0, y, ancho, y, textoPaint);
			y += alturaPrimeraPalabra;
		}
		if (primeraIteracion) {
			scroll.setY_f(y -= alturaPrimeraPalabra * 2);
		}

		return canvas;
	}

	/**
	 * Para evitar que haya palabras cortadas en las descripciones del tablero, creamos esta funci�n, que eval�a si una palabra va ser cortada
	 * en caso afirmativo busca el anterior espacio dentro del intervalo, en caso de no encontrar dicho espacio la palabra acabar� cortada.
	 * 
	 * @param letraInicial
	 * @param letraFinal
	 * @param descripcion
	 * @return
	 */
	private int esEspacio(int letraInicial, int letraFinal, String descripcion) {
		if (descripcion.charAt(letraFinal) == ' ' || descripcion.charAt(letraFinal) == '.' || descripcion.charAt(letraFinal) == ',')
			return letraFinal;
		else {
			for (int i = letraFinal; i > letraInicial; i--) {
				if (descripcion.charAt(i) == ' ' || descripcion.charAt(i) == '.' || descripcion.charAt(i) == ',')
					return i;
			}
		}
		return letraFinal;
	}

	private Canvas pistaAudio(Canvas canvas, ArrayList <PosicionesPalabras> pp) {
		if (primeraIteraccion) {
			Resources res = contexto.getResources();
			for (int i = 0; i < pp.size(); i++) {
				bmp.add(res.getDrawable(R.drawable.principal_music));
			}
		}
		return pintaImagenes(canvas, pp);

	}

	private static Canvas pintaImagenes(Canvas canvas, ArrayList <PosicionesPalabras> pp) {
		filas = (int) Math.ceil((double) (pp.size()) / (double) (columnas));
		tamanoImagenes = ancho / columnas;

		for (int i = 0, contador = 0; i < filas; i++) { // filas
			for (int j = 0; j < columnas; j++, contador++) {
				xIni = tamanoImagenes * j + paddingImagenes;
				yIni = tamanoImagenes * i + scroll.getY_0() + paddingImagenes;
				xFin = tamanoImagenes * (j + 1) - paddingImagenes;
				yFin = tamanoImagenes * (i + 1) - paddingImagenes + scroll.getY_0();
				if (esAudio) {
					xAudiosIni.add(xIni);
					yAudiosIni.add(yIni);
					xAudiosFin.add(xFin);
					yAudiosFin.add(yFin);
				}
				if (contador < pp.size()) {
					PosicionesPalabras aux = pp.get(contador);
					Drawable b = bmp.get(contador);
					b.setBounds(new Rect(xIni, yIni, xFin, yFin));
					b.draw(canvas);
					if (aux.isElegida()) {
						correcto.setBounds(new Rect(xIni, yIni, xFin, yFin));
						correcto.draw(canvas);
					}
				}
			}
		}
		scroll.setY_f(yFin + 3 * paddingImagenes);
		return canvas;
	}

	/**
	 * Determina que imagen (pista de audio) hemos seleccionada, para reproducir su sonido asociado
	 */
	@Override
	public boolean onTouchEvent(MotionEvent evento) {
		switch (evento.getAction()) {
		case MotionEvent.ACTION_DOWN: // Se ha pulsado la pantalla
			int x = (int) (evento.getX());
			int y = (int) (evento.getY());
			if (tipoPista.equals(EtiquetasXML.getNodoPistaAudio())) {
				for (int i = 0; i < xAudiosIni.size(); i++) {
					if (xAudiosIni.get(i) < x && yAudiosIni.get(i) < y && xAudiosFin.get(i) > x && yAudiosFin.get(i) > y) {
						reproduceSonido(pp.get(i));
						break;
					}
				}
			}
			yInicial = y;
			scroll.desplazamiento(yInicial, yInicial, true);
			return true;

		case MotionEvent.ACTION_MOVE: // Se est� moviendo el dedo por la
			yFinal = (int) (evento.getY());

			scroll.desplazamiento(yInicial, yFinal, false);
			invalidate();
			return true;

		case MotionEvent.ACTION_UP: // Se ha levantado el dedo
			scroll.desplazamiento(yInicial, yFinal, true);

			yFinal = (int) (evento.getY());
			invalidate();
			return true;
		}

		return false;
	}
	private void reproduceSonido(PosicionesPalabras aux) {
		if (!aux.isElegida())
			tts.speak(aux.getPalabra(), TextToSpeech.QUEUE_FLUSH, null);
	}

	@Override
	public void onInit(int arg0) {
	}

}
