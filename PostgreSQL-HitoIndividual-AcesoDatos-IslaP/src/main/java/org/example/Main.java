package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Conexion con la base de datos PostgreSQL

        Scanner sc = new Scanner(System.in);
        String opcion = "";

        //Utilizo el JDBC para conectarme a la base de datos
        String conexion = "jdbc:postgresql://localhost:5432/beautyShop";
        String usuario = "postgres";
        String password = "curso";
        Connection con;
        Statement stmt;
        ResultSet rs = null;

        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(conexion, usuario, password);
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("No se ha podido abrir la BD");
            return;
        }

        //Creo un menu por consola para que el usuario pueda elegir que quiere hacer
        //Creo un menu por consola para que el usuario pueda elegir que quiere hacer
        do {
            //Le muestro al usuario las opciones que tiene
            System.out.println("1. Listar productos");
            System.out.println("2. Añadir producto");
            System.out.println("3. Añadir marca");
            System.out.println("4. Modificar producto");
            System.out.println("5. Borrar producto");
            System.out.println("6. Borrar marca");
            System.out.println("7. Exportar todos los datos a un CSV");
            System.out.println("8. Exportar datos de los productos a CSV");
            System.out.println("9. Exportar datos de las marcas a CSV");
            System.out.println("10. Salir");
            System.out.println("Elige una opción: ");

            //Aqui almacenamos la opcion que el usuario elija
            opcion = sc.nextLine();

            //Depandiendo de la opcion que elija el usuario, se ejecutara una accion
            switch (opcion) {
                case "1":
                    //Listar productos
                    try {
                        rs = stmt.executeQuery("SELECT * FROM productos;");
                        rs.beforeFirst();
                        //Recorremos el ResultSet
                        while (rs.next()) {
                            //Mostramos los datos de la consulta
                            System.out.println(rs.getString("nombreProducto") + " "
                                    + rs.getString("categoria") + " " + rs.getFloat("precio"));
                        }

                    } catch (SQLException e) {
                        //Si hay algun error, mostramos un mensaje por consola
                        System.out.println("No se ha podido obtener la tabla productos");
                    }
                    break;
                case "2":
                    //Añadir producto
                    try {
                        //Obtenemos los datos de la tabla producto
                        rs = stmt.executeQuery("SELECT * FROM productos");
                        rs.beforeFirst();
                        rs.moveToInsertRow();
                        //Aqui le pido al usuario que introduzca los datos del producto
                        System.out.println("Nombre: ");
                        rs.updateString("nombreProducto", sc.nextLine());
                        System.out.println("Categoria: ");
                        rs.updateString("categoria", sc.nextLine());
                        System.out.println("Precio: ");
                        rs.updateFloat("precio", Float.parseFloat(sc.nextLine()));
                        System.out.println("ID de la marca: ");
                        rs.updateInt("idMarca_productos", Integer.parseInt(sc.nextLine()));
                        rs.insertRow();
                        System.out.println("Producto añadido");
                    } catch (SQLException e) {
                        //Si hay algun error, mostramos un mensaje por consola
                        System.out.println("No se ha podido obtener la tabla productos");
                    }
                    break;

                case "3":
                    //Añadir marca
                    try {
                        //Obtenemos la informacion de tabla  que almacena las marcas
                        rs = stmt.executeQuery("SELECT * FROM marca");
                        rs.moveToInsertRow();
                        //Aqui le pido al usuario que introduzca los datos de la marca
                        System.out.println("Nombre: ");
                        rs.updateString("nombreMarca", sc.nextLine());
                        rs.insertRow();
                        System.out.println("Marca añadida");
                    } catch (SQLException e) {
                        //Si hay algun error, mostramos un mensaje por consola
                        System.out.println("No se ha podido obtener la tabla marca");
                    }
                    break;

                case "4":
                    //Modificar producto a partir de su ID
                    try {
                        //Obtengo la informacion de la tabla productos
                        rs = stmt.executeQuery("SELECT * FROM productos");
                        System.out.println("Dime el ID producto que deseas modificar: ");
                        //Recojo el ID del producto que el usuario quiere modificar
                        int idProducto = Integer.parseInt(sc.nextLine());
                        //Si el ID existe en la base de datos, se mueve el cursor a la fila que contiene ese ID
                        if (rs.absolute(idProducto)) {
                            //Aqui le indico al usuario que datos tiene que introducir para modificar el producto
                            System.out.println("Nombre: ");
                            //Almaceno el nuevo dato del producto modificado
                            rs.updateString("nombreProducto", sc.nextLine());
                            System.out.println("Categoria: ");
                            rs.updateString("categoria", sc.nextLine());
                            System.out.println("Precio: ");
                            rs.updateFloat("precio", Float.parseFloat(sc.nextLine()));
                            System.out.println("ID de la marca: ");
                            rs.updateInt("idMarca_productos", Integer.parseInt(sc.nextLine()));
                            //Guardo los cambios en la base de datos
                            rs.updateRow();
                            //Muestro un mensaje por consola para indicar al usuario que el producto se ha modificado correctamente
                            System.out.println("Producto modificado");
                        } else {
                            //Si el ID no existe en la base de datos, muestro un mensaje por consola
                            System.out.println("No existe el producto");
                        }
                    } catch (SQLException e) {
                        //Si no se ha podido acceder a la tabla productos, muestro un mensaje por consola
                        System.out.println("No se ha podido obtener la tabla productos");
                    }
                    break;

                case "5":
                    //Borrar producto a partir de su ID
                    try {
                        //Obtengo la informacion de la tabla productos
                        rs = stmt.executeQuery("SELECT * FROM productos");
                        System.out.println("Dime el ID producto que deseas borrar: ");
                        //Recojo el ID del producto que el usuario desea borrar
                        int idProducto = Integer.parseInt(sc.nextLine());
                        //Si el ID existe en la base de datos entoces
                        if (rs.absolute(idProducto)) {
                            //Borro el producto
                            rs.deleteRow();
                            //Muestro un mensaje por consola para indicar al usuario que el producto se ha borrado correctamente
                            System.out.println("Producto borrado");
                            //Si el ID no existe en la base de datos, muestro un mensaje por consola
                        } else {
                            System.out.println("No existe el producto");
                        }
                    } catch (SQLException e) {
                        //Si no se ha podido acceder a la tabla productos, muestro un mensaje por consola
                        System.out.println("No se ha podido obtener la tabla productos");
                    }
                    break;

                case "6":
                    //Borrar marca a partir de su ID
                    try {
                        //Obtengo la informacion de la tabla marca
                        rs = stmt.executeQuery("SELECT * FROM marca");
                        System.out.println("Dime el ID marca que deseas borrar: ");
                        //Recojo el ID de la marca que el usuario desea borrar
                        int idMarca = Integer.parseInt(sc.nextLine());
                        //Si el ID existe en la base de datos entoces
                        if (rs.absolute(idMarca)) {
                            //Borro la marca
                            rs.deleteRow();
                            //Muestro un mensaje por consola para indicar al usuario que la marca se ha
                            // borrado correctamente
                            System.out.println("Marca borrada");
                            //Si el ID no existe en la base de datos
                        } else {
                            //Muestro un mensaje por consola
                            System.out.println("No existe la marca");
                        }
                        //Si no se ha podido acceder a la tabla marca, muestro un mensaje por consola para
                        // indicar al usuario que no se ha podido obtener la tabla
                    } catch (SQLException e) {
                        System.out.println("No se ha podido obtener la tabla marca");
                    }
                    break;

                case "7":
                    //Exportar todos los datos a un CSV
                    try {
                        //Obtengo la informacion de las tablas productos y marca
                        rs = stmt.executeQuery("SELECT * FROM productos, marca WHERE productos.idMarca_productos = marca.idMarca");
                        rs.beforeFirst();
                        //Creo un fichero CSV indicandole la ruta donde se va a guardar
                        String csvFile = "C:\\Isla\\2DAM\\1ºT\\Acceso a datos\\Hito\\PostgreSQL-HitoIndividual-AcesoDatos-IslaP\\CSV\\datos.csv";
                        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
                            //Mientras haya datos en la tabla productos
                            while (rs.next()) {
                                //Creo un String con los datos de la tabla productos y la tabla marca
                                bw.write(rs.getInt("idProducto") + ";" +rs.getString("nombreProducto") + ";" + rs.getString("categoria") + ";" +
                                        rs.getFloat("precio") + ";" + rs.getInt("idMarca_productos") + ";" + rs.getInt("idMarca")+ ";" +rs.getString("nombreMarca"));
                                //Añado un salto de linea
                                bw.newLine();
                            }
                            //Muestro un mensaje por consola para indicar al usuario que se ha exportado correctamente
                            System.out.println("Datos exportados");
                            //Si no se ha podido acceder a la tabla productos
                        } catch (IOException e) {
                            //Muestro un mensaje por consola para indicar al usuario cual es el error
                            e.printStackTrace();
                        }
                        //Si no se ha podido acceder a la tabla productos, muestro un mensaje por consola para indicar al usuario que no se ha podido obtener las tablas
                    } catch (SQLException e) {
                        System.out.println("No se ha podido obtener la tabla productos y o la tabla marca");
                    }
                    break;

                case "8":
                    //Exportar datos de los productos a CSV
                    try {
                        //Obtengo la informacion de la tabla productos
                        rs = stmt.executeQuery("SELECT * FROM productos");
                        rs.beforeFirst();
                        //Creo un fichero CSV indicandole la ruta donde se va a guardar
                        String csvFile = "C:\\Isla\\2DAM\\1ºT\\Acceso a datos\\Hito\\PostgreSQL-HitoIndividual-AcesoDatos-IslaP\\CSV\\productos.csv";
                        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
                            //Mientras haya datos en la tabla productos
                            while (rs.next()) {
                                //Creo un String con los datos de la tabla productos
                                bw.write(rs.getInt("idProducto") + ";" +rs.getString("nombreProducto") + ";" + rs.getString("categoria") + ";" +
                                        rs.getFloat("precio") + ";" + rs.getInt("idMarca_productos"));
                                //Añado un salto de linea
                                bw.newLine();
                            }
                            //Muestro un mensaje por consola para indicar al usuario que se ha exportado correctamente
                            System.out.println("Datos exportados");
                            //Si no se ha podido acceder a la tabla productos
                        } catch (IOException e) {
                            //Muestro un mensaje por consola para indicar al usuario cual es el error
                            e.printStackTrace();
                        }
                        //Si no se ha podido acceder a la tabla productos, muestro un mensaje por consola para indicar al usuario que no se ha podido obtener la tabla
                    } catch (SQLException e) {
                        System.out.println("No se ha podido obtener la tabla productos");
                    }
                    break;

                case "9":
                    //Exportar datos de las marcas a CSV
                    try {
                        //Obtengo la informacion de la tabla marca
                        rs = stmt.executeQuery("SELECT * FROM marca");
                        rs.beforeFirst();
                        //Creo un fichero CSV indicandole la ruta donde se va a guardar
                        String csvFile = "C:\\Isla\\2DAM\\1ºT\\Acceso a datos\\Hito\\PostgreSQL-HitoIndividual-AcesoDatos-IslaP\\CSV\\marcas.csv";
                        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
                            //Mientras haya datos en la tabla marca
                            while (rs.next()) {
                                //Creo un String con los datos de la tabla marca
                                bw.write(rs.getInt("idMarca")+ ";" +rs.getString("nombreMarca"));
                                //Añado un salto de linea
                                bw.newLine();
                            }
                            //Muestro un mensaje por consola para indicar al usuario que se ha exportado correctamente
                            System.out.println("Datos exportados");
                            //Si no se ha podido exportar los datos
                        } catch (IOException e) {
                            //Muestro un mensaje por consola para indicar al usuario cual es el error
                            e.printStackTrace();
                        }
                        //Si no se ha podido acceder a la tabla marca, muestro un mensaje por consola para indicar al usuario que no se ha podido obtener la tabla
                    } catch (SQLException e) {
                        System.out.println("No se ha podido obtener la tabla marca");
                    }
                    break;

                case "10":
                    //Salir
                    System.out.println("Hasta pronto");
                    break;
                default:
                    //Si el usuario no elige una opcion valida
                    System.out.println("Opción incorrecta");
            }//Cierra switch
            //Si el usuario no elige la opcion 10, se repetira el menu
        } while (!opcion.equals("10"));//Cierra do while

        //Cerramos la conexion
        try {
            rs.close();     //Cerramos el ResultSet
            stmt.close();   //Cerramos el Statement
            con.close();    //Cerramos la conexion
        } catch (SQLException e) {
            //Muestro un mensaje por consola para indicar al usuario que no se ha podido cerrar la conexion con la base de datos
            System.out.println("No se ha podido cerrar la BD");
        }

    }//Cierra psvm
}//Cierra main

