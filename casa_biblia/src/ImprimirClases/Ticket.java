/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImprimirClases;

import Clases.Cl_Conectar;
import Clases.Cl_Varios;
import Clases.leer_numeros;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.swing.JOptionPane;

/**
 *
 * @author Dereck
 */
public class Ticket {

    public static leer_numeros leer = new leer_numeros();
    public static Cl_Varios ven = new Cl_Varios();
    public static Cl_Conectar con = new Cl_Conectar();
    public static ArrayList cabezera = new ArrayList();
    public static ArrayList datos_tienda = new ArrayList();
    public static ArrayList datos_vendedor = new ArrayList();
    public static ArrayList cantidad = new ArrayList();
    public static ArrayList producto = new ArrayList();
    public static ArrayList parcial = new ArrayList();
    public static double subtotal;
    public static double igv;
    public static double total;

    public static String centrar_texto(String titulo) {
        String texto;
        int largo = 40;
        int largo_actual = titulo.length();
        int faltantes = (largo - largo_actual) / 2;
        String espacio = "";
        if (faltantes >= 0) {
            for (int i = 0; i < faltantes; i++) {
                espacio = espacio + " ";
            }
            texto = espacio + titulo + espacio;
        } else {
            texto = titulo.substring(0, largo);
        }

        return texto;
    }

    public static String texto_izquierda(String titulo) {
        String texto;
        int largo = 40;
        int largo_actual = titulo.length();
        int faltantes = (largo - largo_actual);
        String espacio = "";
        if (faltantes >= 0) {
            for (int i = 0; i < faltantes; i++) {
                espacio = espacio + " ";
            }
            texto = titulo + espacio;
        } else {
            texto = titulo.substring(0, largo);
        }
        return texto;
    }

    public static String texto_derecha(String titulo) {
        String texto;
        int largo = 40;
        int largo_actual = titulo.length();
        int faltantes = (largo - largo_actual);
        String espacio = "";
        for (int i = 0; i < faltantes; i++) {
            espacio = espacio + " ";
        }
        texto = espacio + titulo;
        return texto;
    }

    public static String columna_10(String titulo) {
        String texto;
        int largo = 10;
        int largo_actual = titulo.length();
        int faltantes = (largo - largo_actual);
        String espacio = "";
        for (int i = 0; i < faltantes; i++) {
            espacio = espacio + " ";
        }
        texto = titulo + espacio;
        return texto;
    }

    public static String columna_30(String titulo) {
        String texto;
        int largo = 30;
        int largo_actual = titulo.length();
        int faltantes = (largo - largo_actual);
        String espacio = "";
        for (int i = 0; i < faltantes; i++) {
            espacio = espacio + " ";
        }
        texto = titulo + espacio;
        return texto;
    }

    public static String columna_20_izquierda(String titulo) {
        String texto;
        int largo = 20;
        int largo_actual = titulo.length();
        int faltantes = (largo - largo_actual);
        String espacio = "";
        for (int i = 0; i < faltantes; i++) {
            espacio = espacio + " ";
        }
        texto = espacio + titulo;
        return texto;
    }

    public static String columna_20_derecha(String titulo) {
        String texto;
        int largo = 20;
        int largo_actual = titulo.length();
        int faltantes = (largo - largo_actual);
        String espacio = "";
        for (int i = 0; i < faltantes; i++) {
            espacio = espacio + " ";
        }
        texto = espacio + titulo;
        return texto;
    }

    public static String columna_2(String titulo) {
        String texto;
        int largo = 2;
        int largo_actual = titulo.length();
        int faltantes = (largo - largo_actual);
        String espacio = "";
        for (int i = 0; i < faltantes; i++) {
            espacio = espacio + " ";
        }
        texto = titulo + espacio;
        return texto;
    }

    public static String columna_5(String titulo) {
        String texto;
        int largo = 5;
        int largo_actual = titulo.length();
        int faltantes = (largo - largo_actual);
        String espacio = "";
        for (int i = 0; i < faltantes; i++) {
            espacio = espacio + " ";
        }
        texto = titulo + espacio;
        return texto;
    }

    public static String columna_33(String titulo) {
        String texto;
        int largo = 33;
        int largo_actual = titulo.length();
        int faltantes = (largo - largo_actual);
        String espacio = "";
        if (faltantes > 0) {
            for (int i = 0; i < faltantes; i++) {
                espacio = espacio + " ";
            }
            texto = titulo + espacio;
        } else {
            texto = titulo.substring(0, largo);
        }
        return texto;
    }

    public static String columna_33_faltante(String titulo) {
        String texto;
        int largo = 38;
        int largo_texto = titulo.length();
        texto = titulo.substring(33, largo_texto);
        int largo_actual = texto.length();
        int faltantes = (largo - largo_actual);
        String espacio = "";
        for (int i = 0; i < faltantes; i++) {
            espacio = espacio + " ";
        }
        texto = "   " + texto + espacio;
        return texto;
    }

    public static String enter() {
        String texto = "\n";
//        String espacio = "";
//        for (int i = 0; i < 40; i++) {
//            espacio = espacio + " ";
//        }
//        texto = espacio;
        return texto;
    }

    public static String linea() {
        String texto;
        String espacio = "";
        for (int i = 0; i < 40; i++) {
            espacio = espacio + "-";
        }
        texto = espacio;
        return texto;
    }

    public static void llenar(String id_venta, String periodo, String id_almacen) {
        cabezera.add(centrar_texto("CORSO CAPRISTANO FAUSTA PAULINA"));
        cabezera.add(centrar_texto("*** BOTICAS INTIFARMA ***"));
        cabezera.add(centrar_texto("RUC: 10328441964"));
        cabezera.add(centrar_texto("AV. E. MEIGGS N. 2112 P.J. FLORIDA ALTA"));
        cabezera.add(centrar_texto("99012154 - 65878451"));
        try {
            Statement st = con.conexion();
            String ver_datos = "select a.nombre as almacen, a.direccion, e.nombres, e.ape_pat, e.ape_mat, td.nombre as tido, v.serie, v.numero, v.fecha_venta, v.tc_venta, v.total, v.fecha_registro from ventas as v inner join tipo_documento as td on "
                    + "v.id_tido = td.id_tido inner join almacenes as a on v.id_almacen = a.id_almacen inner join empleados as e on v.vendedor = e.dni where v.id_venta = '" + id_venta + "' and v.periodo = '" + periodo + "' and v.id_almacen = '" + id_almacen + "'";
            System.out.println(ver_datos);
            ResultSet rs = con.consulta(st, ver_datos);
            if (rs.next()) {
                cabezera.add(centrar_texto("TICKET " + rs.getString("tido")));
                cabezera.add(centrar_texto("B" + ven.ceros_izquierda(3, rs.getString("serie")) + "-" + ven.ceros_izquierda(7, rs.getString("numero"))));
                datos_tienda.add("Tienda: " + rs.getString("almacen"));
                datos_tienda.add(rs.getString("direccion"));
                datos_vendedor.add(columna_10("FECHA") + " " + columna_30(": " + rs.getTimestamp("fecha_registro")));
                datos_vendedor.add(columna_10("SERIE MAQ.") + " " + columna_30(": 125478748A4S4C8"));
                datos_vendedor.add(columna_10("CAJA/TURNO") + " " + columna_30(": 1/MAÑANA"));
                datos_vendedor.add(columna_10("MONEDA") + " " + columna_30(": S/ - SOLES"));
                datos_vendedor.add(columna_10("VENDEDOR") + " " + columna_30(": " + rs.getString("nombres") + " " + rs.getString("ape_pat") + " " + rs.getString("ape_mat")));
                total = rs.getDouble("total");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }

        //cargar productos
        try {
            Statement st = con.conexion();
            String ver_productos = "select p.descripcion, p.presentacion, p.laboratorio, dv.precio_venta, dv.cantidad_venta from detalle_venta as dv inner join productos as p on "
                    + "dv.id_producto = p.id_producto where dv.id_venta = '" + id_venta + "' and dv.periodo = '" + periodo + "' and dv.id_almacen = '" + id_almacen + "'";
            System.out.println(ver_productos);
            ResultSet rs = con.consulta(st, ver_productos);
            while (rs.next()) {
                cantidad.add(rs.getInt("cantidad_venta"));
                producto.add(rs.getString("descripcion") + " - " + rs.getString("presentacion") + " / " + rs.getString("laboratorio"));
                double sub_parcial = rs.getDouble("cantidad_venta") * rs.getDouble("precio_venta");
                parcial.add(ven.formato_numero(sub_parcial));
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }

        subtotal = total / 1.18;
        igv = subtotal * 0.18;

    }

    public static void generar() {
        //llenar("12", "07-2016", "2");
        FileWriter pw = null;
        // PrintWriter pw = null;
        try {
            pw = new FileWriter("USB003:");
            //pw = new PrintWriter(fichero);

            for (Object o_cabezera : cabezera) {
                pw.write(o_cabezera.toString());
            }
            pw.write(enter());
            for (Object o_tienda : datos_tienda) {
                pw.write(texto_izquierda(o_tienda.toString()));
            }
            pw.write(enter());
            for (Object o_vendedor : datos_vendedor) {
                pw.write(o_vendedor.toString());
            }
            pw.write(enter());
            pw.write("CANT.         DESCRIPCION          TOTAL");
            pw.write(linea());
            for (int i = 0; i < producto.size(); i++) {
                pw.write(columna_2(cantidad.get(i).toString()) + " " + columna_33(producto.get(i).toString()) + " " + columna_5(parcial.get(i).toString()));
                System.out.println(producto.get(i).toString().length());
                if (producto.get(i).toString().length() > 33) {
                    pw.write(columna_33_faltante(producto.get(i).toString()));
                }
            }
            pw.write(linea());
            pw.write(enter());
            pw.write(columna_20_izquierda("SUB TOTAL") + columna_20_derecha(ven.formato_numero(subtotal)));
            pw.write(columna_20_izquierda("IGV") + columna_20_derecha(ven.formato_numero(igv)));
            pw.write(columna_20_izquierda("TOTAL") + columna_20_derecha(ven.formato_numero(total)));
            pw.write(enter());
            pw.write(texto_izquierda("SON: " + leer.Convertir(ven.formato_numero(total), true) + " SOLES"));
            pw.write(enter());
            pw.write(centrar_texto("NO HAY DEVOLUCION DE DINERO"));
            pw.write(centrar_texto("TODO CAMBIO DE MERCADERIA SE HARA"));
            pw.write(centrar_texto("DENTRO DE LAS 48 HORAS PREVIA"));
            pw.write(centrar_texto("PRESENTACION DEL COMPROBANTE Y"));
            pw.write(centrar_texto("VERIFICACION POR PARTE DEL QUIMICO."));
            pw.write(enter());
            pw.write(centrar_texto("GRACIAS POR SU COMPRA"));
            //char[] CORTAR_PAPEL = new char[]{0x1B, 'm'};
            //pw.write(CORTAR_PAPEL);
            //char ABRIR_GAVETA[] = {(char) 27, (char) 112, (char) 0, (char) 10, (char) 100};
            //pw.write(ABRIR_GAVETA);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != pw) {
                    pw.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    void generar_2() {
        //llenar("12", "07-2016", "2");
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("impresion.txt");
            pw = new PrintWriter(fichero);

            for (Object o_cabezera : cabezera) {
                pw.println(o_cabezera.toString());
            }
            pw.println(enter());
            for (Object o_tienda : datos_tienda) {
                pw.println(texto_izquierda(o_tienda.toString()));
            }
            pw.println(enter());
            for (Object o_vendedor : datos_vendedor) {
                pw.println(o_vendedor.toString());
            }
            pw.println(enter());
            pw.println("CANT.         DESCRIPCION          TOTAL");
            pw.println(linea());
            for (int i = 0; i < producto.size(); i++) {
                pw.println(columna_2(cantidad.get(i).toString()) + " " + columna_33(producto.get(i).toString()) + " " + columna_5(parcial.get(i).toString()));
                System.out.println(producto.get(i).toString().length());
                if (producto.get(i).toString().length() > 33) {
                    pw.println(columna_33_faltante(producto.get(i).toString()));
                }
            }
            pw.println(linea());
            pw.println(enter());
            pw.println(columna_20_izquierda("SUB TOTAL") + columna_20_derecha(ven.formato_numero(subtotal)));
            pw.println(columna_20_izquierda("IGV") + columna_20_derecha(ven.formato_numero(igv)));
            pw.println(columna_20_izquierda("TOTAL") + columna_20_derecha(ven.formato_numero(total)));
            pw.println(enter());
            pw.println(texto_izquierda("SON: " + leer.Convertir(ven.formato_numero(total), true) + " SOLES"));
            pw.println(enter());
            pw.println(centrar_texto("NO HAY DEVOLUCION DE DINERO"));
            pw.println(centrar_texto("TODO CAMBIO DE MERCADERIA SE HARA"));
            pw.println(centrar_texto("DENTRO DE LAS 48 HORAS PREVIA"));
            pw.println(centrar_texto("PRESENTACION DEL COMPROBANTE Y"));
            pw.println(centrar_texto("VERIFICACION POR PARTE DEL QUIMICO."));
            pw.println(enter());
            pw.println(centrar_texto("GRACIAS POR SU COMPRA"));
            char[] CORTAR_PAPEL = new char[]{0x1B, 'm'};
            pw.write(CORTAR_PAPEL);
            char ABRIR_GAVETA[] = {(char) 27, (char) 112, (char) 0, (char) 10, (char) 100};
            pw.write(ABRIR_GAVETA);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    void imprimir() {

        try {
            //FileWriter imp = new FileWriter("LPT1");
            FileWriter imp = new FileWriter("USB");
            char[] Caracter = new char[]{0x1B, 'R', 18};
            imp.write(Caracter);
            //corta el papel
            char[] CORTAR_PAPEL = new char[]{0x1B, 'm'};
            imp.write(CORTAR_PAPEL);
           // char ABRIR_GAVETA[] = {(char) 27, (char) 112, (char) 0, (char) 10, (char) 100};
            //  imp.write(ABRIR_GAVETA);
            imp.close();
            //limpio las listas que contiene los datos
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al Imprimir:\n" + e.getMessage());
        }
        //Cogemos el servicio de impresión por defecto (impresora por defecto)
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        //Le decimos el tipo de datos que vamos a enviar a la impresora
        //Tipo: bytes Subtipo: autodetectado
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        //Creamos un trabajo de impresión
        DocPrintJob pj = service.createPrintJob();
        //Nuestro trabajo de impresión envía una cadena de texto
        String ss = new String("Aquí lo que vamos a imprimir.");
        byte[] bytes;
        //Transformamos el texto a bytes que es lo que soporta la impresora
        bytes = ss.getBytes();
        //Creamos un documento (Como si fuese una hoja de Word para imprimir)
        Doc doc = new SimpleDoc(bytes, flavor, null);
        //Obligado coger la excepción PrintException
        try {
            //Mandamos a impremir el documento
            pj.print(doc, null);
        } catch (PrintException e) {
            System.out.println("Error al imprimir: " + e.getMessage());
        }
    }
}
