package modelo;

import java.util.Vector;

public class QuickSort {

	/**
	 * @param args
	 */
	private static Vector <String> Palabras;
	private int [] matrix;
	
	public QuickSort (Vector <String> palabras){
		Palabras = new Vector <String>();
		Palabras = palabras;
		ordenaMayorMenor();
	}
	public Vector <String> devuelveVectorOrdenado (){
		return Palabras;
	}
	
	private void ordenaMayorMenor(){
		int tamano = Palabras.size();
		matrix = new int [tamano];
		try{
			for (int i=0; i< tamano; i++)
				matrix[i] = Palabras.get(i).length();
					
			/**
			 * Se ordena la lista de menor a mayor
			 * */
			_Quicksort(matrix, 0, tamano-1);
//			Como Quicksort devuelve la lista ordenada de menor a mayor la invertimos
//				pues a nosotros nos interesa introducir primero las palabras más largas
			for (int j =0, k = tamano-1 ; j < tamano/2; j++,k--){
				int aux = matrix[k];
				matrix[k] = matrix[j];
				matrix[j] = aux;
			}
			
			Vector <String>auxiliar = new Vector <String>();
			
			for (int i = 0; i < matrix.length; i++){
				for (int j =0; j < Palabras.size(); j++){
					if (matrix[i] == Palabras.get(j).length()){	
						String aux = Palabras.get(j);
						auxiliar.add(aux);
						Palabras.remove(j);
					}
				}
			}
			for (int i = 0; i < auxiliar.size(); i++)
				Palabras.add(auxiliar.get(i));
				
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Error: " + e);
		}
	}
	public void _Quicksort(int matrix[], int a, int b)
	{
		this.matrix = new int[matrix.length];
		int buf;
		int from = a;
		int to = b;
		int pivot = matrix[(from+to)/2];
		do
		{
			while(matrix[from] < pivot)
			{
				from++;
			}
			while(matrix[to] > pivot)
			{
				to--;
			}
			if(from <= to)
			{
				buf = matrix[from];
				matrix[from] = matrix[to];
				matrix[to] = buf;
				from++; to--;
			}
		}while(from <= to);
		if(a < to)
		{
			_Quicksort(matrix, a, to);
		}
		if(from < b)
		{
			_Quicksort(matrix, from, b);
		}
		this.matrix = matrix;		
	}

}