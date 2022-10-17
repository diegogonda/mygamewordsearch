package objetos;
/**
 * El objeto Gamas recoge información sobre las posiciones en que se pinta cada cuadrado dentro de la clase gamasVista.class
 * @author Diego
 *
 */
public class Gamas {
	private int nivel;
	private int xInicial;
	private int xFinal;
	private int yInicial;
	private int yFinal;
	public Gamas() {

	}
	/**
	 * @return the nivel
	 */
	public int getNivel() {
		return nivel;
	}
	/**
	 * @param nivel the nivel to set
	 */
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	/**
	 * @return the xInicial
	 */
	public int getxInicial() {
		return xInicial;
	}
	/**
	 * @param xInicial the xInicial to set
	 */
	public void setxInicial(int xInicial) {
		this.xInicial = xInicial;
	}
	/**
	 * @return the xFinal
	 */
	public int getxFinal() {
		return xFinal;
	}
	/**
	 * @param xFinal the xFinal to set
	 */
	public void setxFinal(int xFinal) {
		this.xFinal = xFinal;
	}
	/**
	 * @return the yInicial
	 */
	public int getyInicial() {
		return yInicial;
	}
	/**
	 * @param yInicial the yInicial to set
	 */
	public void setyInicial(int yInicial) {
		this.yInicial = yInicial;
	}
	/**
	 * @return the yFinal
	 */
	public int getyFinal() {
		return yFinal;
	}
	/**
	 * @param yFinal the yFinal to set
	 */
	public void setyFinal(int yFinal) {
		this.yFinal = yFinal;
	}
}