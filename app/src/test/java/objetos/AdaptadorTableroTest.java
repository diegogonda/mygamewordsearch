package objetos;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import dominio.Celda;
import dominio.Orientacion;
import dominio.Palabra;
import dominio.PalabraColocada;
import dominio.Partida;
import dominio.PartidaIncoherenteException;
import dominio.Pista;
import dominio.Tablero;

/**
 * Tests del adaptador entre el dominio y los datos que consume la
 * presentación. No depende de Android, así que corre en la JVM.
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

	private PalabraColocada colocada(String texto, Orientacion orientacion, boolean invertida, Celda... celdas) {
		return new PalabraColocada(new Palabra(texto, Pista.nombre(texto)), orientacion, Arrays.asList(celdas), invertida);
	}

	/**
	 * Construye una partida, escribiendo en el tablero las palabras que se le
	 * pasan.
	 */
	private Partida partidaDe(PalabraColocada... colocadas) throws PartidaIncoherenteException {
		Tablero tablero = new Tablero(TAMANO);
		for (PalabraColocada colocada : colocadas) {
			List<Celda> celdas = colocada.getCeldas();
			String escrito = colocada.isInvertida() ? colocada.getPalabra().getTextoEnTableroInvertido() : colocada.getPalabra().getTextoEnTablero();
			for (int i = 0; i < celdas.size(); i++) {
				tablero.setCelda(celdas.get(i).getX(), celdas.get(i).getY(), escrito.charAt(i));
			}
		}
		return Partida.crea(tablero, Arrays.asList(colocadas));
	}

	private Celda[] enFila(int y, int desde, int cantidad) {
		Celda[] celdas = new Celda[cantidad];
		for (int i = 0; i < cantidad; i++) {
			celdas[i] = new Celda(desde + i, y);
		}
		return celdas;
	}

	@Test
	public void escribeDondeEmpiezaYDondeAcabaCadaPalabra() throws PartidaIncoherenteException {
		List<PosicionesPalabras> lista = palabras("SOL");

		AdaptadorTablero.TableroPreparado preparado = AdaptadorTablero.adapta(partidaDe(colocada("SOL", Orientacion.HORIZONTAL, false, enFila(3, 2, 3))), lista);

		PosicionesPalabras sol = preparado.getPalabras().get(0);
		assertEquals("columna de la primera letra", 2, sol.getxTableroInicial());
		assertEquals("fila de la primera letra", 3, sol.getyTableroInicial());
		assertEquals("columna de la última letra", 4, sol.getxTableroFinal());
		assertEquals("fila de la última letra", 3, sol.getyTableroFinal());
	}

	@Test
	public void escribeElVectorDeLasLetrasDeLaPalabra() throws PartidaIncoherenteException {
		List<PosicionesPalabras> lista = palabras("SOL");

		AdaptadorTablero.TableroPreparado preparado = AdaptadorTablero.adapta(partidaDe(colocada("SOL", Orientacion.HORIZONTAL, false, enFila(3, 2, 3))), lista);

		PosicionesPalabras sol = preparado.getPalabras().get(0);
		assertArrayEquals("columnas de las letras", new int[] { 2, 3, 4 }, sol.getxTableroLetras());
		assertArrayEquals("filas de las letras", new int[] { 3, 3, 3 }, sol.getyTableroLetras());
	}

	@Test
	public void elCodigoDeOrientacionEsElQueUsaLaPresentacion() throws PartidaIncoherenteException {
		List<PosicionesPalabras> lista = palabras("AB", "CD", "EF", "GH");

		Partida partida = partidaDe(colocada("AB", Orientacion.HORIZONTAL, false, enFila(0, 0, 2)),
				colocada("CD", Orientacion.VERTICAL, false, new Celda(2, 0), new Celda(2, 1)),
				colocada("EF", Orientacion.DIAGONAL_1, false, new Celda(3, 0), new Celda(4, 1)),
				colocada("GH", Orientacion.DIAGONAL_2, false, new Celda(5, 1), new Celda(4, 2)));

		AdaptadorTablero.TableroPreparado preparado = AdaptadorTablero.adapta(partida, lista);

		assertEquals("horizontal", 0, preparado.getPalabras().get(0).getOrientacion());
		assertEquals("vertical", 1, preparado.getPalabras().get(1).getOrientacion());
		assertEquals("diagonal principal", 2, preparado.getPalabras().get(2).getOrientacion());
		assertEquals("diagonal secundaria", 3, preparado.getPalabras().get(3).getOrientacion());
	}

	@Test
	public void marcaLaPalabraComoInvertidaCuandoVaDelReves() throws PartidaIncoherenteException {
		List<PosicionesPalabras> lista = palabras("SOL");

		AdaptadorTablero.TableroPreparado preparado = AdaptadorTablero.adapta(partidaDe(colocada("SOL", Orientacion.HORIZONTAL, true, enFila(3, 2, 3))), lista);

		assertTrue("la palabra va del revés", preparado.getPalabras().get(0).isInvertida());
	}

	@Test
	public void conservaElOrdenDeLasPalabrasDeLaPartida() throws PartidaIncoherenteException {
		List<PosicionesPalabras> lista = palabras("mercurio", "venus", "tierra");

		Partida partida = partidaDe(colocada("tierra", Orientacion.HORIZONTAL, false, enFila(0, 0, 6)),
				colocada("mercurio", Orientacion.HORIZONTAL, false, enFila(1, 0, 8)),
				colocada("venus", Orientacion.HORIZONTAL, false, enFila(2, 0, 5)));

		AdaptadorTablero.TableroPreparado preparado = AdaptadorTablero.adapta(partida, lista);

		assertEquals("mercurio", preparado.getPalabras().get(0).getPalabra());
		assertEquals("venus", preparado.getPalabras().get(1).getPalabra());
		assertEquals("tierra", preparado.getPalabras().get(2).getPalabra());
	}

	@Test
	public void dejaFueraLasPalabrasQueNoHanCabido() throws PartidaIncoherenteException {
		List<PosicionesPalabras> lista = palabras("tierra", "marte");

		AdaptadorTablero.TableroPreparado preparado = AdaptadorTablero.adapta(partidaDe(colocada("tierra", Orientacion.HORIZONTAL, false, enFila(0, 0, 6))), lista);

		assertEquals("sólo quedan las palabras colocadas", 1, preparado.getPalabras().size());
		assertEquals("tierra", preparado.getPalabras().get(0).getPalabra());
	}

	@Test
	public void cadaCopiaDeUnaPalabraVaASuPropioSitio() throws PartidaIncoherenteException {
		List<PosicionesPalabras> lista = palabras("SOL", "SOL");

		Partida partida = partidaDe(colocada("SOL", Orientacion.HORIZONTAL, false, enFila(0, 0, 3)),
				colocada("SOL", Orientacion.HORIZONTAL, false, enFila(1, 0, 3)));

		AdaptadorTablero.TableroPreparado preparado = AdaptadorTablero.adapta(partida, lista);

		assertEquals("las dos copias se colocan", 2, preparado.getPalabras().size());
		assertEquals("la primera copia", 0, preparado.getPalabras().get(0).getyTableroInicial());
		assertEquals("la segunda copia", 1, preparado.getPalabras().get(1).getyTableroInicial());
	}

	@Test
	public void lasPalabrasColocadasQuedanMarcadasComoMostradas() throws PartidaIncoherenteException {
		List<PosicionesPalabras> lista = palabras("SOL", "LUNA");
		lista.get(0).setMostradaPantalla(true);

		AdaptadorTablero.TableroPreparado preparado = AdaptadorTablero.adapta(partidaDe(colocada("SOL", Orientacion.HORIZONTAL, false, enFila(0, 0, 3))), lista);

		assertTrue("la palabra colocada se muestra", preparado.getPalabras().get(0).getMostradaPantalla());
		assertFalse("la palabra que no ha cabido no se muestra", lista.get(1).getMostradaPantalla());
	}
}
