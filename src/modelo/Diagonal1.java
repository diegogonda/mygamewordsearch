package modelo;

public class Diagonal1 {
	public static char [][] tableroJuego;
	String Palabra;
	private static int tamañoTablero;
	private static char letraRelleno;
	public static boolean palabraColocada;
	private int xInicial;
	private int yInicial;
	private int xFinal;
	private int yFinal;
	
	public Diagonal1 (String palabra, char [][] tblJuego, int tam, char lr){
		Palabra = palabra;
		tableroJuego = tblJuego;
		tamañoTablero = tam;
		letraRelleno = lr;
		colocaPalabra();
	}
	public void actualizaDatos (String palabra, char [][] tblJuego){
		Palabra = palabra;
		tableroJuego = tblJuego;
		colocaPalabra();
	}
	private void colocaPalabra() {

		int espacioValido = tamañoTablero - Palabra.length() + 1;
		/**
		 * Lo primero que hacemos es determinar en que espacio del vector irá alojada la última letra de la palabra
		 * */
		int ejeX = (int)(Math.random() * espacioValido + Palabra.length() -1);
		xInicial = ejeX;
		int ejeY = (int)(Math.random() * espacioValido + Palabra.length() -1);
		yInicial = ejeY;
		/**
		 * Una vez lo hemos hallado, determinamos donde irá alojada la primera letra
		 * */

		int vectorEjeX [] = new int [Palabra.length()];
		int vectorEjeY [] = new int [Palabra.length()];
		char vectorLetras [] = new char[Palabra.length()];
		int letrasColocadas = 0;
		boolean correcto = true;
		
		int w = 0;
		for (int k = (Palabra.length() - 1); k >= 0; k--, ejeX--, ejeY--, w++){
			if (tableroJuego [ejeY][ejeX] == letraRelleno || tableroJuego[ejeY][ejeX] == Palabra.charAt(k)){
				vectorEjeX[w] = ejeX;
				vectorEjeY[w] = ejeY;
				vectorLetras[w] = tableroJuego[ejeY][ejeX];
				tableroJuego[ejeY][ejeX] = Palabra.charAt(k);
				letrasColocadas++;
			}
			else {
				/**
				 * Encontró un caracter distinto de la letra de relleno, por lo que vamos a dejar el tablero de juego tal y como 
				 * estaba
				 * */
				for (int i = 0; i < letrasColocadas; i++){
				//	int aux = letrasColocadas - i;
				//	tableroJuego [vectorEjeY[aux]][vectorEjeX[aux]] = vectorLetras[aux];
					tableroJuego [vectorEjeY[i]][vectorEjeX[i]] = vectorLetras[i];
				}
				correcto = false;
				break;
			}
		}


		if (correcto){
			xFinal = vectorEjeX[0];
			yFinal = vectorEjeY[0];
			yInicial = vectorEjeY[w-1];
			xInicial = vectorEjeX[w-1];

			palabraColocada = true;
		}
		else palabraColocada = false;
	}
	public boolean resultado (){
		return palabraColocada;
	}
	public char [][] devuelveTableroJuego (){
		return tableroJuego;
	}
	public int getxInicial() {
		return xInicial;
	}
	public void setxInicial(int xInicial) {
		this.xInicial = xInicial;
	}
	public int getyInicial() {
		return yInicial;
	}
	public void setYInicial(int yInicial) {
		this.yInicial = yInicial;
	}
	public int getxFinal() {
		return xFinal;
	}
	public void setxFinal(int xFinal) {
		this.xFinal = xFinal;
	}
	public int getyFinal() {
		return yFinal;
	}
	public void setyFinal(int yFinal) {
		this.yFinal = yFinal;
	}
	public void setyInicial(int yInicial) {
		this.yInicial = yInicial;
	}
	
}