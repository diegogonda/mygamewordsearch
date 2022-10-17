package vistas;

import java.util.ArrayList;
import listViewPersonalizado.ConfiguracionPartida;

import objetos.Actividades;
import objetos.EtiquetasXML;
import objetos.Gamas;
import objetos.Imagenes;
import objetos.Preferencias;
import objetos.Rutas;
import xml.XMLParser;

import actividades.JuegoActivity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import es.uvigo.gti.mygamewordsearch.R;

public class GamasVista extends View {

	private Context contexto; // Contexto de la aplicacion

	/*
	 * Variables que nos proporcionan información sobre el estado del juego
	 */
	private static String temaActual; // Nombre del tema que se va a jugar
	private int cantidadNiveles; // Cantidad de niveles que tiene el tema que se va a jugar
	private int ultimoNivelSuperado;// Ultimo nivel superado en el tema que se va a jugar

	/*
	 * Diferentes márgenes entre las imágenes de la vista
	 */
	private static int tamañoImagenesNumeros;
	private static int paddingSuperiorNumeros;
	private static int paddingLateralNumeros;

	/*
	 * Variable que nos permite acceder a las opciones guardadas dentro del archivo de configuracion de la vista de la actividad
	 * en este caso activity_gamas.xml
	 */
	private TypedArray a;

	/*
	 * Variable que nos permite guarda la información de entre que píxeles está cada partida referenciada en la vista
	 */
	Gamas gamas[];

	public GamasVista(Context context, AttributeSet attrs) {
		super(context, attrs);
		contexto = context;

		/**
		 * Recogemos el contenido del tema actual
		 */
		temaActual = Preferencias.getNombrePartida(contexto);
		XMLParser xml = new XMLParser(contexto);
		ArrayList<String> juego = xml.leeOpcionesJuego(Rutas.rutaOpcionesJuego(temaActual));
		cantidadNiveles = Integer.parseInt(juego.get(1));
		ultimoNivelSuperado = Integer.parseInt(juego.get(2));

		gamas = new Gamas[cantidadNiveles];
		/**
		 * Opciones de estética de la vista
		 */
		float dp = getResources().getDisplayMetrics().density;
		a = contexto.getTheme().obtainStyledAttributes(attrs, R.styleable.GamasVista, 0, 0);
		tamañoImagenesNumeros = a.getDimensionPixelOffset(R.styleable.GamasVista_tamano_numeros, (int) (60 * dp));
		paddingSuperiorNumeros = a.getDimensionPixelOffset(R.styleable.GamasVista_padding_superior_numeros, (int) (50 * dp));
		paddingLateralNumeros = a.getDimensionPixelOffset(R.styleable.GamasVista_padding_lateral_numeros, (int) (50 * dp));
	}

	/**
	 * Método OnDraw. Se encarga de pintar en la pantalla la matriz de números que representan cada partida por separado
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int xIni = paddingLateralNumeros;
		int xFin = xIni + tamañoImagenesNumeros;
		int yIni = paddingSuperiorNumeros;
		int yFin = yIni + tamañoImagenesNumeros;
		int bordesImagenesCentrales = (getWidth() - tamañoImagenesNumeros * 3 + paddingLateralNumeros * 2) / 4;

		for (int i = 0; i < cantidadNiveles; i++) {
			Drawable aux = (i < ultimoNivelSuperado) ? Imagenes.getIdImagenes(i, contexto) : Imagenes.getIdImagenes(-1, contexto);
			aux.setBounds(new Rect(xIni, yIni, xFin, yFin));
			cargaGamas(i, xIni, xFin, yIni, yFin);
			aux.draw(canvas);
			xIni = xFin + bordesImagenesCentrales;
			xFin = xIni + tamañoImagenesNumeros;

			if (i == 2 || i == 5) {
				yIni = yFin + bordesImagenesCentrales;
				yFin = yIni + tamañoImagenesNumeros;
				xIni = paddingLateralNumeros;
				xFin = xIni + tamañoImagenesNumeros;
			}
		}
	}

	/**
	 * Metodo que guarda los vectores en los que está representada cada juego dentro de la vista de juego
	 * 
	 * @param i
	 * @param xIni
	 * @param xFin
	 * @param yIni
	 * @param yFin
	 */
	private void cargaGamas(int i, int xIni, int xFin, int yIni, int yFin) {
		Gamas aux = new Gamas();
		aux.setNivel(i+1);
		aux.setxInicial(xIni);
		aux.setyInicial(yIni);
		aux.setxFinal(xFin);
		aux.setyFinal(yFin);
		gamas[i] = aux;
	}

	/**
	 * Método onTouchEvent. Calcula que partida hemos seleccionado.
	 * Si la partida seleccionada no se ha superado envia un mensaje de error a través de un Toast
	 * Si la partida es jugable, lanza su ejecución
	 * @param evento
	 * @return true
	 */
	public boolean onTouchEvent(MotionEvent evento) {
		switch (evento.getAction()) {
		case MotionEvent.ACTION_DOWN: // Se ha pulsado la pantalla
			break;
		case MotionEvent.ACTION_UP:
			int x = (int) (evento.getX());
			int y = (int) (evento.getY());
			for (Gamas g : gamas) {
				if (x > g.getxInicial() && x < g.getxFinal() && y > g.getyInicial() && y < g.getyFinal()) { // Evaluamos que partida hemos intentado seleccionar
					if (g.getNivel() > ultimoNivelSuperado)
						Toast.makeText(contexto, R.string.error_nivel_no_superador_gamas, Toast.LENGTH_LONG).show();
					else { // Generamos el tablero con el que vamos a jugar
						generadorPartida(g.getNivel());
						break;
					}
				}
			}
			break;
		}
		return true;
	}

	/**
	 * Método que crea el entorno para una partida nueva.
	 * Recibe el nivel de juego y carga los valores con los que crear el nuevo juego
	 * 
	 * @param nivel
	 *            nivel del juego al que se va a jugar
	 */
	private void generadorPartida(int nivel) {
		String ruta = Rutas.getRutaArchivos() + temaActual + Rutas.getNivel() + nivel + Rutas.getConfiguracionPartida();
		XMLParser xml = new XMLParser(contexto);
		ConfiguracionPartida cp = xml.leerPartidasDefinidas(ruta, 100, 100);

		Preferencias.setTamañoTablero(String.valueOf(cp.getTamaño()), contexto);
		Preferencias.setPalabrasInvertidas(cp.getInvertida().equals(EtiquetasXML.getSi()) ? true : false, contexto);
		Preferencias.setTipoPista(cp.getTipoPista(), contexto);
		Preferencias.setOrientacionPalabras(cp.getOrientacion(), contexto);
		Preferencias.setCantNivelesGama(cantidadNiveles, contexto);
		Preferencias.setNivelActual(nivel, contexto);
		Preferencias.setUltimoNivelSuperado(ultimoNivelSuperado, contexto);
		Preferencias.setTipoPartida(EtiquetasXML.getGama(), contexto);
		Actividades.iniciarActividad(JuegoActivity.class, contexto);
	}
}
