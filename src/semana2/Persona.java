package semana2;

public class Persona {
	private String nombre;
	private int edad;
	
	public void saludar(){
		
		System.out.println("Holaa!");
	}	
	
	public String getNombre() {
		return nombre;
		
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public int getEdad() {
		return edad;
		
	}
	
	public void setEdad(int edad) {
		this.edad = edad;
	}
}


