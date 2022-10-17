package vistas;

import objetos.Actividades;
import objetos.EtiquetasXML;
import actividades.AgregarPartidasActivity;
import actividades.ConfiguraActivity;
import actividades.MainActivity;
import actividades.NuevasPalabrasActivity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import es.uvigo.gti.mygamewordsearch.R;

public class BannerSuperiorVista extends View {

	private int numeroColumnas;
	private Paint lineaPaint = new Paint();
	private int opcionElegida = 0;
	private int ancho;
	private int xIni;
	private int xFin;
	private int anchoCuadros;
	private int paddingImagenes;
	private int paddingLateralIzquierdo;
	private int ajusteEsquinas;
	private int tamañoImagenes;
	private Context contexto;
	private int largoLinea;

	Bitmap imagen = BitmapFactory.decodeResource(getResources(), R.drawable.principal_bar, null);

	public BannerSuperiorVista(Context context, AttributeSet attrs) {
		super(context, attrs);

		contexto = context;
		float dp = getResources().getDisplayMetrics().density;
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BannerSuperiorVista, 0, 0);
		try {

			lineaPaint.setColor(a.getColor(R.styleable.BannerSuperiorVista_color_linea, Color.BLACK));
			lineaPaint.setStrokeWidth(3);
			paddingImagenes = a.getDimensionPixelSize(R.styleable.BannerSuperiorVista_padding_fotos, 4);
			tamañoImagenes = a.getDimensionPixelSize(R.styleable.BannerSuperiorVista_tamano_imagenes, 4);
			largoLinea = a.getDimensionPixelSize(R.styleable.BannerSuperiorVista_largo_linea, (int) (100 * dp));
			paddingLateralIzquierdo = a.getDimensionPixelSize(R.styleable.BannerSuperiorVista_padding_lateral_izquierdo, (int) (10 * dp));
			if (contexto.getClass() == MainActivity.class)
				opcionElegida = 0;
			if (contexto.getClass() == AgregarPartidasActivity.class || contexto.getClass() == NuevasPalabrasActivity.class)
				opcionElegida = 2;
			if (contexto.getClass() == ConfiguraActivity.class)
				opcionElegida = 3;
			numeroColumnas = 3;

		} finally {
			a.recycle();
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		ancho = getWidth();
		anchoCuadros = ancho / numeroColumnas;

		canvas = PintaCuadrosOpciones(canvas);
		canvas = PintaImagenes(canvas);

	}

	public boolean onTouchEvent(MotionEvent evento) {
		int x = (int) (evento.getX());
		int y = (int) (evento.getY());
		switch (evento.getAction()) {
		case MotionEvent.ACTION_DOWN: // Se ha pulsado la pantalla
			if (contexto.getClass() == NuevasPalabrasActivity.class) {
				Toast.makeText(contexto, R.string.botonera_deshabilitada, Toast.LENGTH_LONG).show();
				return false;
			}
			if (x > 0 && y > 0 && x < ancho / 3 && y < anchoCuadros) {
				Actividades.iniciarActividad(MainActivity.class, contexto);
			} else if ((x > ancho / 3) && (y > 0) && (x < ancho * 2 / 3) && (y < anchoCuadros)) {
				Actividades.iniciarActividadNuevaPartida(AgregarPartidasActivity.class, contexto, EtiquetasXML.getAgregarGama(), false);

			} else if ((x > ancho * 2 / 3) && (y > 0) && (x < ancho) && (y < anchoCuadros)) {
				if (opcionElegida != 3) {
					Actividades.iniciarActividad(ConfiguraActivity.class, contexto);
				}
			}
			break;

		}
		return true;
	}

	private Canvas PintaImagenes(Canvas canvas) {
		Bitmap bmpBar = BitmapFactory.decodeResource(getResources(), R.drawable.principal_suma, null);
		Bitmap bmpGears = BitmapFactory.decodeResource(getResources(), R.drawable.principal_gears, null);
		Bitmap bmpPad = BitmapFactory.decodeResource(getResources(), R.drawable.principal_pad, null);

		if (contexto.getClass() == NuevasPalabrasActivity.class) {
			bmpBar = BitmapFactory.decodeResource(getResources(), R.drawable.principal_suma_blanco_negro, null);
			bmpGears = BitmapFactory.decodeResource(getResources(), R.drawable.principal_gears_blanco_negro, null);
			bmpPad = BitmapFactory.decodeResource(getResources(), R.drawable.principal_pad_blanco_negro, null);
		}

		if (bmpBar != null && bmpGears != null && bmpPad != null) {
			int xIni = 0 + paddingImagenes + paddingLateralIzquierdo;
			int yIni = 0 + paddingImagenes;
			int xFin = tamañoImagenes - paddingImagenes + paddingLateralIzquierdo;
			int yFin = tamañoImagenes - paddingImagenes;
			canvas.drawBitmap(bmpPad, null, new Rect(xIni, yIni, xFin, yFin), null);

			xIni = ancho / 3 + paddingImagenes;
			yIni = paddingImagenes;
			xFin = ancho * 2 / 3 - paddingImagenes;
			canvas.drawBitmap(bmpBar, null, new Rect(xIni, yIni, xFin, yFin), null);

			xIni = ancho * 2 / 3 + paddingImagenes;
			yIni = paddingImagenes;
			xFin = ancho - paddingImagenes;
			canvas.drawBitmap(bmpGears, null, new Rect(xIni, yIni, xFin, yFin), null);
		}

		return canvas;
	}

	private Canvas PintaCuadrosOpciones(Canvas canvas) {

		switch (opcionElegida) {
		case 0:
			xIni = ancho / 3 - ajusteEsquinas;
			canvas.drawLine(xIni, largoLinea, getWidth(), largoLinea, lineaPaint);
			break;
		case 1:
			break;
		case 2:
			xIni = 0;
			xFin = ancho / 3;
			canvas.drawLine(xIni, largoLinea, xFin, largoLinea, lineaPaint);
			xIni = ancho * 2 / 3;
			xFin = getWidth();
			canvas.drawLine(xIni, largoLinea, xFin, largoLinea, lineaPaint);
			break;
		case 3:
			xIni = 0;
			xFin = ancho * 2 / 3;
			canvas.drawLine(xIni, largoLinea, xFin, largoLinea, lineaPaint);
			break;
		}
		for (int i = 1; i < numeroColumnas; i++) {
			int x = ancho * i / numeroColumnas;
			canvas.drawLine(x, 0, x, largoLinea, lineaPaint);
		}

		return canvas;
	}
}