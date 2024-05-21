package objetos;

import listViewPersonalizado.ConfiguracionPartida;
import xml.XMLParser;
import actividades.GamasActivity;
import actividades.JuegoActivity;
import actividades.MainActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.widget.ImageView;
import es.uvigo.gti.mygamewordsearch.R;

public class MensajesEmergentes {
	static boolean salir = false;
	private static MediaPlayer mediaPlayer;

	public MensajesEmergentes() {

	}
	
	/**
	 * Metodo que lanza un dialogo que nos da la opcion de finalizar una partida en juego
	 * @param contexto Contexto de la actividad en que estamos actualmente
	 * @return true or false
	 */
	public static boolean AtrasFinalizarJuego (final Context contexto){
		
//		String partida = Preferencias.getTipoPartida(contexto);
//		if (partida.equals(EtiquetasXML.getPartidaRapida())) {
			return Atras (contexto, R.string.quieres_finalizar_partida, R.string.si, R.string.no, MainActivity.class);
//		} else {
//			return Atras (contexto, R.string.quieres_finalizar_partida, R.string.si, R.string.no, GamasActivity.class);
//		}

	}

	/**
	 * Metodo que lanza un dialogo que nos da la opcion de Salir a otra actividad
	 * @param contexto Contexto de la actividad en que estamos actualmente
	 * @return true or false
	 */
	public static boolean AtrasSalir(final Context contexto) {
		return Atras (contexto, R.string.quieres_salir, R.string.si, R.string.no, MainActivity.class);
	}
	/**
	 * Metodo que realmente lanza el dialogo
	 * @param contexto Contexto de la actividad en que estamos actualmente
	 * @param titulo Titulo del dialogo
	 * @param positivo Mensaje en el boton positivo
	 * @param negativo Mensaje en el boton negativo
	 * @param clase 
	 * @return true or false
	 */
	private static boolean Atras (final Context contexto, int titulo, int positivo, int negativo, final Class<?> clase){
		// Esto es lo que hace mi bot�n al pulsar ir a atr�s
		AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
		builder.setTitle("¿Quieres volver al menú principal?");
		builder.setPositiveButton("Si", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				
				Actividades.iniciarActividad(clase, contexto);
			}
		});
		builder.setNegativeButton("No", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		AlertDialog alerta = builder.create();
		alerta.show();
		return true;
	}
	public static boolean PartidaGanada (final Context contexto, boolean gamaSuperada, int tamanoImagenesReforazadoras, final String temaActual, final int nivelEnJuego, final int ultimoNivelSuperado){
		AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
		if (Preferencias.isSonidosActivados(contexto)) {
			mediaPlayer = MediaPlayer.create(contexto, R.raw.positivo2);
			mediaPlayer.start();
		}
		int reforzador = codigoReforzador(contexto);
		if (reforzador != -1) {
			ImageView imagen = new ImageView(contexto);
			Bitmap bmp = BitmapFactory.decodeResource(contexto.getResources(), reforzador);
			bmp = Bitmap.createScaledBitmap(bmp, tamanoImagenesReforazadoras, tamanoImagenesReforazadoras, true);
			imagen.setImageBitmap(bmp);
			builder.setView(imagen);
		}
		if (gamaSuperada)
			builder.setTitle(R.string.ganaste_gama);
		else
			builder.setTitle(R.string.has_ganado);

		builder.setMessage(R.string.que_quieres_hacer);

		String botonIzq = contexto.getResources().getString((gamaSuperada) ? R.string.volver_a_jugar_gama : R.string.seguir_jugando);
		builder.setPositiveButton(botonIzq, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				VolverAJugar(contexto, temaActual, nivelEnJuego, ultimoNivelSuperado);
				dialog.cancel();
			}
		});
		builder.setNegativeButton(R.string.salir, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				Intent i = new Intent(contexto, MainActivity.class);
				contexto.startActivity(i);
				((Activity) contexto).finish();
				((Activity) contexto).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
dialog.cancel();
			}

		});
		AlertDialog alerta = builder.create();
		alerta.show();
		return true;
	}
	private static int codigoReforzador(Context contexto) {
		int aleatorio = (int) (Math.random() * 5 + 1);
		String tipoReforzador =  Preferencias.getReforzadorVisual(contexto);
		if (tipoReforzador.equals(EtiquetasXML.getReforzadorSinReforzador()))
			return -1;
		else if (tipoReforzador.equals(EtiquetasXML.getReforzadorColor())) {
			switch (aleatorio) {
			case 1:
				return R.drawable.color_1;
			case 2:
				return R.drawable.color_2;
			case 3:
				return R.drawable.color_3;
			case 4:
				return R.drawable.color_4;
			case 5:
				return R.drawable.color_5;
			case 6:
				return R.drawable.color_6;
			case 7:
				return R.drawable.color_7;
			case 8:
				return R.drawable.color_8;
			case 9:
				return R.drawable.color_9;
			default:
				return R.drawable.color_1;
			}
		} else if (tipoReforzador.equals(EtiquetasXML.getReforzadorBlancoNegro())) {
			switch (aleatorio) {
			case 1:
				return R.drawable.blanco_negro_1;
			case 2:
				return R.drawable.blanco_negro_2;
			case 3:
				return R.drawable.blanco_negro_3;
			case 4:
				return R.drawable.blanco_negro_4;
			case 5:
				return R.drawable.blanco_negro_5;
			case 6:
				return R.drawable.blanco_negro_6;
			case 7:
				return R.drawable.blanco_negro_7;
			case 8:
				return R.drawable.blanco_negro_8;
			case 9:
				return R.drawable.blanco_negro_9;
			default:
				return R.drawable.blanco_negro_1;
			}
		} else if (tipoReforzador.equals(EtiquetasXML.getReforzadorAltoContraste())) {
			switch (aleatorio) {
			case 1:
				return R.drawable.alto_contraste_1;
			case 2:
				return R.drawable.alto_contraste_2;
			case 3:
				return R.drawable.alto_contraste_3;
			case 4:
				return R.drawable.alto_contraste_4;
			case 5:
				return R.drawable.alto_contraste_5;
			case 6:
				return R.drawable.alto_contraste_6;
			case 7:
				return R.drawable.alto_contraste_7;
			case 8:
				return R.drawable.alto_contraste_8;
			case 9:
				return R.drawable.alto_contraste_9;
			default:
				return R.drawable.alto_contraste_1;
			}
		} else
			return -1;
	}
	/**
	 * Metodo que lanza la actividad Juego para volver a jugar
	 */
	private static void VolverAJugar(final Context contexto, String temaActual, int nivelEnJuego, int ultimoNivelSuperado) {
		String tipoPartida = Preferencias.getTipoPartida(contexto);
		if (tipoPartida.equals(EtiquetasXML.getPartidaRapida())) {
			Actividades.iniciarActividad(JuegoActivity.class, contexto);
			Actividades.iniciarActividad(JuegoActivity.class, contexto);
		} else if (tipoPartida.equals(EtiquetasXML.getGama())) {
			generadorPartida(contexto, temaActual, nivelEnJuego, ultimoNivelSuperado);
			Actividades.iniciarActividad(JuegoActivity.class, contexto);

		} else {
			Actividades.iniciarActividad(GamasActivity.class, contexto);
		}
	}
	private static void generadorPartida(final Context contexto, String temaActual, int nivelEnJuego, int ultimoNivelSuperado) {
		String ruta = Rutas.getRutaArchivos() + temaActual + Rutas.getNivel() + nivelEnJuego + Rutas.getConfiguracionPartida();
		XMLParser xml = new XMLParser(contexto);
		ConfiguracionPartida cp = xml.leerPartidasDefinidas(ruta, 100, 100);
		Preferencias.setTamanoTablero(String.valueOf(cp.getTamano()), contexto);
		Preferencias.setPalabrasInvertidas(cp.getInvertida().equals(EtiquetasXML.getSi()) ? true : false, contexto);
		Preferencias.setTipoPista(cp.getTipoPista(), contexto);
		Preferencias.setOrientacionPalabras(cp.getOrientacion(), contexto);
		Preferencias.setNivelActual(nivelEnJuego, contexto);
		Preferencias.setUltimoNivelSuperado(ultimoNivelSuperado, contexto);
		Preferencias.setTipoPartida(EtiquetasXML.getGama(), contexto);
		Actividades.iniciarActividad(JuegoActivity.class, contexto);
	}

}
