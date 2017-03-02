/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImprimirClases;

import Clases.Cl_Conectar;
import Clases.Cl_Varios;
import br.com.adilson.util.Extenso;
import br.com.adilson.util.PrinterMatrix;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

/**
 *
 * @author gerenciatecnica
 */
public class Print_Envio {

    Cl_Conectar con = new Cl_Conectar();
    Cl_Varios ven = new Cl_Varios();
    String fecha_envio, documento, serie, numero, ruc_empresa, razon_empresa, direcion_empresa, partida, llegada, tipo_movimiento, tipo_envio;

    public void generar(int envio, int anio, String empresa, int tienda) {
        PrinterMatrix printer = new PrinterMatrix();

        Extenso e = new Extenso();

        e.setNumber(101.85);

        try {
            Statement st = con.conexion();
            String c_guia = "select e.fecha, td.nombre as nombre_doc, e.empresa, em.razon_social, em.direccion, e.serie, e.numero, e.direccion as llegada, "
                    + "al.direccion as partida, motivo from envios as e inner join tipo_documento as td on td.id_tido = e.id_tido inner join empresas as "
                    + "em on em.ruc_empresa =  e.empresa inner join almacenes as al on al.id_almacen = e.id_almacen and al.empresa = e.empresa where "
                    + "e.id_envio = '" + envio + "' and e.anio = '" + anio + "' and e.empresa = '" + empresa + "' and e.id_almacen = '" + tienda + "'";
            ResultSet rs = con.consulta(st, c_guia);
            if (rs.next()) {
                fecha_envio = ven.fechaformateada(rs.getString("fecha"));
                documento = rs.getString("nombre_doc").toUpperCase();
                razon_empresa = rs.getString("razon_social").toUpperCase();
                ruc_empresa = rs.getString("empresa").toUpperCase();
                direcion_empresa = rs.getString("direccion").toUpperCase();
                serie = ven.ceros_izquierda(4, rs.getString("serie"));
                numero = ven.ceros_izquierda(7, rs.getString("numero"));
                llegada = rs.getString("llegada").toUpperCase();
                partida = rs.getString("partida").toUpperCase();
                tipo_envio = rs.getString("motivo").toUpperCase();
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }

        //Definir el tamanho del papel para la impresion  aca 25 lineas y 80 columnas
        printer.setOutSize(60, 80);
        //Imprimir * de la 2da linea a 25 en la columna 1;
        // printer.printCharAtLin(2, 25, 1, "*");
        //Imprimir * 1ra linea de la columa de 1 a 80
        printer.printCharAtCol(1, 1, 80, "=");
        //Imprimir Encabezado nombre del La EMpresa
        printer.printTextWrap(1, 2, 1, 60, "FECHA ENVIO: " + fecha_envio);
        printer.printTextWrap(1, 2, 61, 80, varios_impresion.texto_derecha(19, documento));
        //printer.printTextWrap(linI, linE, colI, colE, null);
        printer.printTextWrap(2, 3, 1, 49, razon_empresa);
        printer.printTextWrap(2, 3, 60, 70, " SERIE:");
        printer.printTextWrap(2, 3, 71, 80, varios_impresion.texto_derecha(9, serie));
        printer.printTextWrap(3, 3, 1, 50, "RUC. : " + ruc_empresa);
        printer.printTextWrap(3, 3, 60, 70, "NUMERO:");
        printer.printTextWrap(3, 3, 72, 80, varios_impresion.texto_derecha(8, numero));
        printer.printTextWrap(4, 4, 1, 80, direcion_empresa);
        printer.printTextWrap(5, 5, 1, 80, "VENTA DE REGALOS, LIBROS, BIBLIAS Y ACCESORIOS EN GENERAL");
        printer.printTextWrap(7, 7, 1, 80, "Partida:  " + partida);
        printer.printTextWrap(8, 8, 1, 80, "Llegada:  " + llegada);

        printer.printCharAtCol(11, 1, 80, "=");
        printer.printTextWrap(11, 12, 1, 80, "Cant U.M. Producto                                                     P. Vta.");
        printer.printCharAtCol(13, 1, 80, "=");

        int filas = 0;

        try {
            Statement st = con.conexion();
            String c_detalle = "select de.cantidad, p.descripcion, p.codigo_externo, p.precio from detalle_envio as de inner join productos as p on p.id_producto = de.id_producto where "
                    + "de.id_envio = '" + envio + "' and de.anio = '" + anio + "' and de.empresa = '" + empresa + "' and de.id_almacen = '" + tienda + "'";
            ResultSet rs = con.consulta(st, c_detalle);
            while (rs.next()) {
                filas++;
                printer.printTextWrap(12 + filas, 13 + filas + 1 , 1, 4, rs.getInt("cantidad") + "");
                printer.printTextWrap(12 + filas, 13 + filas + 1, 5, 10, "UND");
                printer.printTextWrap(12 + filas, 13 + filas + 1, 11, 73, varios_impresion.remove(rs.getString("descripcion")) + " " + rs.getString("codigo_externo"));
                printer.printTextWrap(12 + filas, 13 + filas + 1, 74, 80, varios_impresion.texto_derecha(6, rs.getString("precio")));
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }

        printer.printCharAtCol(55, 1, 80, "=");
        printer.printTextWrap(56, 56, 2, 80, "MOTIVO: " + tipo_envio);

        printer.toFile("impresion.txt");

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream("impresion.txt");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        if (inputStream == null) {
            return;
        }
        
         DocFlavor docFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
         Doc document = new SimpleDoc(inputStream, docFormat, null);

         PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();

         PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();

         if (defaultPrintService != null) {
         DocPrintJob printJob = defaultPrintService.createPrintJob();
         try {
         printJob.print(document, attributeSet);

         } catch (Exception ex) {
         ex.printStackTrace();
         }
         } else {
         System.err.println("No existen impresoras instaladas");
         }
        
    }
}
