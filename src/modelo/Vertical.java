package modelo;

public class Vertical {
	public static char [][] tableroJuego;
	String Palabra;
	private static int tamañoTablero;
	private static char letraRelleno;
	private static boolean palabraColocada;
	private int xInicial;
	private int yInicial;
	private int xFinal;
	private int yFinal;

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
	public Vertical (String palabra, char [][] tblJuego, int tam, char lr){
		Palabra = palabra;
		tableroJuego = tblJuego;
		tamañoTablero = tam;
		letraRelleno = lr;
		/**
		 * El constructor sólo coloca la primera palabra del tablero, del resto se ocuta actualizaDatos
		 * */
		colocaPalabra();
	}
	public char [][] devuelveTableroJuego (){
		return tableroJuego;
	}
	public void actualizaDatos (String palabra, char [][] tblJuego){
		Palabra = palabra;
		tableroJuego = tblJuego;
		colocaPalabra();
	}
	private void colocaPalabra() {
		int ejeYinicial = tamañoTablero - Palabra.length() + 1;
		int ejeX = (int)(Math.random()*tamañoTablero);
		int ejeY = (int)(Math.random()*ejeYinicial);
		
		xInicial = ejeX;
		yInicial = ejeY;
				
		int vectorEjeX [] = new int [tamañoTablero];
		int vectorEjeY [] = new int [tamañoTablero];
		int letrasColocadas = 0;
		boolean correcto = true;
		int k = 0;
		for (; k < Palabra.length(); k++, ejeY++){
			if (tableroJuego [ejeY][ejeX] == letraRelleno){
				vectorEjeX[k] = ejeX;
				vectorEjeY[k] = ejeY;
				tableroJuego[ejeY][ejeX] = Palabra.charAt(k);
				letrasColocadas++;
			}
			else {
				/**
				 * Encontró un caracter distinto de la letra de relleno, por lo que vamos a dejar el tablero de juego tal y como 
				 * estaba
				 * */
				for (int i = 0; i < letrasColocadas; i++){
					tableroJuego [vectorEjeY[i]][vectorEjeX[i]] = letraRelleno;
				}
				correcto = false;
				break;
			}
		}
		if (correcto){
			xFinal = vectorEjeX[k-1];
			yFinal = vectorEjeY[k-1];
			palabraColocada = true;
		}
		else palabraColocada = false;		
	}
	public boolean resultado (){
		return palabraColocada;
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
	public void setyInicial(int yInicial) {
		this.yInicial = yInicial;
	}


}