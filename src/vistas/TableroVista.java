package vistas;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import objetos.CreaTablero;
import objetos.EtiquetasXML;
import objetos.MensajesEmergentes;
import objetos.PosicionesPalabras;
import objetos.Preferencias;
import objetos.Rutas;
import objetos.TableroCanvas;
import xml.XMLParser;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import es.uvigo.gti.mygamewordsearch.R;

public class TableroVista extends View {
	/**
	 * Variables de las im�genes reforzadoras
	 */
	private static int tamanoImagenesReforazadoras;
	/**
	 * Variables de Juego
	 */
	private XMLParser xml;
	private CreaTablero ct;
	private static ArrayList<PosicionesPalabras> pp;
	private static PosicionesPalabras aux;
	private int palabraSeleccionada;

	private int xInicial;
	private int yInicial;
	private int xFinal;
	private int yFinal;
	private TableroCanvas tc;
	private boolean seleccionando = false;
	// TIPO DE SELECCION: PULSANDO LA PRIMERA LETRA Y LA �LTIMA
	/**
	 * Variables cuyo valores ser�n cargados desde la vista XML
	 * */
	private int anchoBarra;
	private int barraPalabraSeleccionadaX;
	private int barraPalabraSeleccionadaY;
	private int margenDerechoTablero;
	private int margenIzquierdoTablero;
	private int margenSuperiorTablero;
	private int margenInferiorTablero;
	private int barraPalabraSeleccionadaInclinadaX1;
	private int barraPalabraSeleccionadaInclinadaX2;
	private int radioPista;
	private int ajustePista;
	private Paint letrasPaint = new Paint();
	private Paint seleccionadaPaint = new Paint();
	private Paint pistaPaint = new Paint();
	private int nivelEnJuego;
	private int ultimoNivelSuperado;
	private int nivelesGama;
	private String temaActual;
	private boolean gamaSuperada = false;
	/**
	 * Variables del tablero
	 */
	private static int tamanoTablero = 10;
	private static String tipoPista; // Variable guarda el tipo de pista que
										// vamos a usar
	private static char[][] tableroJuego;
	private static Context contexto;
	private static int contadorColores = 0;
	private static int color = Color.YELLOW;
	/**
	 * Variables de la grilla
	 * 
	 * @param context
	 * @param attrs
	 */
	private static Paint colorGrilla = new Paint();
	private static int anchoGrillas;

	public TableroVista(Context context, AttributeSet attrs) {
		super(context, attrs);
		contexto = context;
		/**
		 * Preferencias del tablero
		 */
		tamanoTablero = Integer.valueOf(Preferencias.getTamanoTablero(contexto));
		tipoPista = Preferencias.getTipoPista(contexto);

		tc = new TableroCanvas();

		float dp = getResources().getDisplayMetrics().density;
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TableroVista, 0, 0);

		try {
			/**
			 * En primer lugar, para crear el tablero, recogemos el contenido del fichero Posiciones_palabras.xml y generamos con �l, el contenido la variable pp
			 */

			xml = new XMLParser(contexto);
			pp = xml.leePosicionesPalabras(false);

			ct = new CreaTablero(contexto, pp);
			tableroJuego = ct.getTableroJuego();
			xml.escribeTableroJuego(tableroJuego, tamanoTablero);
			pp = ct.getPp();

			anchoBarra = a.getDimensionPixelSize(R.styleable.TableroVista_anchoBarra, (int) (20 * dp));
			barraPalabraSeleccionadaX = a.getDimensionPixelSize(R.styleable.TableroVista_barraPalabraSeleccionadaX, (int) (20 * dp));
			barraPalabraSeleccionadaY = a.getDimensionPixelSize(R.styleable.TableroVista_barraPalabraSeleccionadaY, (int) (20 * dp));
			barraPalabraSeleccionadaInclinadaX1 = a.getDimensionPixelSize(R.styleable.TableroVista_barraPalabraSeleccionadaInclinadaX1, (int) (20 * dp));
			barraPalabraSeleccionadaInclinadaX2 = a.getDimensionPixelSize(R.styleable.TableroVista_barraPalabraSeleccionadaInclinadaX2, (int) (20 * dp));

			margenDerechoTablero = a.getDimensionPixelSize(R.styleable.TableroVista_margenDerechoTablero, (int) (20 * dp));
			margenIzquierdoTablero = a.getDimensionPixelSize(R.styleable.TableroVista_margenIzquierdoTablero, (int) (20 * dp));
			margenSuperiorTablero = a.getDimensionPixelSize(R.styleable.TableroVista_margenSuperiorTablero, (int) (20 * dp));
			margenInferiorTablero = a.getDimensionPixelSize(R.styleable.TableroVista_margenInferiorTablero, (int) (20 * dp));

			letrasPaint.setTextSize(a.getDimension(R.styleable.TableroVista_altoLetras, 16 * dp));
			letrasPaint.setColor(a.getColor(R.styleable.TableroVista_colorLetras, Color.BLACK));
			letrasPaint.setColor(Color.BLACK);

			radioPista = a.getDimensionPixelOffset(R.styleable.TableroVista_radio_marca_pista, (int) (15 * dp));
			ajustePista = a.getDimensionPixelOffset(R.styleable.TableroVista_ajuste_marca_pista, (int) (5 * dp));
			pistaPaint.setColor(a.getColor(R.styleable.TableroVista_color_marca_pista, Color.GREEN));
			pistaPaint.setAlpha(80);

			seleccionadaPaint.setColor(color);

			tamanoImagenesReforazadoras = a.getDimensionPixelOffset(R.styleable.TableroVista_tamano_imagen_reforzadora, (int) (50 * dp));

			colorGrilla.setColor(a.getColor(R.styleable.TableroVista_color_grilla, Color.BLUE));
			colorGrilla.setStrokeWidth(a.getDimension(R.styleable.TableroVista_ancho_grilla, 2 * dp));
		} finally {
			a.recycle();
		}
		letrasPaint.setAntiAlias(true);
		letrasPaint.setTextAlign(Paint.Align.CENTER);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		anchoGrillas = getWidth() / tamanoTablero;
		anchoBarra = anchoGrillas * 8 / 10;
		letrasPaint.setTextSize(anchoBarra);
		radioPista = anchoBarra*65/100;
		seleccionadaPaint.setStrokeWidth(anchoBarra);
		int ancho = canvas.getWidth();
		resizeView(this, ancho, ancho); // forzamos que la vista tenga el mismo ancho que alto

		tc.setCanvas(canvas);
		tc.PintaTablero(pp, getWidth(), tamanoTablero, tableroJuego, letrasPaint, margenDerechoTablero, margenIzquierdoTablero, margenSuperiorTablero, margenInferiorTablero);
		tc.PintaGrilla(ancho, tamanoTablero, colorGrilla);
		(pp, anchoBarra, barraPalabraSeleccionadaX, barraPalabraSeleccionadaY, barraPalabraSeleccionadaInclinadaX1, barraPalabraSeleccionadaInclinadaX2);

		if (seleccionando) {
			seleccionadaPaint.setAlpha(100);
			canvas.drawLine(xInicial, yInicial, xFinal, yFinal, seleccionadaPaint);
		}

		if (tipoPista.equals("pista_marca")) {
			tc.PistasMarcaPrimeraLetra(canvas, pp, radioPista, ajustePista, pistaPaint);
		}

		canvas = tc.getCanvas();
	}

	@Override
	/**
	 * Recoge los puntos sobre los que estamos pulsando para pintarlos en la pantalla
	 * Punto tambi�n llama al Metodo que determina si hemos seleccionado o no una palabra
	 */
	public boolean onTouchEvent(MotionEvent evento) {
		switch (evento.getAction()) {
		case MotionEvent.ACTION_DOWN: // Se ha pulsado la pantalla
			xInicial = (int) evento.getX();
			yInicial = (int) evento.getY();
			xFinal = (int) evento.getX();
			yFinal = (int) evento.getY();
			break;
		case MotionEvent.ACTION_MOVE: // Se est� moviendo el dedo por la pantalla
			xFinal = (int) (evento.getX());
			yFinal = (int) (evento.getY());
			seleccionando = true;
			invalidate();
			break;

		case MotionEvent.ACTION_UP: // Se ha levantado el dedo
			xFinal = (int) evento.getX();
			yFinal = (int) evento.getY();
			
			if ((palabraSeleccionada = palabraSeleccionada()) != -1) {
				aux = pp.get(palabraSeleccionada);
				aux.setElegida(true);
				aux.setColor(color);
				pp.set(palabraSeleccionada, aux);
				xml.escribePosicionesPalabras(pp);
				
				seleccionando = false;
				seleccionadaPaint.setColor((color = colorLinea()));
			}
			else {
				xInicial = 0;
				yInicial = 0;
				xFinal = 0;
				yFinal = 0;
			}
			if (todasSeleccionadas()) {
				PartidaFinalizada();
			}

			invalidate();
			break;
		}
		return true;
	}

	/**
	 * Determina si ya hemos seleccionado todas las palabras del tablero
	 * */
	private boolean todasSeleccionadas() {
		for (PosicionesPalabras aux : pp)
			if (!aux.isElegida())
				return false;
		return true;
	}

	/**
	 * Determina si despues de pulsar y soltar sobre el tablero una palabra fue seleccionada
	 * */
	private int palabraSeleccionada() {
		int k = 0;
		for (PosicionesPalabras aux : pp) {
			// Si se selecciona de adelante a atr�s
			double xMaxInicial = aux.getxPixelInicial() + anchoGrillas / 2;
			double xMinInicial = aux.getxPixelInicial() - anchoGrillas / 2;
			double yMaxInicial = aux.getyPixelInicial() + anchoGrillas / 2;
			double yMinInicial = aux.getyPixelInicial() - anchoGrillas / 2;

			double xMaxFinal = aux.getxPixelFinal() + anchoGrillas / 2;
			double xMinFinal = aux.getxPixelFinal() - anchoGrillas / 2;
			double yMaxFinal = aux.getyPixelFinal() + anchoGrillas / 2;
			double yMinFinal = aux.getyPixelFinal() - anchoGrillas / 2;
			/*
			 * Palabra seleccionada "normal"
			 */
			if (xMaxInicial > xInicial && xMinInicial < xInicial && yMaxInicial > yInicial && yMinInicial < yInicial) { // seleccionada letra inicial
				if (xMaxFinal > xFinal && xMinFinal < xFinal && yMaxFinal > yFinal && yMinFinal < yFinal) {
					// En este punto ya sabemos que hay seleccionada una palabra, pero para evitar que se vuelva a seleccionar
					// cambiando de color su raya, comprobamos y si ya fue seleccionada revolvemos -1, es decir, no seleccionada
					return (aux.isElegida())? -1 : k;

				}
			}

			if (xMaxFinal > xInicial && xMinFinal < xInicial && yMaxFinal > yInicial && yMinFinal < yInicial) { // seleccionada letra inicial
				if (xMaxInicial > xFinal && xMinInicial < xFinal && yMaxInicial > yFinal && yMinInicial < yFinal) {
					// En este punto ya sabemos que hay seleccionada una palabra, pero para evitar que se vuelva a seleccionar
					// cambiando de color su raya, comprobamos y si ya fue seleccionada revolvemos -1, es decir, no seleccionada
					return (aux.isElegida())? -1 : k;
				}
			}
			k++;
		}
		return -1; // No se selecciononada
	}

	/**
	 * Metodo en el que, una vez se ha terminado la partida, se eval�a si se trata de una gama o no. Si es Gama: 1.- Si eval�a si hemos superado un nivel no superado anteriomente. Si es un nivel no
	 * superado, se comprueba que no sea el �ltimo nivel del juego, si no lo es se actualiza la informacion del ultimo nivel superado en el archivo opciones_juego.xml y se salta al Metodo
	 * constructorAlerta(). Si lo es se sale a la Actividad Gamas 2.- Si es un nivel, se abre un dialogo para pasar al siguiente nivel o salir
	 * */
	private void PartidaFinalizada() {
		xml = new XMLParser(contexto);
		String partida = Preferencias.getTipoPartida(contexto);

		if (partida.equals(EtiquetasXML.getGama())) { // si es gama actualizamos el nivel

			nivelEnJuego = Preferencias.getNivelActual(contexto);
			ultimoNivelSuperado = Preferencias.getUltimoNivelSuperado(contexto);
			nivelesGama = Preferencias.getCantNivelesGama(contexto);
			temaActual = Preferencias.getNombrePartida(contexto);

			xml = new XMLParser(contexto);

			String ruta = Rutas.getRutaArchivos() + temaActual + Rutas.getOpcionesJuego();

			if (nivelEnJuego < ultimoNivelSuperado) { // No estamos jugando el nivel superado, si no uno anterior
				nivelEnJuego++;
				xml.escribeOpcionesJuego(ruta, nivelesGama, ultimoNivelSuperado, EtiquetasXML.getSi());

			} else {
				if (nivelEnJuego == ultimoNivelSuperado && ultimoNivelSuperado < nivelesGama) { // Es decir, se supero nivel que no es el ultimo de la gama
					nivelEnJuego++;
					xml.escribeOpcionesJuego(ruta, nivelesGama, ++ultimoNivelSuperado, EtiquetasXML.getSi());
				} else {
					nivelEnJuego = 1;
					gamaSuperada = true;
				}
			}
		}
		MensajesEmergentes.PartidaGanada(contexto, gamaSuperada, tamanoImagenesReforazadoras, temaActual, nivelEnJuego, ultimoNivelSuperado);
	}

	private static int colorLinea() {
		if (contadorColores == 7)
			contadorColores = 0;
		int aux;
		switch (contadorColores) {
		case 0:
			aux = Color.BLACK;
			break;
		case 1:
			aux = Color.BLUE;
			break;
		case 2:
			aux = Color.CYAN;
			break;
		case 3:
			aux = Color.GRAY;
			break;
		case 4:
			aux = Color.GREEN;
			break;
		case 5:
			aux = Color.MAGENTA;
			break;
		case 6:
			aux = Color.RED;
			break;
		default:
			aux = Color.BLUE;
			break;
		}
		contadorColores++;
		return aux;
	}

	/**
	 * Metodo que se encarga de quela vista tenga el tamano indicado
	 * @param view
	 * @param newWidth
	 * @param newHeight
	 */
	private void resizeView(View view, int newWidth, int newHeight) {
		try {
			Constructor<? extends LayoutParams> ctor = view.getLayoutParams().getClass().getDeclaredConstructor(int.class, int.class);
			view.setLayoutParams(ctor.newInstance(newWidth, newHeight));
		} catch (Exception e) {
			Log.e("Error en TableroVista -> resizeView", e.getMessage());
		}
	}
}