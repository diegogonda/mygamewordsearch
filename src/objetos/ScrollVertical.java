package objetos;

public class ScrollVertical {

	private int y_0; // punto inicial de la vista que se va a visualizar
	private int y_v; // Punto ultimo de la vista que se va a poder visualizar
	private int y_f; // Último pixel que se va a pintar en la vista. Este punto sólo se visualizará si y_v = y_f
	private int y_m; //Marca la cantidad de movimiento que hemos hecho, evita que al dejar de pulsar y volver hacerlo la vista se reinicie en y_0 = 0

	public ScrollVertical() {

	}

	/**
	 * Este método calcula cual es que tamaño del desplazamiento que hemos generado sobre la vista
	 * 
	 * @param yInicial Punto donde el usuario pulso con el dedo en la pantalla
	 * @param yFinal Punto donde el usuario está pulsando actualmente, o en su defecto, donde dejo de pulsar
	 * @param actualizaMovimiento 	esta variable se usa para saber cuando actualizar el valor de y_m. Con y_m calculamos la distancia que 
	 * 								hemos movido el dedo. Pero esta distancia no se puede actualizar si aun se esta realizando el movimiento
	 * 								ya que la vista se desplazará demasiado rápido. Sólo se debe actualizar cuando se haga el gesto ACTION_UP
	 * 								Es decir:
	 * 									si ACTION_UP actualizaMovimiento = true
	 * 									sino actualizaMovimiento = false
	 */
	public void desplazamiento(int yInicial, int yFinal, boolean actualizaMovimiento) {
		if (y_f <= y_v) return; //Es decir, la vista no necesita scroll cuando la zona visible es mayor o igual que el contenido representado
		
		if (yInicial < yFinal) { //Scroll hacia abajo
			if (y_0 >= 0){
				y_0 = 0;
				y_m = 0;
				return;
			}
			else 
				y_0 = y_m +yFinal - yInicial;

		} else if (yInicial > yFinal) { //Scroll hacia arriba
			
			if ((y_v + Math.abs(y_0)) >= y_f){ //Comprobamos que no nos pasamos de la altura máxima de la vista
				// actualizamos el valor del movimiento para que cuando se vuelva a pulsar sobre la pantalla se mantenga
				//la vista en su posicion hasta que se mueva el dedo
				y_m = y_v - y_f; 
				return;
			}
			else //Si no lo hacemos actualizamos el valor de y_0
				y_0 = y_m +yFinal - yInicial;
		}
		y_0 = y_m + yFinal - yInicial;
		if (actualizaMovimiento)
			y_m = y_0;
	}

	/**
	 * @return the y_a
	 */
	public int getY_m() {
		return y_m;
	}

	/**
	 * @param y_a the y_a to set
	 */
	public void setY_m(int y_m) {
		this.y_m = y_m;
	}

	/**
	 * punto inicial de la vista que se va a visualizar
	 * 
	 * @return the y_0
	 */
	public int getY_0() {
		return y_0;
	}

	/**
	 * punto inicial de la vista que se va a visualizar
	 * 
	 * @param y_0
	 *            the y_0 to set
	 */
	public void setY_0(int y_0) {
		this.y_0 = y_0;
	}

	/**
	 * Punto ultimo de la vista que se va a poder visualizar
	 * 
	 * @return the y_v
	 */
	public int getY_v() {
		return y_v;
	}

	/**
	 * Punto ultimo de la vista que se va a poder visualizar
	 * 
	 * @param y_v
	 *            the y_v to set
	 */
	public void setY_v(int y_v) {
		this.y_v = y_v;
	}

	/**
	 * Último pixel que se va a pintar en la vista. Este punto sólo se visualizará si y_v = y_f
	 * 
	 * @return the y_f
	 */
	public int getY_f() {
		return y_f;
	}

	/**
	 * Último pixel que se va a pintar en la vista. Este punto sólo se visualizará si y_v = y_f
	 * 
	 * @param y_f
	 *            the y_f to set
	 */
	public void setY_f(int y_f) {
		this.y_f = y_f;
	}
}
