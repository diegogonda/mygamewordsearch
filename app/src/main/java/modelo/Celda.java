package modelo;

/**
 * Celda del tablero, identificada por sus coordenadas.
 */
public final class Celda {

	private final int x;
	private final int y;

	public Celda(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public boolean equals(Object otro) {
		if (this == otro) {
			return true;
		}
		if (!(otro instanceof Celda)) {
			return false;
		}
		Celda celda = (Celda) otro;
		return x == celda.x && y == celda.y;
	}

	@Override
	public int hashCode() {
		return x * 31 + y;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
