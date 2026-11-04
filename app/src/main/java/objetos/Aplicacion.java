package objetos;

import android.app.Application;

/**
 * Application de la aplicación.
 *
 * Se utiliza como punto único de inicialización del sistema de rutas, de modo
 * que cualquier actividad, servicio o vista puede resolver las rutas de datos
 * sin depender del orden en el que se hayan creado.
 */
public class Aplicacion extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Rutas.inicializar(this);
	}
}
