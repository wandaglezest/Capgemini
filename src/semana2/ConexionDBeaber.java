package semana2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class ConexionDBeaber {
	public static void main(String[] args) {

        String url = "jdbc:oracle:thin:@frparccsw:1521/FREEPDB1";
        String usuario = "user2";
        String password = "user2";

        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Menú de operaciones
            System.out.println("\nSeleccione una operación:");
            System.out.println("1. Insertar un registro");
            System.out.println("2. Actualizar un registro");
            System.out.println("3. Eliminar un registro");
            System.out.println("4. Consultar registros");
            System.out.println("5. Salir");
            System.out.print("Opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();  // Consumir el salto de línea

            if (opcion == 5) {
                System.out.println("Saliendo...");
                break;
            }

            // Solicitar la tabla sobre la que se desea trabajar
            System.out.println("Seleccione la tabla:");
            System.out.println("1. clientes");
            System.out.println("2. productos");
            System.out.println("3. ventas");
            System.out.print("Opción: ");
            int tablaOpcion = scanner.nextInt();
            scanner.nextLine();  // Consumir el salto de línea

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

            // Realizar operación según la opción seleccionada
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

 // Método para obtener las columnas de una tabla seleccionada
    private static String[] obtenerColumnas(String url, String usuario, String password, String tabla) {
        tabla = tabla.toUpperCase();  // Asegúrate de que la tabla esté en mayúsculas
        try (Connection con = DriverManager.getConnection(url, usuario, password);
             // Cambiar a un Statement que soporte desplazamiento
             PreparedStatement pstmt = con.prepareStatement(
                 "SELECT column_name FROM user_tab_columns WHERE table_name = ?",
                 ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            pstmt.setString(1, tabla);
            ResultSet rs = pstmt.executeQuery();

            // Usar ArrayList para almacenar las columnas
            ArrayList<String> columnasList = new ArrayList<>();

            // Recorrer el ResultSet y agregar las columnas al ArrayList
            while (rs.next()) {
                columnasList.add(rs.getString("column_name"));
            }

            // Convertir el ArrayList en un array
            if (!columnasList.isEmpty()) {
                return columnasList.toArray(new String[0]);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para insertar un registro en la tabla seleccionada
    private static void insertarRegistro(Scanner scanner, String url, String usuario, String password, String tabla) {
        System.out.println("\nInsertando un registro en la tabla: " + tabla);
        String[] columnas = obtenerColumnas(url, usuario, password, tabla);

        if (columnas != null) {
            System.out.println("Columnas de la tabla " + tabla + ":");
            for (int i = 0; i < columnas.length; i++) {
                System.out.println((i + 1) + ". " + columnas[i]);
            }

            // Crear la consulta de inserción
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

                // Solicitar valores para las columnas
                for (int i = 0; i < columnas.length; i++) {
                    System.out.print("Ingrese el valor para la columna " + columnas[i] + ": ");
                    String valor = scanner.nextLine();
                    pstmt.setString(i + 1, valor);
                }

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("El registro ha sido insertado correctamente.");
                } else {
                    System.out.println("No se pudo insertar el registro.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para actualizar un registro en la tabla seleccionada
    private static void actualizarRegistro(Scanner scanner, String url, String usuario, String password, String tabla) {
        System.out.println("\nActualizando un registro en la tabla: " + tabla);
        String[] columnas = obtenerColumnas(url, usuario, password, tabla);

        if (columnas != null) {
            System.out.println("Columnas de la tabla " + tabla + ":");
            for (int i = 0; i < columnas.length; i++) {
                System.out.println((i + 1) + ". " + columnas[i]);
            }

            // Pedir el ID o columna para actualizar
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

                // Solicitar valores para actualizar
                for (int i = 0; i < columnas.length; i++) {
                    System.out.print("Ingrese el nuevo valor para la columna " + columnas[i] + ": ");
                    String valor = scanner.nextLine();
                    pstmt.setString(i + 1, valor);
                }

                // Pedir el valor de la condición
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

    // Método para eliminar un registro de la tabla seleccionada
    private static void eliminarRegistro(Scanner scanner, String url, String usuario, String password, String tabla) {
        System.out.println("\nEliminando un registro de la tabla: " + tabla);
        String[] columnas = obtenerColumnas(url, usuario, password, tabla);

        if (columnas != null) {
            System.out.print("Ingrese el valor de la columna para la condición de eliminación: (DELETE FROM " +tabla+ " WHERE *columna* = '')");
            String columnaCondicion = scanner.nextLine();

            String deleteSQL = "DELETE FROM " + tabla + " WHERE " + columnaCondicion + " = ?";
            try (Connection con = DriverManager.getConnection(url, usuario, password);
                 PreparedStatement pstmt = con.prepareStatement(deleteSQL)) {

                // Pedir el valor de la condición
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

    // Método para consultar registros de la tabla seleccionada
    private static void consultarRegistros(Scanner scanner, String url, String usuario, String password, String tabla) {
        System.out.println("\nConsultando registros de la tabla: " + tabla);
        String[] columnas = obtenerColumnas(url, usuario, password, tabla);

        if (columnas != null) {
            // Mostrar las columnas y ejecutar la consulta
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
