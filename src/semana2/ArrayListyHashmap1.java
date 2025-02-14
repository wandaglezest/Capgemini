package semana2;

import java.util.ArrayList;

public class ArrayListyHashmap1 {
	public static void main(String[] args) {
		
		ArrayList<String> nombres = new ArrayList<>();
		nombres.add("Luis");
		nombres.add("Marcos");
		nombres.add("Sofía");
		
	
		//nombres.forEach(nombre ->System.out.println(nombre));
		
		for (String nombre:nombres) {
			System.out.println(nombre);
		}
		
	}

}
