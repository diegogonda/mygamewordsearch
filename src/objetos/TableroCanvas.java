package objetos;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TableroCanvas {
	private Canvas canvas = new Canvas();
	private Paint seleccionadaPaint = new Paint();
	private int xInicial;
	private int yInicial;
	private int xFinal;
	private int yFinal;
	private int orientacion;

	public TableroCanvas() {

	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	public Canvas PintaTablero(ArrayList<PosicionesPalabras> pp, int ancho, int N, char tableroJuego[][], Paint textoPaint, int margenDerechoTablero, int margenIzquierdoTablero, int margenSuperiorTablero, int margenInferiorTablero) {

		int separacionLetras = canvas.getWidth() - margenDerechoTablero - margenIzquierdoTablero; // El -10 es para centrar
		// bien el tablero
		separacionLetras = separacionLetras / (N - 1);
		for (int i = 1, indice1 = 0; i < N * 2; i += 2, indice1++) {
			int y = ancho * i / (2 * N) + margenSuperiorTablero / N;
			for (int j = 1, indice2 = 0; j < N * 2; j += 2, indice2++) {
				int x = ancho * j / (2 * N);
				String s = new StringBuilder().append("").append(tableroJuego[indice1][indice2]).toString();

				for (int k = 0; k < pp.size(); k++) {
					PosicionesPalabras aux = pp.get(k);
					if (aux.getxTableroInicial() == indice2 && aux.getyTableroInicial() == indice1) {
						aux.setxPixelInicial(x);
						aux.setyPixelInicial(y - margenSuperiorTablero / N);
						pp.set(k, aux);
					}
					if (aux.getxTableroFinal() == indice2 && aux.getyTableroFinal() == indice1) {
						aux.setxPixelFinal(x);
						aux.setyPixelFinal(y - margenSuperiorTablero / N);
						pp.set(k, aux);
					}
				}
				canvas.drawText(s, x, y, textoPaint);
			}
		}
		return canvas;
	}

	public Canvas PintaLineasSeleccionadas(ArrayList<PosicionesPalabras> pp, int anchoBarra, int barraPalabraSeleccionadaX, int barraPalabraSeleccionadaY, int barraPalabraSeleccionadaInclinadaX1, int barraPalabraSeleccionadaInclinadaX2) {
		for (PosicionesPalabras aux : pp) {
			if (aux.isElegida()) {
				seleccionadaPaint.setColor(aux.getColor());
				seleccionadaPaint.setStrokeWidth(anchoBarra);
				seleccionadaPaint.setAlpha(100);
				xInicial = aux.getxPixelInicial();
				yInicial = aux.getyPixelInicial();
				xFinal = aux.getxPixelFinal();
				yFinal = aux.getyPixelFinal();
				orientacion = aux.getOrientacion();
				AjustePosicionesLineas(barraPalabraSeleccionadaX, barraPalabraSeleccionadaY, barraPalabraSeleccionadaInclinadaX1, barraPalabraSeleccionadaInclinadaX2);
			}
		}
		return canvas;
	}

	public int ColorAleatorio() {

		int color = (int) (Math.random() * 7);
		switch (color) {
		case 0:
			return Color.BLACK;
		case 1:
			return Color.BLUE;
		case 2:
			return Color.CYAN;
		case 3:
			return Color.RED;
		case 4:
			return Color.GRAY;
		case 5:
			return Color.GREEN;
		case 6:
			return Color.YELLOW;		
		case 7:
			return Color.MAGENTA;
		}
		return -1;
	}

	private void AjustePosicionesLineas(int barraPalabraSeleccionadaX, int barraPalabraSeleccionadaY, int barraPalabraSeleccionadaInclinadaX1, int barraPalabraSeleccionadaInclinadaX2) {
		switch (orientacion) {
		case 0: // Palabras Horizontales
			int x_1 = xFinal + barraPalabraSeleccionadaX;
			int y_1 = yFinal;
			int x_0 = xInicial - barraPalabraSeleccionadaX;
			int y_0 = y_1;
			canvas.drawLine(x_0, y_0, x_1, y_1, seleccionadaPaint);
			break;
		case 1:
			x_1 = xFinal;
			y_1 = yFinal + barraPalabraSeleccionadaY;
			x_0 = x_1;
			y_0 = yInicial - barraPalabraSeleccionadaY;

			canvas.drawLine(x_0, y_0, x_1, y_1, seleccionadaPaint);
			break;
		case 2:
			x_1 = xFinal + barraPalabraSeleccionadaInclinadaX1;
			y_1 = yFinal;
			x_0 = xInicial + barraPalabraSeleccionadaInclinadaX1;
			y_0 = yInicial;
			int x_2 = xInicial - barraPalabraSeleccionadaInclinadaX2;
			int y_2 = EcuacionRectaPendiente(x_0, x_1, y_0, y_1, x_2);
			int y_3;
			int x_3 = xFinal + barraPalabraSeleccionadaInclinadaX2;
			y_3 = EcuacionRectaPendiente(x_0, x_1, y_0, y_1, x_3);
			canvas.drawLine(x_2, y_2, x_3, y_3, seleccionadaPaint);
			break;
		case 3:
			x_1 = xFinal - barraPalabraSeleccionadaInclinadaX1;
			y_1 = yFinal;
			x_0 = xInicial - barraPalabraSeleccionadaInclinadaX1;
			y_0 = yInicial;
			x_2 = xInicial + barraPalabraSeleccionadaInclinadaX2;
			y_2 = EcuacionRectaPendiente(x_0, x_1, y_0, y_1, x_2);
			x_3 = xFinal - barraPalabraSeleccionadaInclinadaX2;
			y_3 = EcuacionRectaPendiente(x_0, x_1, y_0, y_1, x_3);
			canvas.drawLine(x_2, y_2, x_3, y_3, seleccionadaPaint);

			break;
		}
	}

	private int EcuacionRectaPendiente(int x_0, int x_1, int y_0, int y_1, int x_2) {

		int m = (int) ((x_0 - x_1) / (y_0 - y_1));
		int y_2 = (int) (y_1 - (x_1 - x_2) / m);
		return y_2;
	}

	public void PistasMarcaPrimeraLetra(Canvas canvas, ArrayList<PosicionesPalabras> pp, int radioPista, int ajustePista, Paint pistaPaint) {
		for (PosicionesPalabras aux : pp) {
			if (!aux.isElegida()) {
				int x, y;
				if (aux.isInvertida()) {
					x = aux.getxPixelFinal();
					y = aux.getyPixelFinal();
				} else {
					x = aux.getxPixelInicial();
					y = aux.getyPixelInicial();

				}
				canvas.drawCircle(x, y, radioPista, pistaPaint);
			}
		}

	}

	public void PintaGrilla(int ancho, int N, Paint linea) {
		for (int i = N - 1; i > 0; i--) {
			// horizontales
			canvas.drawLine(0, ancho * i / N, ancho, ancho * i / N, linea);
			canvas.drawLine(ancho * i / N, 0, ancho * i / N, ancho, linea);
		}
	}
}
