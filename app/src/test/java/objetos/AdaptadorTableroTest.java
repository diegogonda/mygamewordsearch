package objetos;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import modelo.Celda;
import modelo.Orientacion;
import modelo.PosicionPalabra;
import modelo.Tablero;
import modelo.TableroGenerado;

/**
 * Tests del adaptador entre el motor de tableros y los datos que consume la
 * presentación. No depende de Android, así que se ejecuta en la JVM.
 */
public class AdaptadorTableroTest {

	private static final int TAMANO = 9;

	private List<PosicionesPalabras> palabras(String... nombres) {
		List<PosicionesPalabras> lista = new ArrayList<PosicionesPalabras>();
		for (String nombre : nombres) {
			PosicionesPalabras palabra = new PosicionesPalabras();
			palabra.setPalabra(nombre);
			lista.add(palabra);
		}
		return lista;
	}

	private TableroGenerado tablero(PosicionPalabra... posiciones) {
		return new TableroGenerado(new Tablero(TAMANO), Arrays.asList(posiciones));
	}

	private PosicionPalabra posicion(String palabra, Orientacion orientacion, Celda... celdas) {
		return new PosicionPalabra(palabra, orientacion, Arrays.asList(celdas), false);
	}

	@Test
	public void escribeDondeEmpiezaYDondeAcabaCadaPalabra() {
		List<PosicionesPalabras> lista = palabras("SOL");

		AdaptadorTablero.TableroPreparado preparado = AdaptadorTablero.adapta(tablero(posicion("SOL", Orientacion.HORIZONTAL, new Celda(2, 3), new Celda(3, 3), new Celda(4, 3))), lista);

		PosicionesPalabras sol = preparado.getPalabras().get(0);
		assertEquals("columna de la primera letra", 2, sol.getxTableroInicial());
		assertEquals("fila de la primera letra", 3, sol.getyTableroInicial());
		assertEquals("columna de la última letra", 4, sol.getxTableroFinal());
		assertEquals("fila de la última letra", 3, sol.getyTableroFinal());
	}

	@Test
	public void escribeElVectorDeLasLetrasDeLaPalabra() {
		List<PosicionesPalabras> lista = palabras("SOL");

		AdaptadorTablero.TableroPreparado preparado = AdaptadorTablero.adapta(tablero(posicion("SOL", Orientacion.HORIZONTAL, new Celda(2, 3), new Celda(3, 3), new Celda(4, 3))), lista);

		PosicionesPalabras sol = preparado.getPalabras().get(0);
		assertArrayEquals("columnas de las letras", new int[] { 2, 3, 4 }, sol.getxTableroLetras());
		assertArrayEquals("filas de las letras", new int[] { 3, 3, 3 }, sol.getyTableroLetras());
	}

	@Test
	public void elCodigoDeOrientacionEsElQueUsaLaPresentacion() {
		List<PosicionesPalabras> lista = palabras("A", "B", "C", "D");

		AdaptadorTablero.TableroPreparado preparado = AdaptadorTablero.adapta(tablero(posicion("A", Orientacion.HORIZONTAL, new Celda(0, 0)),
				posicion("B", Orientacion.VERTICAL, new Celda(1, 0)),
				posicion("C", Orientacion.DIAGONAL_1, new Celda(2, 0)),
				posicion("D", Orientacion.DIAGONAL_2, new Celda(3, 0))), lista);

		assertEquals("horizontal", 0, preparado.getPalabras().get(0).getOrientacion());
		assertEquals("vertical", 1, preparado.getPalabras().get(1).getOrientacion());
		assertEquals("diagonal principal", 2, preparado.getPalabras().get(2).getOrientacion());
		assertEquals("diagonal secundaria", 3, preparado.getPalabras().get(3).getOrientacion());
	}

	@Test
	public void marcaLaPalabraComoInvertidaCuandoVaDelReves() {
		List<PosicionesPalabras> lista = palabras("SOL");

		PosicionPalabra invertida = new PosicionPalabra("SOL", Orientacion.HORIZONTAL, Arrays.asList(new Celda(4, 3), new Celda(3, 3), new Celda(2, 3)), true);

		AdaptadorTablero.TableroPreparado preparado = AdaptadorTablero.adapta(tablero(invertida), lista);

		assertTrue("la palabra va del revés", preparado.getPalabras().get(0).isInvertida());
	}

	@Test
	public void conservaElOrdenDeLasPalabrasDeLaPartida() {
		List<PosicionesPalabras> lista = palabras("mercurio", "venus", "tierra");

		AdaptadorTablero.TableroPreparado preparado = AdaptadorTablero.adapta(tablero(posicion("tierra", Orientacion.HORIZONTAL, new Celda(0, 0)),
				posicion("mercurio", Orientacion.HORIZONTAL, new Celda(0, 1)),
				posicion("venus", Orientacion.HORIZONTAL, new Celda(0, 2))), lista);

		assertEquals("mercurio", preparado.getPalabras().get(0).getPalabra());
		assertEquals("venus", preparado.getPalabras().get(1).getPalabra());
		assertEquals("tierra", preparado.getPalabras().get(2).getPalabra());
	}

	@Test
	public void dejaFueraLasPalabrasQueNoHanCabido() {
		List<PosicionesPalabras> lista = palabras("tierra", "marte");

		AdaptadorTablero.TableroPreparado preparado = AdaptadorTablero.adapta(tablero(posicion("tierra", Orientacion.HORIZONTAL, new Celda(0, 0))), lista);

		assertEquals("sólo quedan las palabras colocadas", 1, preparado.getPalabras().size());
		assertEquals("tierra", preparado.getPalabras().get(0).getPalabra());
	}

	@Test
	public void cadaCopiaDeUnaPalabraVaASuPropioSitio() {
		List<PosicionesPalabras> lista = palabras("SOL", "SOL");

		AdaptadorTablero.TableroPreparado preparado = AdaptadorTablero.adapta(tablero(posicion("SOL", Orientacion.HORIZONTAL, new Celda(0, 0), new Celda(1, 0)),
				posicion("SOL", Orientacion.HORIZONTAL, new Celda(0, 1), new Celda(1, 1))), lista);

		assertEquals("las dos copias se colocan", 2, preparado.getPalabras().size());
		assertEquals("la primera copia", 0, preparado.getPalabras().get(0).getyTableroInicial());
		assertEquals("la segunda copia", 1, preparado.getPalabras().get(1).getyTableroInicial());
	}

	@Test
	public void lasPalabrasColocadasQuedanMarcadasComoMostradas() {
		List<PosicionesPalabras> lista = palabras("SOL", "LUNA");
		lista.get(0).setMostradaPantalla(true);

		AdaptadorTablero.TableroPreparado preparado = AdaptadorTablero.adapta(tablero(posicion("SOL", Orientacion.HORIZONTAL, new Celda(0, 0))), lista);

		assertTrue("la palabra colocada se muestra", preparado.getPalabras().get(0).getMostradaPantalla());
		assertFalse("la palabra que no ha cabido no se muestra", lista.get(1).getMostradaPantalla());
	}
}
