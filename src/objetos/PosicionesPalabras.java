package objetos;

public class PosicionesPalabras {
	//datos de la palabra
	private int id;
	private String palabra;
	private String pistaImagen;
	private String pistaAudio;
	private String pistaCategoria;
	private String pistaDescripcion;
	
	//Datos relativos a su representacion en pantalla
	private boolean elegida = false;
	private boolean mostradaPantalla = false;
	private int orientacion;
	private boolean invertida = false;
	
	//posición en la pantalla
	private int xPixelInicial;
	private int yPixelInicial;
	private int xPixelFinal;
	private int yPixelFinal;
	private int xPixelesLetras[];
	private int yPixelesLetras[];
	
	//posición en la matriz
	private int xTableroFinal;
	private int yTableroFinal;
	private int xTableroInicial;
	private int yTableroInicial;
	private int xTableroLetras[];
	private int yTableroLetras[];
	
	/**
	 * @return el xPixelesLetras
	 */
	public int[] getxPixelesLetras() {
		return xPixelesLetras;
	}

	/**
	 * @param xPixelesLetras el xPixelesLetras a establecer
	 */
	public void setxPixelesLetras(int[] xPixelesLetras) {
		this.xPixelesLetras = xPixelesLetras;
	}

	/**
	 * @return el yPixelesLetras
	 */
	public int[] getyPixelesLetras() {
		return yPixelesLetras;
	}

	/**
	 * @param yPixelesLetras el yPixelesLetras a establecer
	 */
	public void setyPixelesLetras(int[] yPixelesLetras) {
		this.yPixelesLetras = yPixelesLetras;
	}

	/**
	 * @return el xTableroLetras
	 */
	public int[] getxTableroLetras() {
		return xTableroLetras;
	}

	/**
	 * @param xTableroLetras el xTableroLetras a establecer
	 */
	public void setxTableroLetras(int[] xTableroLetras) {
		this.xTableroLetras = xTableroLetras;
	}

	/**
	 * @return el yTableroLetras
	 */
	public int[] getyTableroLetras() {
		return yTableroLetras;
	}

	/**
	 * @param yTableroLetras el yTableroLetras a establecer
	 */
	public void setyTableroLetras(int[] yTableroLetras) {
		this.yTableroLetras = yTableroLetras;
	}

	//Color de la línea que marca la palabra seleccionada
	private int color;
	
	
	public int getOrientacion() {
		return orientacion;
	}

	public void setOrientacion(int orientacion) {
		this.orientacion = orientacion;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public PosicionesPalabras (){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPalabra() {
		return palabra;
	}

	public void setPalabra(String palabra) {
		this.palabra = palabra;
	}

	/**
	 * @return el pistaImagen
	 */
	public String getPistaImagen() {
		return pistaImagen;
	}

	/**
	 * @param pistaImagen el pistaImagen a establecer
	 */
	public void setPistaImagen(String pistaImagen) {
		this.pistaImagen = pistaImagen;
	}

	/**
	 * @return el pistaAudio
	 */
	public String getPistaAudio() {
		return pistaAudio;
	}

	/**
	 * @param pistaAudio el pistaAudio a establecer
	 */
	public void setPistaAudio(String pistaAudio) {
		this.pistaAudio = pistaAudio;
	}

	/**
	 * @return el pistaCategoria
	 */
	public String getPistaCategoria() {
		return pistaCategoria;
	}

	/**
	 * @param pistaCategoria el pistaCategoria a establecer
	 */
	public void setPistaCategoria(String pistaCategoria) {
		this.pistaCategoria = pistaCategoria;
	}

	/**
	 * @return el pistaDescripcion
	 */
	public String getPistaDescripcion() {
		return pistaDescripcion;
	}

	/**
	 * @param pistaDescripcion el pistaDescripcion a establecer
	 */
	public void setPistaDescripcion(String pistaDescripcion) {
		this.pistaDescripcion = pistaDescripcion;
	}

	/**
	 * @param mostradaPantalla el mostradaPantalla a establecer
	 */
	public void setMostradaPantalla(boolean mostradaPantalla) {
		this.mostradaPantalla = mostradaPantalla;
	}

	public boolean isElegida() {
		return elegida;
	}

	public void setElegida(boolean elegida) {
		this.elegida = elegida;
	}

	public int getxPixelInicial() {
		return xPixelInicial;
	}

	public void setxPixelInicial(int xPixelInicial) {
		this.xPixelInicial = xPixelInicial;
	}

	public int getyPixelInicial() {
		return yPixelInicial;
	}

	public void setyPixelInicial(int yPixelInicial) {
		this.yPixelInicial = yPixelInicial;
	}

	public int getxTableroFinal() {
		return xTableroFinal;
	}

	public void setxTableroFinal(int xTableroFinal) {
		this.xTableroFinal = xTableroFinal;
	}

	public int getyTableroFinal() {
		return yTableroFinal;
	}

	public void setyTableroFinal(int yTableroFinal) {
		this.yTableroFinal = yTableroFinal;
	}

	public int getxPixelFinal() {
		return xPixelFinal;
	}

	public void setxPixelFinal(int xPixelFinal) {
		this.xPixelFinal = xPixelFinal;
	}

	public int getyPixelFinal() {
		return yPixelFinal;
	}

	public void setyPixelFinal(int yPixelFinal) {
		this.yPixelFinal = yPixelFinal;
	}

	public int getxTableroInicial() {
		return xTableroInicial;
	}

	public void setxTableroInicial(int xTableroIicial) {
		this.xTableroInicial = xTableroIicial;
	}

	public int getyTableroInicial() {
		return yTableroInicial;
	}

	public void setyTableroInicial(int yTableroInicial) {
		this.yTableroInicial = yTableroInicial;
	}

	public boolean getMostradaPantalla() {
		return mostradaPantalla;
	}

	/**
	 * @return el invertida
	 */
	public boolean isInvertida() {
		return invertida;
	}

	/**
	 * @param invertida el invertida a establecer
	 */
	public void setInvertida(boolean invertida) {
		this.invertida = invertida;
	}
	
}
