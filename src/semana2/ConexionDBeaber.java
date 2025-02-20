package semana2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;


/* EL pom.xml SE VERÍA ASi:
 * 
 * <?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.4.2</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.bbdd</groupId>
	<artifactId>BBDDyEclipse</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>BBDDyEclipse</name>
	<description>Demo project for Spring Boot</description>
	<url/>
	<licenses>
		<license/>
	</licenses>
	<developers>
		<developer/>
	</developers>
	<scm>
		<connection/>
		<developerConnection/>
		<tag/>
		<url/>
	</scm>
	<properties>
		<java.version>17</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
    		<groupId>com.oracle.database.jdbc</groupId>
    		<artifactId>ojdbc8</artifactId>
    		<version>19.8.0.0</version>
		</dependency>
		<dependency>
    		<groupId>mysql</groupId>
    		<artifactId>mysql-connector-java</artifactId>
    		<version>8.0.29</version> <!-- Asegúrate de usar la versión más reciente -->
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>
*/

public class ConexionDBeaber {
	public static void main(String[] args) {

         String url = "jdbc:oracle:thin:@frparccsw:1521/FREEPDB1";
        String usuario = "user2";
        String password = "user2";

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nSeleccione una operación:");
            System.out.println("1. Insertar un registro");
            System.out.println("2. Actualizar un registro");
            System.out.println("3. Eliminar un registro");
            System.out.println("4. Consultar registros");
            System.out.println("5. Salir");
            System.out.print("Opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();  

            if (opcion == 5) {
                System.out.println("Saliendo...");
                break;
            }

            System.out.println("Seleccione la tabla:");
            System.out.println("1. clientes");
            System.out.println("2. productos");
            System.out.println("3. ventas");
            System.out.print("Opción: ");
            int tablaOpcion = scanner.nextInt();
            scanner.nextLine();  

            String tabla = "";
            switch (tablaOpcion) {
                case 1:
                    tabla = "clientes";
                    break;
                case 2:
                    tabla = "productos";
                    break;
                case 3:
                    tabla = "ventas";
                    break;
                default:
                    System.out.println("Tabla no válida. Eligiendo 'clientes' por defecto.");
                    tabla = "clientes";
                    break;
            }

            switch (opcion) {
                case 1:
                    insertarRegistro(scanner, url, usuario, password, tabla);
                    break;
                case 2:
                    actualizarRegistro(scanner, url, usuario, password, tabla);
                    break;
                case 3:
                    eliminarRegistro(scanner, url, usuario, password, tabla);
                    break;
                case 4:
                    consultarRegistros(scanner, url, usuario, password, tabla);
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
        scanner.close();
    }

    private static String[] obtenerColumnas(String url, String usuario, String password, String tabla) {
        tabla = tabla.toUpperCase();  
        try (Connection con = DriverManager.getConnection(url, usuario, password);
             
             PreparedStatement pstmt = con.prepareStatement(
                 "SELECT column_name FROM user_tab_columns WHERE table_name = ?",
                 ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            pstmt.setString(1, tabla);
            ResultSet rs = pstmt.executeQuery();

            
            ArrayList<String> columnasList = new ArrayList<>();

            
            while (rs.next()) {
                columnasList.add(rs.getString("column_name"));
            }

            
            if (!columnasList.isEmpty()) {
                return columnasList.toArray(new String[0]);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    private static void insertarRegistro(Scanner scanner, String url, String usuario, String password, String tabla) {
        System.out.println("\nInsertando un registro en la tabla: " + tabla);
        String[] columnas = obtenerColumnas(url, usuario, password, tabla);

        if (columnas != null) {
            System.out.println("Columnas de la tabla " + tabla + ":");
            for (int i = 0; i < columnas.length; i++) {
                System.out.println((i + 1) + ". " + columnas[i]);
            }

            
            String insertSQL = "INSERT INTO " + tabla + " (";
            String valuesSQL = "VALUES (";
            for (int i = 0; i < columnas.length; i++) {
                insertSQL += columnas[i];
                valuesSQL += "?";
                if (i < columnas.length - 1) {
                    insertSQL += ", ";
                    valuesSQL += ", ";
                }
            }
            insertSQL += ") " + valuesSQL + ")";

            try (Connection con = DriverManager.getConnection(url, usuario, password);
                 PreparedStatement pstmt = con.prepareStatement(insertSQL)) {

                
                for (int i = 0; i < columnas.length; i++) {
                    System.out.print("Ingrese el valor para la columna " + columnas[i] + ": ");
                    String valor = scanner.nextLine();
                    pstmt.setString(i + 1, valor);
                }

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("El registro ha sido insertado correctamente .");
                } else {
                    System.out.println("No se pudo insertar el registro.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void actualizarRegistro(Scanner scanner, String url, String usuario, String password, String tabla) {
        System.out.println("\nActualizando un registro en la tabla: " + tabla);
        String[] columnas = obtenerColumnas(url, usuario, password, tabla);

        if (columnas != null) {
            System.out.println("Columnas de la tabla " + tabla + ":");
            for (int i = 0; i < columnas.length; i++) {
                System.out.println((i + 1) + ". " + columnas[i]);
            }

            System.out.print("Ingrese el valor de la columna que usará como condición para actualizar: ");
            String columnaCondicion = scanner.nextLine();

            String updateSQL = "UPDATE " + tabla + " SET ";
            for (int i = 0; i < columnas.length; i++) {
                updateSQL += columnas[i] + " = ?";
                if (i < columnas.length - 1) {
                    updateSQL += ", ";
                }
            }
            updateSQL += " WHERE " + columnaCondicion + " = ?";

            try (Connection con = DriverManager.getConnection(url, usuario, password);
                 PreparedStatement pstmt = con.prepareStatement(updateSQL)) {

                for (int i = 0; i < columnas.length; i++) {
                    System.out.print("Ingrese el nuevo valor para la columna " + columnas[i] + ": ");
                    String valor = scanner.nextLine();
                    pstmt.setString(i + 1, valor);
                }

                System.out.print("Ingrese el valor de la columna para la condición de actualización: ");
                String condicionValor = scanner.nextLine();
                pstmt.setString(columnas.length + 1, condicionValor);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("El registro ha sido actualizado correctamente.");
                } else {
                    System.out.println("No se encontró el registro con ese valor.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void eliminarRegistro(Scanner scanner, String url, String usuario, String password, String tabla) {
        System.out.println("\nEliminando un registro de la tabla: " + tabla);
        String[] columnas = obtenerColumnas(url, usuario, password, tabla);

        if (columnas != null) {
            System.out.print("Ingrese el valor de la columna para la condición de eliminación: (DELETE FROM " +tabla+ " WHERE *columna* = '')");
            String columnaCondicion = scanner.nextLine();

            String deleteSQL = "DELETE FROM " + tabla + " WHERE " + columnaCondicion + " = ?";
            try (Connection con = DriverManager.getConnection(url, usuario, password);
                 PreparedStatement pstmt = con.prepareStatement(deleteSQL)) {

                System.out.print("Ingrese el valor de la condición: (DELETE FROM " +tabla+ " WHERE "+ columnaCondicion +" = '*valor*')");
                String condicionValor = scanner.nextLine();
                pstmt.setString(1, condicionValor);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("El registro ha sido eliminado correctamente.");
                } else {
                    System.out.println("No se encontró el registro con ese valor.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void consultarRegistros(Scanner scanner, String url, String usuario, String password, String tabla) {
        System.out.println("\nConsultando registros de la tabla: " + tabla);
        String[] columnas = obtenerColumnas(url, usuario, password, tabla);

        if (columnas != null) {
            System.out.println("Columnas de la tabla " + tabla + ":");
            for (String columna : columnas) {
                System.out.print(columna + " | ");
            }
            System.out.println();

            String selectSQL = "SELECT * FROM " + tabla;
            try (Connection con = DriverManager.getConnection(url, usuario, password);
                 Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(selectSQL)) {

                while (rs.next()) {
                    for (String columna : columnas) {
                        System.out.print(rs.getString(columna) + " | ");
                    }
                    System.out.println();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}



}
