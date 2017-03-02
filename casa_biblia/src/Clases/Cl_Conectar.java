/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author LUIS OYANGUREN
 */
public class Cl_Conectar {

    private static Connection conexion = null;
    private static final String bd = "casa_biblia"; // Nombre de BD.
     private static final String user = "biblia_lsp"; // Usuario de BD.*/
    /*private static final String bd = "casa_biblia"; // Nombre de BD.
    private static final String user = "root_lsp"; // Usuario de BD.*/
    private static final String password = "root/*123"; // Password de BD.
    //Driver para MySQL en este caso.
    private static final String driver = "com.mysql.jdbc.Driver";
    // Ruta del servidor.
    String url = leer_archivo("server.txt");
    //String server = "jdbc:mysql://" + url + "/" + bd;
    String server = "jdbc:mysql://lunasystemsperu.com:3306/" + bd;

    public void conectar() {
        // if (comprueba_internet() == true) {
        try {

            Class.forName(driver);
            conexion = DriverManager.getConnection(server, user, password);

        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error! - Imposible realizar la conexion a BD. " + bd + " \nRevise su Conexion a Internet\nPongase en contacto con su administrador de Sistema");
            System.out.print(e);
            e.printStackTrace();
            System.exit(0);
        }
        //} else {
        //    JOptionPane.showMessageDialog(null, "No se ha establecido conexion a internet verifica su red");
        //    System.exit(0);
        // }

    }

    /**
     * Método para establecer la conexión con la base de datos.
     *
     * @return
     */
    public Connection conx() {
        return conexion;
    }

    public Statement conexion() {
        Statement st = null;
        try {
            st = conexion.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println("Error: Conexión incorrecta.");
            e.printStackTrace();
        }
        return st;
    }

    /**
     * Método para realizar consultas del tipo: SELECT * FROM tabla WHERE..."
     *
     * @param st
     * @param cadena La consulta en concreto
     * @return
     */
    public ResultSet consulta(Statement st, String cadena) {
        ResultSet rs = null;
        try {
            rs = st.executeQuery(cadena);
        } catch (SQLException e) {
            System.out.println("Error con: " + cadena);
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * Método para realizar consultas de actualización, creación o eliminación.
     *
     * @param st
     * @param cadena La consulta en concreto
     * @return
     */
    public int actualiza(Statement st, String cadena) {
        int rs = -1;
        try {
            rs = st.executeUpdate(cadena);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error con: " + cadena + "\nSQLException: " + e.getLocalizedMessage());
            System.out.println("Error con: " + cadena);
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("valor de rs: " + rs);
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * Método para cerrar la consula
     *
     * @param rs
     */
    public void cerrar(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.out.print("Error: No es posible cerrar la consulta.");
            }
        }
    }

    /**
     * Método para cerrar la conexión.
     *
     * @param st
     */
    public void cerrar(java.sql.Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                System.out.print("Error: No es posible cerrar la conexión.");
            }
        }
    }

    public void conectar(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean comprueba_internet() {
        boolean conectado = false;
        String dirWeb = "www.lunasystemsperu.com";
        int puerto = 80;
        try {
            Socket s = new Socket(dirWeb, puerto);
            if (s.isConnected()) {
                conectado = true;
                System.out.println("Conexión establecida con la dirección: " + dirWeb + " a travéz del puerto: " + puerto);
            }
        } catch (Exception e) {
            System.err.println("No se pudo establecer conexión con: " + dirWeb + " a travez del puerto: " + puerto);
            JOptionPane.showMessageDialog(null, "No se pudo establecer conexión con: " + dirWeb + " a travez del puerto: " + puerto);
        }
        return conectado;
    }

    public String leer_archivo(String nom_arc) {
        String linea = null;
        try {
            File Ffichero = new File(nom_arc);
            /*Si existe el fichero*/
            if (Ffichero.exists()) {
                /*Abre un flujo de lectura a el fichero*/
                BufferedReader Flee = new BufferedReader(new FileReader(Ffichero));
                String Slinea;
                /*Lee el fichero linea a linea hasta llegar a la ultima*/
                while ((Slinea = Flee.readLine()) != null) {
                    /*Imprime la linea leida*/
                    linea = Slinea;
                }
                /*Cierra el flujo*/
                Flee.close();
            } else {
                System.out.println("Fichero No Existe");
                linea = "NO ALMACEN";
            }
        } catch (IOException ex) {
            /*Captura un posible error y le imprime en pantalla*/
            System.out.println(ex.getMessage());
        }
        return linea;
    }

}
