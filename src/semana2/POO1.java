package semana2;

public class POO1 {

	public static void main(String[] args) {
		Persona wanda = new Persona();
		
		wanda.setNombre("Wanda González");
		wanda.setEdad(24);
		
		System.out.println("Alumno:" + wanda.getNombre() + " Edad:" + wanda.getEdad());
		
		wanda.saludar();
	}

}
