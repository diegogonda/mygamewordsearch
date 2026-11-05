package dominio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.Test;

/**
 * Verificación por propiedades del motor de tableros.
 *
 * En vez de comprobar casos concretos, se le pasa al motor un montón de
 * combinaciones y se comprueba que TODAS cumplen el contrato: el tablero
 * lleno, cada palabra legible en su orientación y el mismo tablero para la
 * misma semilla.
 *
 * El motor es lógica pura, así que esto corre en la JVM en menos de un
 * segundo.
 */
public class PropiedadesDelMotorTest {

	private static final int SEMILLAS = 40;

	/**
	 * Palabras de la partida, de longitudes distintas para que el motor tenga
	 * que ordenarlas.
	 */
	private static final String[] TEXTOS = { "mercurio", "venus", "tierra", "marte", "jupiter", "saturno", "urano", "neptuno", "sol", "luna", "mar", "casa" };

	private List<Palabra> palabras(int cantidad) {
		List<Palabra> palabras = new ArrayList<Palabra>();
		for (int i = 0; i < cantidad; i++) {
			String texto = TEXTOS[i % TEXTOS.length];
			palabras.add(new Palabra(texto, Pista.nombre(texto)));
		}
		return palabras;
	}

	private String lee(Tablero tablero, PalabraColocada colocada) {
		StringBuilder texto = new StringBuilder();
		for (Celda celda : colocada.getCeldas()) {
			texto.append(tablero.getCelda(celda.getX(), celda.getY()));
		}
		return texto.toString();
	}

	/**
	 * El contrato que tiene que cumplir cualquier tablero que produzca el
	 * motor.
	 */
	private void compruebaContrato(Partida partida, int tamano, List<Palabra> originales) {
		Tablero tablero = partida.getTablero();

		assertEquals("el tablero tiene el tamaño pedido", tamano, tablero.getTamano());

		java.util.Set<String> occupied = new java.util.HashSet<String>();
		for (PalabraColocada colocada : partida.getPalabras()) {
			for (Celda celda : colocada.getCeldas()) {
				occupied.add(celda.getX() + "," + celda.getY());
			}
		}

		for (int y = 0; y < tamano; y++) {
			for (int x = 0; x < tamano; x++) {
				char letra = tablero.getCelda(x, y);
				assertTrue("no puede quedar ninguna celda vacía", letra != Tablero.LETRA_RELLENO);
				if (!occupied.contains(x + "," + y)) {
					assertTrue("las celdas que no son de palabra se rellenan con el abecedario", letra >= 'a' && letra <= 'z');
				}
			}
		}

		for (PalabraColocada colocada : partida.getPalabras()) {
			String esperado = colocada.isInvertida() ? colocada.getPalabra().getTextoEnTableroInvertido() : colocada.getPalabra().getTextoEnTablero();
			assertEquals("la palabra tiene que estar escrita en el tablero", esperado, lee(tablero, colocada));
			assertTrue("la palabra tiene que venir de la lista", originales.contains(colocada.getPalabra()));
		}
	}

	@Test
	public void todosLosTablerosCumplenElContrato() {
		for (int tamano = ReglasDeTablero.TAMANO_MINIMO; tamano <= ReglasDeTablero.TAMANO_MAXIMO; tamano++) {
			for (ModoOrientacion modo : ModoOrientacion.values()) {
				for (int semilla = 0; semilla < SEMILLAS; semilla++) {
					compruebaUnaPartida(tamano, modo, semilla);
				}
			}
		}
	}

	private void compruebaUnaPartida(int tamano, ModoOrientacion modo, int semilla) {
		Random aleatorio = new Random(semilla);
		GeneradorTablero motor = new GeneradorTablero(new ColocadorPalabras(aleatorio), aleatorio);
		List<Palabra> palabras = palabras(tamano);
		ConfiguracionJuego configuracion = new ConfiguracionJuego(tamano, modo, true);

		try {
			Partida partida = motor.genera(configuracion, palabras);
			compruebaContrato(partida, tamano, palabras);
			assertTrue("una partida con todas las palabras es jugable", partida.getCantidadPalabras() >= ReglasDeTablero.paraTamano(tamano).minimoPalabras()
					|| partida.getCantidadPalabras() == palabras.size());
		} catch (TableroException e) {
			Partida parcial = e.getPartidaParcial();
			assertTrue("si el motor falla, tiene que llevar la mejor partida parcial", parcial != null);
			compruebaContrato(parcial, tamano, palabras);
		}
	}

	@Test
	public void laMismaSemillaDaElMismoTablero() {
		for (int tamano = ReglasDeTablero.TAMANO_MINIMO; tamano <= ReglasDeTablero.TAMANO_MAXIMO; tamano++) {
			for (int semilla = 0; semilla < 10; semilla++) {
				Partida primera = genera(tamano, ModoOrientacion.TODAS, semilla);
				Partida segunda = genera(tamano, ModoOrientacion.TODAS, semilla);
				assertTrue("con la misma semilla el tablero tiene que ser el mismo", Arrays.deepEquals(primera.getTablero().getCeldas(), segunda.getTablero().getCeldas()));
			}
		}
	}

	private Partida genera(int tamano, ModoOrientacion modo, int semilla) {
		Random aleatorio = new Random(semilla);
		GeneradorTablero motor = new GeneradorTablero(new ColocadorPalabras(aleatorio), aleatorio);
		try {
			return motor.genera(new ConfiguracionJuego(tamano, modo, false), palabras(tamano));
		} catch (TableroException e) {
			return e.getPartidaParcial();
		}
	}

	@Test
	public void lasPalabrasRepetidasVanCadaUnaASuSitio() {
		Random aleatorio = new Random(7);
		GeneradorTablero motor = new GeneradorTablero(new ColocadorPalabras(aleatorio), aleatorio);
		List<Palabra> palabras = new ArrayList<Palabra>();
		for (int i = 0; i < 6; i++) {
			palabras.add(new Palabra("sol", Pista.nombre("sol")));
		}

		try {
			Partida partida = motor.genera(new ConfiguracionJuego(9, ModoOrientacion.TODAS, false), palabras);

			for (PalabraColocada colocada : partida.getPalabras()) {
				assertEquals("sol", colocada.getTexto());
			}
			assertTrue("alguna copia debe colocarse", partida.getCantidadPalabras() > 0);
		} catch (TableroException e) {
			Partida parcial = e.getPartidaParcial();
			for (PalabraColocada colocada : parcial.getPalabras()) {
				assertEquals("sol", colocada.getTexto());
			}
		}
	}

	@Test
	public void unaPalabraInvertidaSeLeeDelReves() {
		for (int semilla = 0; semilla < 50; semilla++) {
			Random aleatorio = new Random(semilla);
			GeneradorTablero motor = new GeneradorTablero(new ColocadorPalabras(aleatorio), aleatorio);
			List<Palabra> palabras = new ArrayList<Palabra>();
			palabras.add(new Palabra("marte", Pista.nombre("marte")));
			palabras.add(new Palabra("venus", Pista.nombre("venus")));
			palabras.add(new Palabra("tierra", Pista.nombre("tierra")));

			Partida partida;
			try {
				partida = motor.genera(new ConfiguracionJuego(9, ModoOrientacion.TODAS, true), palabras);
			} catch (TableroException e) {
				partida = e.getPartidaParcial();
			}

			for (PalabraColocada colocada : partida.getPalabras()) {
				String esperado = colocada.isInvertida() ? colocada.getPalabra().getTextoEnTableroInvertido() : colocada.getPalabra().getTextoEnTablero();
				assertEquals("la palabra se lee como toca", esperado, lee(partida.getTablero(), colocada));
			}
		}
	}

	@Test
	public void elJugadorEncuentraLaPalabraQueArrastra() throws Exception {
		Partida partida = partidaDePrueba();

		Optional<PalabraColocada> encontrada = partida.buscaPalabra(new Celda(0, 0), new Celda(4, 0));

		assertTrue("el jugador tiene que encontrar la palabra", encontrada.isPresent());
		assertEquals("perro", encontrada.get().getTexto());
	}

	private Partida partidaDePrueba() throws PartidaIncoherenteException {
		Tablero tablero = new Tablero(9);
		String texto = "perro";
		for (int i = 0; i < texto.length(); i++) {
			tablero.setCelda(i, 0, texto.charAt(i));
		}
		List<Celda> celdas = new ArrayList<Celda>();
		for (int i = 0; i < texto.length(); i++) {
			celdas.add(new Celda(i, 0));
		}
		return Partida.crea(tablero, Arrays.asList(new PalabraColocada(new Palabra("perro", Pista.nombre("perro")), Orientacion.HORIZONTAL, celdas, false)));
	}
}
